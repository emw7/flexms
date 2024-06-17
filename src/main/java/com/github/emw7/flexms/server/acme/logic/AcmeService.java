package com.github.emw7.flexms.server.acme.logic;

import com.github.emw7.flexms.commons.acme.Foo;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.SortDefault;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AcmeService {

  // create
  @NonNull Foo create (@NonNull final Foo foo) throws RequestErrorException;// throws /*BadPropertiesClientException,*/ ServerRequestErrorException;

  // retrieve
  @NonNull Foo retrieve(@NonNull final String uuid) throws NotFoundClientException;

  // list
  public record ListFilter (
    String nameStartsWith,
    Integer imin,
    Integer imax,
    Integer iequals) {}

  @NonNull PagedModel<Foo> list(@NonNull final ListFilter listFilter,
      @NonNull final Pageable pageable) throws ServerRequestErrorException;

  // update
  @NonNull Foo update(@NonNull final Foo foo) throws NotFoundClientException, ServerRequestErrorException;

  // patch
  // TODO usare Foo o FooPatched?
  //  Foo potrebbe forzare a non-null alcuni campi, invece FooPatched potrebbe accettare a null i campi che non sono da aggiornare.
  @NonNull void patch(@NonNull List<String> uuids, @NonNull final Foo foo) throws NotFoundClientException, ServerRequestErrorException;


  // delete
  void delete(@NonNull String uuid) throws ServerRequestErrorException;
  void delete(@NonNull List<String> uuids) throws ServerRequestErrorException;

}
