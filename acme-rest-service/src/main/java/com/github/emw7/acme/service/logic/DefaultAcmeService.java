package com.github.emw7.acme.service.logic;

import com.github.emw7.acme.model.Acme;
import com.github.emw7.platform.error.AlreadyExistsClientException;
import com.github.emw7.platform.error.BadPropertiesClientException;
import com.github.emw7.platform.error.ServiceNotFoundServerException;
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
public class DefaultAcmeService implements AcmeService {

  private void throwRequestedException(@Nullable final String requestedException)
      throws RequestErrorException {
    switch (requestedException) {
      case "#already-exists":
        throw new AlreadyExistsClientException(new Id("SETKH"),
            "acme", 1, null) {
        };
      case "#bad-properties":
        throw new BadPropertiesClientException(null, new Id("3V6VI"),
            List.of(BadPropertiesClientException.min("i", -1, 0)));
      case "#dependency-unavailable":
        throw new ServiceNotFoundServerException(null, new Id("5OMN5"), "master", "slave",
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
  public @NonNull Acme create(@NonNull final Acme foo)
      throws RequestErrorException {
//    if (foo.id() != null) {
//      //throw new BadPropertiesBadRequestException("en:app.error.acme.create", "app.error.acme.create", Map.of());
//      throw new DependencyUnavailableServerException(null, new Id("B31MY"), "caller", "callee",
//          null);
//    }
    throwRequestedException(foo.id());
    return new Acme(UUID.randomUUID().toString(), foo.name(), foo.i());
  }

  /**
   * Set Foo.id to:
   * <ul>
   * <li>#not-found to trigger an NotFoundClientException</li>
   * </ul>
   *
   * @param uuid
   * @return
   * @throws NotFoundClientException
   */
  @Override
  public @NonNull Acme retrieve(@NonNull final String uuid) throws NotFoundClientException {
    return null;
  }

  @NonNull
  @Override
  public PagedModel<Acme> list(@NonNull final ListFilter listFilter,
      @NonNull final Pageable pageable) throws ServerRequestErrorException {
    return null;
  }

  @NonNull
  @Override
  public Acme update(@NonNull final Acme foo)
      throws NotFoundClientException, ServerRequestErrorException {
    return null;
  }

  @NonNull
  @Override
  public void patch(@NonNull final List<String> uuids, @NonNull final Acme foo)
      throws NotFoundClientException, ServerRequestErrorException {

  }

  @Override
  public void delete(@NonNull final String uuid) throws ServerRequestErrorException {

  }

  @Override
  public void delete(@NonNull final List<String> uuids) throws ServerRequestErrorException {

  }
}
