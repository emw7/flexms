package com.github.emw7.foo.service.foo.logic;

import com.github.emw7.foo.model.Foo;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface FooService {

  // create
  @NonNull
  Foo create(@NonNull final Foo foo)
      throws RequestErrorException;// throws /*BadPropertiesClientException,*/ ServerRequestErrorException;

  // retrieve
  @NonNull
  Foo retrieve(@NonNull final String id) throws NotFoundClientException;

  // list
  record ListFilter(String nameStartsWith, Integer imin, Integer imax, Integer iequals) {}

  @NonNull
  List<Foo> list(@NonNull final ListFilter listFilter, @NonNull final Pageable pageable)
      throws ServerRequestErrorException;

  // update
  @NonNull
  Foo update(@NonNull final String id, @NonNull final Foo foo) throws NotFoundClientException, ServerRequestErrorException;

  // patch
//  final class FooPatcher  implements ModelPatcher<Foo>{
//    private boolean resetName;
//    private String name;
//    private Integer i;
//
//    private FooPatcher(final boolean resetName, final String name, final Integer i) {
//      this.resetName = resetName;
//      this.name = name;
//      this.i = i;
//    }
//
//    public Foo patch(@NonNull final Foo foo)
//    {
//      return new Foo(
//          foo.id(),
//          ( resetName || name != null ) ? name : foo.name(),
//          ( i != null ) ? i : foo.i(),
//          foo.creationTime());
//    }
//  }
  record FooPatcher(boolean resetName, String name, Integer i)  implements ModelPatcher<Foo>{

    public Foo patch(@NonNull final Foo foo)
    {
      return new Foo(
          foo.id(),
          ( resetName || name != null ) ? name : foo.name(),
          ( i != null ) ? i : foo.i(),
          foo.creationTime());
    }
  }

  @NonNull Foo patch(@NonNull String id, @NonNull final FooService.FooPatcher fooPatcher)
      throws NotFoundClientException, ServerRequestErrorException;


  // delete
  void delete(@NonNull String id) throws ServerRequestErrorException;

  void delete(@NonNull Set<String> ids) throws ServerRequestErrorException;

}
