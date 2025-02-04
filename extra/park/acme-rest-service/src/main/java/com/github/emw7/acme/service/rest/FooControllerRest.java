package com.github.emw7.acme.service.rest;

import com.github.emw7.acme.model.Acme;
import com.github.emw7.platform.error.RequestErrorException;
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

  // create
  @NonNull
  Acme fooPost(@NonNull @RequestBody final Acme foo) throws RequestErrorException;

  // retrieve
  @NonNull
  Acme fooGet(@RequestHeader @NonNull final HttpHeaders httpHeaders,
      @NonNull final @PathVariable String uuid) throws RequestErrorException;

  // list
  @NonNull
  PagedModel<Acme> fooList(@Nullable @RequestParam(required = false) String name,
      @Nullable @RequestParam(required = false) Integer imin,
      @Nullable @RequestParam(required = false) Integer imax,
      @Nullable @RequestParam(required = false) Integer iequals,
      @Nullable final Pageable pageable) throws RequestErrorException;

  // update
  @NonNull
  Acme fooPut(@NonNull @PathVariable String uuid, @NonNull @RequestBody final Acme foo)
      throws RequestErrorException;

  // delete
  void fooDelete(@NonNull @PathVariable String uuid) throws RequestErrorException;

}
