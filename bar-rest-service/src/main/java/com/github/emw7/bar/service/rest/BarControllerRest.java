package com.github.emw7.bar.service.rest;

import com.github.emw7.bar.model.Bar;
import com.github.emw7.bar.logic.api.BarService.BarPatcher;
import com.github.emw7.bar.model.Bar.Severity;
import com.github.emw7.bar.model.Bar.State;
import com.github.emw7.platform.error.RequestErrorException;
import java.time.ZonedDateTime;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface BarControllerRest {

  record Ids (@NonNull Set<String> ids) {}

  // create
  @NonNull
  Bar barPost(@NonNull @RequestBody final Bar bar) throws RequestErrorException;

  // retrieve
  @NonNull
  Bar barGet(@RequestHeader @NonNull final HttpHeaders httpHeaders,
      @NonNull @PathVariable final String id) throws RequestErrorException;

  // list
  @NonNull
  PagedModel<Bar> barList(
      @Nullable @RequestParam(required = false) String typeIs,
      @Nullable @RequestParam(required = false) Severity[] severityIn,
      @Nullable @RequestParam(required = false) State stateAtLeast,
      @Nullable @RequestParam(required = false) ZonedDateTime tbefore,
      @Nullable @RequestParam(required = false) ZonedDateTime tafter, ZonedDateTime[] tin,
      @NonNull @SortDefault(sort = "severity", direction = Direction.DESC) final Pageable pageable) throws RequestErrorException;

  // update full
  @NonNull
  Bar barPut(@NonNull @PathVariable final String id, @NonNull @RequestBody final Bar bar)
      throws RequestErrorException;

  // update partial
  @NonNull
  Bar barPatch(@NonNull @PathVariable final String id, @NonNull @RequestBody final BarPatcher barPatcher)
      throws RequestErrorException;

  // delete one
  void barDelete(@NonNull @PathVariable final String id) throws RequestErrorException;

  // delete all
  public void barDeleteAll(@NonNull final Ids ids) throws RequestErrorException;
}
