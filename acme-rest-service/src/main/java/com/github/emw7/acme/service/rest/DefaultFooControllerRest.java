package com.github.emw7.acme.service.rest;

import com.github.emw7.acme.model.Acme;
import com.github.emw7.acme.service.logic.AcmeService;
import com.github.emw7.platform.app.request.context.RequestContextHolder;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.telemetry.tracing.TracingContainer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import org.springframework.core.style.DefaultValueStyler;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/vx/acme")
public class DefaultFooControllerRest implements FooControllerRest {

  private final AcmeService acmeService;

  public DefaultFooControllerRest(final AcmeService acmeService) {
    this.acmeService = acmeService;
  }

  // region API
  // create
  @PostMapping("/foo")
  public @NonNull Acme fooPost(@NonNull @RequestBody final Acme foo) throws RequestErrorException {
    System.out.printf("[EMW7] %s; foo: %s%n", "fooPost", foo);
    final Acme fooCreated = acmeService.create(foo);
    return fooCreated;
  }

  // retrieve
  @GetMapping("/foo/{id}")
  public @NonNull Acme fooGet(@RequestHeader @NonNull final HttpHeaders httpHeaders,
      @NonNull final @PathVariable String uuid) {
    System.out.printf("[EMW7] %s; id: %s%n", "fooGet", uuid);

    System.out.printf("trace %s, span %s%n", TracingContainer.traceId(), TracingContainer.spanId());

    System.out.printf("Caller: %s%n", RequestContextHolder.get().caller());
    System.out.printf("Originator: %s%n", RequestContextHolder.get().originator());

    System.out.println(new DefaultValueStyler().style(Map.of("name", "enrico", "age", 47)));
    return new Acme(uuid, "", 0);
  }

  // list
  // Mah... per fare list con filtri... non si può usare Foo... quindi si deve, come faceva il business
  //  service usare una classe filtro che "wrappa" il modello... altrimenti si usano i @RequestParam
  //  e poi si mappano in una classe... per cui a questo punto conviene usare la classe... che però
  //  forse NON può essere record o @Value... ma deve avere un costruttore per ogni property e quindi
  //  getter e setter... prove da fare... forse con lombok... che però vorrei evitare!

  //  class FooFilter {
//    Integer imin;
//    Integer imax;
//    Integer iequals;
//
//    String[] names;
//
//    String id;
//  }
  @GetMapping("/foo")
  public @NonNull PagedModel<Acme> fooList(@Nullable @RequestParam(required = false) String name,
      @Nullable @RequestParam(required = false) Integer imin,
      @Nullable @RequestParam(required = false) Integer imax,
      @Nullable @RequestParam(required = false) Integer iequals,
      @Nullable @SortDefault(sort = "name", direction = Direction.DESC) @SortDefault(sort = "i", direction = Direction.ASC) final Pageable pageable) {
    System.out.printf("[EMW7] %s; imin: %s, imax: %s, iequals: %s  , pageable: %s%n", "fooList",
        Optional.ofNullable(imin).map(String::valueOf).orElse("null"),
        Optional.ofNullable(imax).map(String::valueOf).orElse("null"),
        Optional.ofNullable(iequals).map(String::valueOf).orElse("null"), pageable);

    final Comparator<Acme> comparator;
    try {
      comparator = x(pageable.getSort());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    List<Acme> foos = generateRandomFoos(imin, imax, 99);
    int si = pageable.getPageNumber() * pageable.getPageSize();
    int ee = Integer.min(foos.size(), si + (int) pageable.getPageSize());
    if (pageable.getOffset() >= foos.size()) {
      si = 0; //(si < foos.size()) ? si : 0;
      ee = 0; //(ee <= foos.size()) ? ee : 0;
    }
    foos.sort(comparator);
    return new PagedModel<>(new PageImpl<>(foos.subList(si, ee), pageable, foos.size()));
  }

  // update
  @PutMapping("/foo/{id}")
  public @NonNull Acme fooPut(@NonNull @PathVariable String uuid,
      @NonNull @RequestBody final Acme foo) {
    System.out.printf("[EMW7] %s; id: %s, foo: %s%n", "fooPut", uuid, foo);
    return new Acme(uuid, foo.name(), foo.i());
  }

  // delete
  @DeleteMapping("/foo/{id}")
  public void fooDelete(@NonNull @PathVariable String uuid) {
    System.out.printf("[EMW7] %s; id: %s%n", "fooDelete", uuid);
  }
  //endregion API

  //region Private methods

  /**
   * @param imin
   * @param imax
   * @param howManyFooToGenerate if negative then generates a number of Foos among 0 and
   *                             -howManyFooToGenerate
   * @return
   */
  private List<Acme> generateRandomFoos(@Nullable final Integer imin, @NonNull final Integer imax,
      final int howManyFooToGenerate) {
    final int num = (howManyFooToGenerate >= 0) ? howManyFooToGenerate :
        // +2 because 2nd parameter is exclusive.
        ThreadLocalRandom.current().nextInt(0, -howManyFooToGenerate + 2);
    final List<Acme> foos = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      int max = Optional.ofNullable(imax).orElse(Integer.MAX_VALUE);
      if (max < Integer.MAX_VALUE) {
        max += 1;
      } // because imax is inclusive then
      //  if max < Integer.MAX_VALUE we must add 1
      //  because in the row it is used as bound that
      //  is exclusive.
      final int id = ThreadLocalRandom.current().nextInt(Optional.ofNullable(imin).orElse(Integer.MIN_VALUE), max);
      final Acme foo = new Acme(UUID.randomUUID().toString(), "foo-" + id, id);
      foos.add(foo);
    }
    return foos;
  }

  private Comparator<Acme> x(Sort sort) throws Exception {

    if (sort.isUnsorted()) {
      return Comparator.nullsLast(new Comparator<Acme>() {
        @Override
        public int compare(final Acme o1, final Acme o2) {
          return 0;
        }
      });
    }
    // else...
    Iterator<Order> orderIterator = sort.iterator();
    Comparator<Acme> comparator = y(orderIterator.next());
    while (orderIterator.hasNext()) {
      comparator = comparator.thenComparing(y(orderIterator.next()));
    }
//    return new Comparator<Foo>() {
//      @Override
//      public int compare(final Foo left, final Foo right) {
//        for ( Order order: sort.orders())
//      }
//    };
//  }
    return Comparator.nullsLast(comparator);
  }

  private Comparator<Acme> y(Order order) throws Exception {
    String propertyName = order.getProperty();
    Comparator comparator = Comparator.comparing(valueExtractor(Acme.class, propertyName)
        /*foo -> { try { return (Comparable)Foo.class.getDeclaredField(propertyName).get(foo); } catch (Exception e ) { throw new IllegalArgumentException(e); }}*/);
    if (order.isDescending()) {
      return comparator.reversed();
    } else {
      return comparator;
    }
  }

  private <T, R extends Comparable> Function<T, R> valueExtractor(
      @Nullable final Class<T> itemClass, @NonNull final String propertyName) {
    try {
      Field field = itemClass.getDeclaredField(propertyName);
      if (field.trySetAccessible()) {
        return $ -> {
          try {
            return (R) field.get($);
          } catch (IllegalAccessException e) {
            return null;
          }
        };
      } else {
        return $ -> null;
      }
    } catch (NoSuchFieldException e) {
      return $ -> null;
    }
  }
  //endregion Private methods

}
