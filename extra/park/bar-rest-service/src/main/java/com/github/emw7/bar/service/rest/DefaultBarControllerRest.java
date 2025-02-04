package com.github.emw7.bar.service.rest;

import com.github.emw7.bar.logic.api.ApiDefinition;
import com.github.emw7.bar.logic.api.ApiDefinition.Create;
import com.github.emw7.bar.logic.api.ApiDefinition.Retrieve;
import com.github.emw7.bar.logic.api.BarService;
import com.github.emw7.bar.logic.api.BarService.BarPatcher;
import com.github.emw7.bar.logic.api.BarService.ListFilter;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.bar.model.Bar.Severity;
import com.github.emw7.bar.model.Bar.State;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.time.ZonedDateTime;
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
@RequestMapping("/v1")
public class DefaultBarControllerRest implements
    BarControllerRest {

  private final BarService barService;

  public DefaultBarControllerRest(final BarService barService) {
    this.barService = barService;
  }

  // region API
  // create
  // TODO throws samethins more meaningful.
  @PostMapping(Create.endpoint)
  public @NonNull Bar barPost(@NonNull @RequestBody final Bar bar) throws RequestErrorException {
    System.out.printf("[EMW7] %s; bar: %s%n", "barPost", bar);
    return barService.create(bar);
  }

  // retrieve
  @GetMapping(Retrieve.endpoint)
  public @NonNull Bar barGet(@RequestHeader @NonNull final HttpHeaders httpHeaders,
      @NonNull final @PathVariable String id) throws NotFoundClientException {
    //System.out.printf("[EMW7] %s; id: %s%n", "barGet", uuid);

    //System.out.printf("trace %s, span %s%n", TracingContainer.traceId(), TracingContainer.spanId());

    //System.out.printf("Caller: %s%n", RequestContextHolder.get().caller());
    //System.out.printf("Originator: %s%n", RequestContextHolder.get().originator());

    //System.out.println(new DefaultValueStyler().style(Map.of("name", "enrico", "age", 47)));
    return barService.retrieve(id);
  }

  // list
  // Mah... per fare list con filtri... non si può usare Bar... quindi si deve, come faceva il business
  //  service usare una classe filtro che "wrappa" il modello... altrimenti si usano i @RequestParam
  //  e poi si mappano in una classe... per cui a questo punto conviene usare la classe... che però
  //  forse NON può essere record o @Value... ma deve avere un costruttore per ogni property e quindi
  //  getter e setter... prove da fare... forse con lombok... che però vorrei evitare!

  //  class BarFilter {
//    Integer imin;
//    Integer imax;
//    Integer iequals;
//
//    String[] names;
//
//    String id;
//  }
  @GetMapping(ApiDefinition.List.endpoint)
  public @NonNull PagedModel<Bar> barList(
      @Nullable @RequestParam(required = false) String typeIs,
      @Nullable @RequestParam(required = false) Severity[] severityIn,
      @Nullable @RequestParam(required = false) State stateAtLeast,
      @Nullable @RequestParam(required = false) ZonedDateTime tbefore,
      @Nullable @RequestParam(required = false) ZonedDateTime tafter, ZonedDateTime[] tin,
      @NonNull @SortDefault(sort = "severity", direction = Direction.DESC)
      @SortDefault(sort = "severity", direction = Direction.DESC) final Pageable pageable)
      throws ServerRequestErrorException {
    final List<Bar> bars = barService.list(
        new ListFilter(typeIs, severityIn, stateAtLeast, tbefore, tafter, tin),
        pageable);
    return new PagedModel<>(new PageImpl<>(bars, pageable, bars.size()));
  }

  // update full
  @PutMapping("/{id}")
  public @NonNull Bar barPut(@NonNull @PathVariable final String id,
      @NonNull @RequestBody final Bar bar)
      throws NotFoundClientException, ServerRequestErrorException {
    return barService.update(id, bar);
  }

  // update partial
  @PatchMapping("/{id}")
  public @NonNull Bar barPatch(@NonNull @PathVariable final String id,
      @NonNull @RequestBody final BarPatcher barPatcher)
      throws NotFoundClientException, ServerRequestErrorException {
    return barService.patch(id, barPatcher);
  }

  // delete one
  @DeleteMapping("/{id}")
  public void barDelete(@NonNull @PathVariable String id) throws ServerRequestErrorException {
    barService.delete(id);
  }

  // delete all
  @PostMapping("/op:delete-all")
  public void barDeleteAll(@NonNull @RequestBody Ids ids) throws ServerRequestErrorException {
    barService.delete(ids.ids());
  }
  //endregion API

}
