package com.github.emw7.foo.logic;

import com.github.emw7.foo.logic.error.FooNotFoundException;
import com.github.emw7.foo.model.Foo;
import com.github.emw7.platform.error.AlreadyExistsClientException;
import com.github.emw7.platform.error.BadPropertiesClientException;
import com.github.emw7.platform.error.ServiceNotFoundServerException;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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
public class DefaultFooService implements FooService {

  private void throwRequestedException(@Nullable final String requestedException)
      throws RequestErrorException {
    switch (requestedException) {
      case "#already-exists":
        throw new AlreadyExistsClientException(new Id("SETKH"),
            "foo", 1, null) {
        };
      case "#bad-properties":
        throw new BadPropertiesClientException(null, new Id("3V6VI"),
            List.of(BadPropertiesClientException.min("i", -1, 0)));
      case "#dependency-unavailable":
        throw new ServiceNotFoundServerException(null, new Id("5OMN5"), "master", "slave",
            null);
      case "#not-found":
        throw new FooNotFoundException(new Id("WV4KE"), ":id", null) {
        };
      case null:
      default:
        return;
    }
  }

  /**
   * Set Foo.id to:
   * <ul>
   * <li>#already-exist to trigger an AlreadyExistsClientException</li>
   * <li>#bad-properties to trigger a BadPropertiesClientException</li>
   * <li>#depenecny-unavailable to trigger a DependencyUnavailableServerException</li>
   * </ul>
   *
   * @param foo
   * @return
   * @throws ServerRequestErrorException
   */
  @Override
  public @NonNull Foo create(@NonNull final Foo foo)
      throws RequestErrorException {
//    if (foo.id() != null) {
//      //throw new BadPropertiesBadRequestException("en:app.error.Foo.create", "app.error.Foo.create", Map.of());
//      throw new DependencyUnavailableServerException(null, new Id("B31MY"), "caller", "callee",
//          null);
//    }
    throwRequestedException(foo.id());
    final String id=  UUID.randomUUID().toString();
    return new Foo(id, foo.name(), foo.i());
  }

  /**
   * Set Foo.id to:
   * <ul>
   * <li>#not-found to trigger an NotFoundClientException</li>
   * </ul>
   *
   * @param id
   * @return
   * @throws NotFoundClientException
   */
  @Override
  public @NonNull Foo retrieve(@NonNull final String id) throws NotFoundClientException {
    return new Foo(id, "foo", Math.abs(UUID.fromString(id).hashCode()));
  }

  @NonNull
  @Override
  public List<Foo> list(@NonNull final ListFilter listFilter,
      @NonNull final Pageable pageable) throws ServerRequestErrorException {
    System.out.printf("[EMW7] %s; imin: %s, imax: %s, iequals: %s  , pageable: %s%n", "fooList",
        Optional.ofNullable(listFilter.imin()).map(String::valueOf).orElse("null"),
        Optional.ofNullable(listFilter.imax()).map(String::valueOf).orElse("null"),
        Optional.ofNullable(listFilter.iequals()).map(String::valueOf).orElse("null"), pageable);

    final Comparator<Foo> comparator;
    try {
      comparator = x(pageable.getSort());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    List<Foo> foos = generateRandomFoos(listFilter.imin(), listFilter.imax(), 99);
    int si = pageable.getPageNumber() * pageable.getPageSize();
    int ee = Integer.min(foos.size(), si + (int) pageable.getPageSize());
    if (pageable.getOffset() >= foos.size()) {
      si = 0; //(si < foos.size()) ? si : 0;
      ee = 0; //(ee <= foos.size()) ? ee : 0;
    }
    foos.sort(comparator);
    return foos.subList(si,ee);
  }

  @NonNull
  @Override
  public Foo update(@NonNull final String id, @NonNull final Foo foo)
      throws NotFoundClientException, ServerRequestErrorException {
    class ThrowRequestedException {

      private void throwRequestedException(@Nullable final String requestedException)
          throws FooNotFoundException {
        switch (requestedException) {
          case "#not-found":
            throw new FooNotFoundException(new Id("WV4KE"), ":id", null) {
            };
          case null:
          default:
            return;
        }
      }
    }
    //System.out.printf("[EMW7] %s; id: %s, foo: %s%n", "fooPut", uuid, foo);
    new ThrowRequestedException().throwRequestedException(id);
    return new Foo(id, foo.name(), foo.i());
  }

  @Override
  public @NonNull Foo patch(@NonNull final String id, @NonNull final FooPatcher fooPatcher)
      throws NotFoundClientException, ServerRequestErrorException {
    return fooPatcher.patch(retrieve(id));
  }

  @Override
  public void delete(@NonNull final String id) throws ServerRequestErrorException {
// System.out.printf("[EMW7] %s; id: %s%n", "fooDelete", uuid);
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
   * @param imin
   * @param imax
   * @param howManyFooToGenerate if negative then generates a number of Foos among 0 and
   *                             -howManyFooToGenerate
   * @return
   */
  private List<Foo> generateRandomFoos(@Nullable final Integer imin, @NonNull final Integer imax,
      final int howManyFooToGenerate) {
    final int num = (howManyFooToGenerate >= 0) ? howManyFooToGenerate :
        // +2 because 2nd parameter is exclusive.
        ThreadLocalRandom.current().nextInt(0, -howManyFooToGenerate + 2);
    final List<Foo> foos = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      int max = Optional.ofNullable(imax).orElse(Integer.MAX_VALUE);
      if (max < Integer.MAX_VALUE) {
        max += 1;
      } // because imax is inclusive then
      //  if max < Integer.MAX_VALUE we must add 1
      //  because in the row it is used as bound that
      //  is exclusive.
      final int ir = ThreadLocalRandom.current().nextInt(Optional.ofNullable(imin).orElse(Integer.MIN_VALUE), max);
      final UUID uuid= UUID.randomUUID();
      final Foo foo = new Foo(uuid.toString(), "foo-" + ir, Math.abs(uuid.hashCode()));
      foos.add(foo);
    }
    return foos;
  }

  private Comparator<Foo> x(Sort sort) throws Exception {

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
      comparator = comparator.thenComparing(y(orderIterator.next()));
    }
    return Comparator.nullsLast(comparator);
  }

  private Comparator<Foo> y(Order order) throws Exception {
    String propertyName = order.getProperty();
    Comparator comparator = Comparator.comparing(valueExtractor(Foo.class, propertyName)
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
