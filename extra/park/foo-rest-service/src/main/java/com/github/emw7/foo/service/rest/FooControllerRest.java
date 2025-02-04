package com.github.emw7.foo.service.rest;

import com.github.emw7.foo.model.Foo;
import com.github.emw7.foo.logic.FooService.FooPatcher;
import com.github.emw7.platform.error.RequestErrorException;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface FooControllerRest {

  record Ids (@NonNull Set<String> ids) {}

  // create
  @NonNull
  Foo fooPost(@NonNull @RequestBody final Foo foo) throws RequestErrorException;

  // retrieve
  @NonNull
  Foo fooGet(@RequestHeader @NonNull final HttpHeaders httpHeaders,
      @NonNull @PathVariable final String id) throws RequestErrorException;

  // list
  @NonNull
  PagedModel<Foo> fooList(@Nullable @RequestParam(required = false) final String name,
      @Nullable @RequestParam(required = false) final Integer imin,
      @Nullable @RequestParam(required = false) final Integer imax,
      @Nullable @RequestParam(required = false) final Integer iequals,
      @NonNull final Pageable pageable) throws RequestErrorException;

  // update full
  @NonNull
  Foo fooPut(@NonNull @PathVariable final String id, @NonNull @RequestBody final Foo foo)
      throws RequestErrorException;

  // update partial
  @NonNull
  Foo fooPatch(@NonNull @PathVariable final String id, @NonNull @RequestBody final FooPatcher fooPatcher)
      throws RequestErrorException;

  // delete one
  void fooDelete(@NonNull @PathVariable final String id) throws RequestErrorException;

  // delete all
  public void fooDeleteAll(@NonNull final Ids ids) throws RequestErrorException;
}
