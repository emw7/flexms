package com.github.emw7.flexms.server.acme.logic;

import static com.github.emw7.platform.error.RequestErrorException.appClientErrorLabel;

import com.github.emw7.flexms.commons.acme.Foo;
import com.github.emw7.platform.error.AlreadyExistsClientException;
import com.github.emw7.platform.error.BadPropertiesClientException;
import com.github.emw7.platform.app.error.exception.DependencyUnavailableServerException;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class AcmeServiceDefault implements AcmeService {

  private void throwRequestedException(@Nullable final String requestedException)
      throws RequestErrorException {
    switch (requestedException) {
      case "#already-exists":
        throw new AlreadyExistsClientException(new Id("SETKH"),
            appClientErrorLabel("foo.already-exists"), "foo", 1, null) {
        };
      case "#bad-properties":
        throw new BadPropertiesClientException(null, new Id("3V6VI"),
            List.of(BadPropertiesClientException.min("i", -1, 0)));
      case "#dependency-unavailable":
        throw new DependencyUnavailableServerException(null, new Id("5OMN5"), "master", "slave",
            null);
      case "#not-found":
        throw new NotFoundClientException(new Id("WV4KE"), "foo", "y", null) {
        };
      case null:
      default:
        return;
    }
  }

  /**
   * Set Foo.uuid to:
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
//    if (foo.uuid() != null) {
//      //throw new BadPropertiesBadRequestException("en:app.error.acme.create", "app.error.acme.create", Map.of());
//      throw new DependencyUnavailableServerException(null, new Id("B31MY"), "caller", "callee",
//          null);
//    }
    throwRequestedException(foo.uuid());
    return new Foo(UUID.randomUUID().toString(), foo.name(), foo.i());
  }

  /**
   * Set Foo.uuid to:
   * <ul>
   * <li>#not-found to trigger an NotFoundClientException</li>
   * </ul>
   *
   * @param uuid
   * @return
   * @throws NotFoundClientException
   */
  @Override
  public @NonNull Foo retrieve(@NonNull final String uuid) throws NotFoundClientException {
    return null;
  }

  @NonNull
  @Override
  public PagedModel<Foo> list(@NonNull final ListFilter listFilter,
      @NonNull final Pageable pageable) throws ServerRequestErrorException {
    return null;
  }

  @NonNull
  @Override
  public Foo update(@NonNull final Foo foo)
      throws NotFoundClientException, ServerRequestErrorException {
    return null;
  }

  @NonNull
  @Override
  public void patch(@NonNull final List<String> uuids, @NonNull final Foo foo)
      throws NotFoundClientException, ServerRequestErrorException {

  }

  @Override
  public void delete(@NonNull final String uuid) throws ServerRequestErrorException {

  }

  @Override
  public void delete(@NonNull final List<String> uuids) throws ServerRequestErrorException {

  }
}
