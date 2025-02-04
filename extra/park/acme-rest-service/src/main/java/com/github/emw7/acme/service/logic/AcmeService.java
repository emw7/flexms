package com.github.emw7.acme.service.logic;

import com.github.emw7.acme.model.Acme;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.lang.NonNull;

public interface AcmeService {

  // create
  @NonNull
  Acme create(@NonNull final Acme foo)
      throws RequestErrorException;// throws /*BadPropertiesClientException,*/ ServerRequestErrorException;

  // retrieve
  @NonNull
  Acme retrieve(@NonNull final String uuid) throws NotFoundClientException;

  // list
  public record ListFilter(String nameStartsWith, Integer imin, Integer imax, Integer iequals) {

  }

  @NonNull
  PagedModel<Acme> list(@NonNull final ListFilter listFilter, @NonNull final Pageable pageable)
      throws ServerRequestErrorException;

  // update
  @NonNull
  Acme update(@NonNull final Acme foo) throws NotFoundClientException, ServerRequestErrorException;

  // patch
  // TODO usare Foo o FooPatched?
  //  Foo potrebbe forzare a non-null alcuni campi, invece FooPatched potrebbe accettare a null i campi che non sono da aggiornare.
  @NonNull
  void patch(@NonNull List<String> uuids, @NonNull final Acme foo)
      throws NotFoundClientException, ServerRequestErrorException;


  // delete
  void delete(@NonNull String uuid) throws ServerRequestErrorException;

  void delete(@NonNull List<String> uuids) throws ServerRequestErrorException;

}
