package com.github.emw7.foo.service.rest;

import com.github.emw7.foo.logic.FooService;
import com.github.emw7.foo.logic.FooService.FooPatcher;
import com.github.emw7.foo.logic.FooService.ListFilter;
import com.github.emw7.foo.model.Foo;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/foo")
public class DefaultFooControllerRest implements FooControllerRest {

  private final FooService fooService;

  public DefaultFooControllerRest(final FooService fooService) {
    this.fooService = fooService;
  }

  // region API
  // create
  @PostMapping
  public @NonNull Foo fooPost(@NonNull @RequestBody final Foo foo) throws RequestErrorException {
    System.out.printf("[EMW7] %s; foo: %s%n", "fooPost", foo);
    return fooService.create(foo);
  }

  // retrieve
  @GetMapping("/{id}")
  public @NonNull Foo fooGet(@RequestHeader @NonNull final HttpHeaders httpHeaders,
      @NonNull final @PathVariable String id) throws NotFoundClientException {
    //System.out.printf("[EMW7] %s; id: %s%n", "fooGet", uuid);

    //System.out.printf("trace %s, span %s%n", TracingContainer.traceId(), TracingContainer.spanId());

    //System.out.printf("Caller: %s%n", RequestContextHolder.get().caller());
    //System.out.printf("Originator: %s%n", RequestContextHolder.get().originator());

    //System.out.println(new DefaultValueStyler().style(Map.of("name", "enrico", "age", 47)));
    return fooService.retrieve(id);
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
  @GetMapping
  public @NonNull PagedModel<Foo> fooList(@Nullable @RequestParam(required = false) String name,
      @Nullable @RequestParam(required = false) Integer imin,
      @Nullable @RequestParam(required = false) Integer imax,
      @Nullable @RequestParam(required = false) Integer iequals,
      @NonNull @SortDefault(sort = "name", direction = Direction.DESC) @SortDefault(sort = "i", direction = Direction.ASC) final Pageable pageable)
      throws ServerRequestErrorException {
    final List<Foo> foos = fooService.list(
        new ListFilter(name, imin, imax, iequals),
        pageable);
    return new PagedModel<>(new PageImpl<>(foos, pageable, foos.size()));
  }

  // update full
  @PutMapping("/{id}")
  public @NonNull Foo fooPut(@NonNull @PathVariable final String id,
      @NonNull @RequestBody final Foo foo)
      throws NotFoundClientException, ServerRequestErrorException {
    return fooService.update(id, foo);
  }

  // update partial
  @PatchMapping("/{id}")
  public @NonNull Foo fooPatch(@NonNull @PathVariable final String id,
      @NonNull @RequestBody final FooPatcher fooPatcher)
      throws NotFoundClientException, ServerRequestErrorException {
    return fooService.patch(id, fooPatcher);
  }

  // delete one
  @DeleteMapping("/{id}")
  public void fooDelete(@NonNull @PathVariable String id) throws ServerRequestErrorException {
    fooService.delete(id);
  }

  // delete all
  @PostMapping("/op:delete-all")
  public void fooDeleteAll(@NonNull @RequestBody Ids ids) throws ServerRequestErrorException {
    fooService.delete(ids.ids());
  }
  //endregion API

}
