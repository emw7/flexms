package com.github.emw7.bar.logic;

import com.github.emw7.bar.logic.api.BarService;
import com.github.emw7.bar.logic.api.error.BarNotFoundException;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.bar.model.Bar.Severity;
import com.github.emw7.bar.model.Bar.State;
import com.github.emw7.platform.error.AlreadyExistsClientException;
import com.github.emw7.platform.error.BadPropertiesClientException;
import com.github.emw7.platform.error.ServiceNotFoundServerException;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class DefaultBarService implements BarService {

  private void throwRequestedException(@Nullable final String requestedException)
      throws RequestErrorException {
//    switch (requestedException) {
//      case "#already-exists":
//        throw new AlreadyExistsClientException(new Id("SETKH"),
//            "bar", 1, null) {
//        };
//      case "#bad-properties":
//        throw new BadPropertiesClientException(null, new Id("3V6VI"),
//            List.of(BadPropertiesClientException.min("i", -1, 0)));
//      case "#dependency-unavailable":
//        throw new ServiceNotFoundServerException(null, new Id("5OMN5"), "master", "slave",
//            null);
//      case "#not-found":
//        throw new BarNotFoundException(new Id("WV4KE"), ":id", null) {
//        };
//      case null:
//      default:
//        return;
//    }
  }

  /**
   * Set Bar.id to:
   * <ul>
   * <li>#already-exist to trigger an AlreadyExistsClientException</li>
   * <li>#bad-properties to trigger a BadPropertiesClientException</li>
   * <li>#depenecny-unavailable to trigger a DependencyUnavailableServerException</li>
   * </ul>
   *
   * @param bar
   * @return
   * @throws ServerRequestErrorException
   */
  @Override
  public @NonNull Bar create(@NonNull final Bar bar)
      throws RequestErrorException {
//    if (bar.id() != null) {
//      //throw new BadPropertiesBadRequestException("en:app.error.Bar.create", "app.error.Bar.create", Map.of());
//      throw new DependencyUnavailableServerException(null, new Id("B31MY"), "caller", "callee",
//          null);
//    }
    throwRequestedException(bar.id());
    final String id=  UUID.randomUUID().toString();
    return new Bar(id, bar.type(), bar.descriptionLabel(), bar.severity(), bar.state(),
        ZonedDateTime.now());
  }

  /**
   * Set Bar.id to:
   * <ul>
   * <li>#not-found to trigger an NotFoundClientException</li>
   * </ul>
   *
   * @param id
   * @return
   * @throws NotFoundClientException
   */
  @Override
  public @NonNull Bar retrieve(@NonNull final String id) throws NotFoundClientException {
    return new Bar(id, "a bar", "a.bar", Severity.NONE, State.PUSH, ZonedDateTime.now());
  }

  @NonNull
  @Override
  public List<Bar> list(@NonNull final ListFilter listFilter,
      @NonNull final Pageable pageable) throws ServerRequestErrorException {
//    System.out.printf("[EMW7] %s; imin: %s, imax: %s, iequals: %s  , pageable: %s%n", "barList",
//        Optional.ofNullable(listFilter.imin()).map(String::valueOf).orElse("null"),
//        Optional.ofNullable(listFilter.imax()).map(String::valueOf).orElse("null"),
//        Optional.ofNullable(listFilter.iequals()).map(String::valueOf).orElse("null"), pageable);

    final Comparator<Bar> comparator;
    try {
      comparator = x(pageable.getSort());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    List<Bar> bars = generateRandomBars(listFilter, 99);
    int si = pageable.getPageNumber() * pageable.getPageSize();
    int ee = Integer.min(bars.size(), si + (int) pageable.getPageSize());
    if (pageable.getOffset() >= bars.size()) {
      si = 0; //(si < bars.size()) ? si : 0;
      ee = 0; //(ee <= bars.size()) ? ee : 0;
    }
    bars.sort(comparator);
    return bars.subList(si,ee);
  }

  /**
   * bar.id() is ignored, that is returned entity has id equals to specified {@code id}.
   *
   * @param id the id of the entity to be upated
   * @param bar
   * @return
   * @throws NotFoundClientException
   * @throws ServerRequestErrorException
   */
  @Override
  public @NonNull Bar update(@NonNull final String id, @NonNull final Bar bar)
      throws NotFoundClientException, ServerRequestErrorException {
    class ThrowRequestedException {

      private void throwRequestedException(@Nullable final String requestedException)
          throws BarNotFoundException {
        switch (requestedException) {
          case "#not-found":
            throw new BarNotFoundException(new Id("WV4KE"), ":id", null) {
            };
          case null:
          default:
            return;
        }
      }
    }
    //System.out.printf("[EMW7] %s; id: %s, bar: %s%n", "barPut", uuid, bar);
    new ThrowRequestedException().throwRequestedException(id);
    return new Bar(id, bar.type(), bar.descriptionLabel(), bar.severity(), bar.state(),
        bar.t());
  }

  @Override
  public @NonNull Bar patch(@NonNull final String id, @NonNull final BarPatcher barPatcher)
      throws NotFoundClientException, ServerRequestErrorException {
    return barPatcher.patch(retrieve(id));
  }

  @Override
  public void delete(@NonNull final String id) throws ServerRequestErrorException {
// System.out.printf("[EMW7] %s; id: %s%n", "barDelete", uuid);
  }

  @Override
  public void delete(@NonNull final Set<String> ids) throws ServerRequestErrorException {
    // TODO massive operations.
    // do not use this::delete... must be decided the semantic:
    //  - delete all or none
    //  - keep deleted the ones that can be deleted and report errors for the ones that could not.
    ids.forEach(id -> System.out.printf("delete %s%n",id));
  }

  //region Private methods

  /**
   * @param howManyBarToGenerate if negative then generates a number of Bars among 0 and
   *                             -howManyBarToGenerate
   * @return
   */
  private List<Bar> generateRandomBars(ListFilter listFilter,
      final int howManyBarToGenerate) {

    final int num = (howManyBarToGenerate >= 0) ? howManyBarToGenerate :
        // +2 because 2nd parameter is exclusive.
        ThreadLocalRandom.current().nextInt(0, -howManyBarToGenerate + 2);

    final List<Bar> bars = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      final UUID uuid= UUID.randomUUID();
      final Bar bar = new Bar(uuid.toString(), i + "-bar", i +".bar", Severity.valueOf(
          ThreadLocalRandom.current().nextInt(Severity.NONE.severity(), Severity.CRITICAL.severity()+1)),
          State.valueOf(
              ThreadLocalRandom.current().nextInt(State.PUSH.state(), State.START.state()+1)),
          ZonedDateTime.now().minusHours(ThreadLocalRandom.current().nextInt(0, 25)));
      bars.add(bar);
    }
    return bars;
  }

  private Comparator<Bar> x(Sort sort) throws Exception {

    if (sort.isUnsorted()) {
      return Comparator.nullsLast(new Comparator<Bar>() {
        @Override
        public int compare(final Bar o1, final Bar o2) {
          return 0;
        }
      });
    }
    // else...
    Iterator<Order> orderIterator = sort.iterator();
    Comparator<Bar> comparator = y(orderIterator.next());
    while (orderIterator.hasNext()) {
      comparator = comparator.thenComparing(y(orderIterator.next()));
    }
    return Comparator.nullsLast(comparator);
  }

  private Comparator<Bar> y(Order order) throws Exception {
    String propertyName = order.getProperty();
    Comparator comparator = Comparator.comparing(valueExtractor(Bar.class, propertyName)
        /*bar -> { try { return (Comparable)Bar.class.getDeclaredField(propertyName).get(bar); } catch (Exception e ) { throw new IllegalArgumentException(e); }}*/);
    if (order.isDescending()) {
      return comparator.reversed();
    } else {
      return comparator;
    }
  }

  private <T, R extends Comparable> Function<T, R> valueExtractor(
      @NonNull final Class<T> itemClass, @NonNull final String propertyName) {
    try {
      Field field = itemClass.getDeclaredField(propertyName);
      if (field.trySetAccessible()) {
        return $ -> {
          try {
            // FIXME this does not work with enum as field.get($) returns name() and so the order
            //  is on enum constant names.
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
