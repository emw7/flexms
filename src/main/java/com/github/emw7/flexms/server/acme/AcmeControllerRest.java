package com.github.emw7.flexms.server.acme;

import com.github.emw7.flexms.commons.acme.Foo;
import com.github.emw7.flexms.server.acme.logic.AcmeService;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.SortDefault;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/vx/acme")
public class AcmeControllerRest implements AcmeController {

  private final AcmeService acmeService;

  public AcmeControllerRest(final AcmeService acmeService) {
    this.acmeService = acmeService;
  }

  // region API
  // create
  @PostMapping("/foo")
  public @NonNull Foo fooPost(@NonNull @RequestBody final Foo foo) throws Exception {
    System.out.printf("[EMW7] %s; foo: %s%n", "fooPost", foo);
    //return new Foo(UUID.randomUUID().toString(), foo.name(), foo.i());
    final Foo fooCreated= acmeService.create(foo);
    return foo;
  }

  // retrieve
  @GetMapping("/foo/{uuid}")
  public @NonNull Foo fooGet(@NonNull final @PathVariable String uuid) {
    System.out.printf("[EMW7] %s; uuid: %s%n", "fooGet", uuid);
    return new Foo(uuid, "", 0);
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
//    String uuid;
//  }
  @GetMapping("/foo")
  public @NonNull PagedModel<Foo> fooList(@NonNull @RequestParam(required = false) Optional<String> name,
      @NonNull @RequestParam(required = false) Optional<Integer> imin,
      @NonNull @RequestParam(required = false) Optional<Integer> imax,
      @NonNull @RequestParam(required = false) Optional<Integer> iequals,
      @NonNull @SortDefault(sort = "name", direction = Direction.DESC) @SortDefault(sort = "i", direction = Direction.ASC) final Pageable pageable) {
    System.out.printf("[EMW7] %s; imin: %s, imax: %s, iequals: %s  , pageable: %s%n", "fooList",
        imin.map(String::valueOf).orElse("null"),
        imax.map(String::valueOf).orElse("null"),
        iequals.map(String::valueOf).orElse("null"),
        pageable);

    final Comparator<Foo> comparator;
    try {
      comparator= x(pageable.getSort());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    List<Foo> foos = generateRandomFoos(imin, imax, 99);
    int si= pageable.getPageNumber() * pageable.getPageSize();
    int ee= Integer.min(foos.size(), si+ (int)pageable.getPageSize());
    if ( pageable.getOffset() >= foos.size() ) {
      si = 0; //(si < foos.size()) ? si : 0;
      ee = 0; //(ee <= foos.size()) ? ee : 0;
    }
    foos.sort(comparator);
    return new PagedModel<>(new PageImpl<>(foos.subList(si,ee), pageable, foos.size()));
  }

  // update
  @PutMapping("/foo/{uuid}")
  public @NonNull Foo fooPut(@NonNull @PathVariable String uuid, @NonNull @RequestBody final Foo foo) {
    System.out.printf("[EMW7] %s; uuid: %s, foo: %s%n", "fooPut", uuid, foo);
    return new Foo(uuid, foo.name(), foo.i());
  }

  // delete
  @DeleteMapping("/foo/{uuid}")
  public void fooDelete(@NonNull @PathVariable String uuid) {
    System.out.printf("[EMW7] %s; uuid: %s%n", "fooDelete", uuid);
  }
  //endregion API

  //region Private methods

  /**
   *
   * @param imin
   * @param imax
   * @param howManyFooToGenerate if negative then generates a number of Foos among 0 and
   *                             -howManyFooToGenerate
   * @return
   */
  private List<Foo> generateRandomFoos (@NonNull final Optional<Integer> imin,
      @NonNull final Optional<Integer> imax, final int howManyFooToGenerate)
  {
    final int num= ( howManyFooToGenerate >= 0 ) ?
        howManyFooToGenerate :
        // +2 because 2nd parameter is exclusive.
        ThreadLocalRandom.current().nextInt(0,-howManyFooToGenerate + 2);
    final List<Foo> foos = new ArrayList<>();
    for ( int i=0; i < num; i++ ) {
      int max= imax.orElse(Integer.MAX_VALUE);
      if ( max < Integer.MAX_VALUE ) { max+= 1; } // because imax is inclusive then
                                                  //  if max < Integer.MAX_VALUE we must add 1
                                                  //  because in the row it is used as bound that
                                                  //  is exclusive.
      final int id= ThreadLocalRandom.current().nextInt(imin.orElse(Integer.MIN_VALUE),max);
      final Foo foo= new Foo(UUID.randomUUID().toString(), "foo-" + id, id);
      foos.add(foo);
    }
    return foos;
  }

  private Comparator<Foo> x (Sort sort)  throws Exception {

    if (sort.isUnsorted()) {
      return Comparator.nullsLast(new Comparator<Foo>() {
        @Override
        public int compare(final Foo o1, final Foo o2) {
          return 0;
        }
      });
    }
    // else...
    Iterator<Order> orderIterator = sort.iterator();
    Comparator<Foo> comparator = y(orderIterator.next());
    while (orderIterator.hasNext()) {
      comparator= comparator.thenComparing(y(orderIterator.next()));
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

  private Comparator<Foo> y (Order order) throws Exception{
    String propertyName= order.getProperty();
    Comparator comparator= Comparator.comparing(
        valueExtractor(Foo.class, propertyName)
      /*foo -> { try { return (Comparable)Foo.class.getDeclaredField(propertyName).get(foo); } catch (Exception e ) { throw new IllegalArgumentException(e); }}*/);
    if ( order.isDescending() ) {
      return comparator.reversed();
    }
    else {
      return comparator;
    }
  }

  private <T, R extends Comparable> Function<T, R> valueExtractor (@Nullable final Class<T> itemClass, @NonNull final String propertyName) {
    try {
      Field field= itemClass.getDeclaredField(propertyName);
      if ( field.trySetAccessible() ) {
      return $ -> { try { return (R)field.get($); } catch ( IllegalAccessException e ) { return null; }};
      }
      else {
        return $ -> null;
      }
    } catch ( NoSuchFieldException e ) {
      return $ -> null;
    }
  }
  //endregion Private methods

}
