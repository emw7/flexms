package com.github.emw7.bar.logic.api;

import com.github.emw7.bar.logic.api.error.BarNotFoundException;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.bar.model.Bar.Severity;
import com.github.emw7.platform.core.map.MapUtil;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ResourceIdClientException;
import com.github.emw7.platform.error.category.NotFound;
import com.github.emw7.platform.service.core.error.model.RequestErrorResponse;
import java.lang.annotation.Annotation;
import java.net.http.HttpHeaders;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.management.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.SortDefault;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

// TODO da rivedere che requestBodyClazz, pathParameters e queryParameters mi sembrano tanto cose
//  da REST e *NON* generiche.
public final class ApiDefinition {

  public enum Semantic {
    POST, GET, PUT, PATCH, DELETE
  }

  // create
  public static final class Create {

    private Create() {
    }

    public static final Semantic semantic = Semantic.POST;
    // TODO must be "/bar".
    public static final String endpoint = "/bar";
    public static final Class<Bar> requestBodyClazz = Bar.class;
    public static final java.util.List<String> pathParameters = java.util.List.of();
    public static final java.util.List<String> queryParameters = java.util.List.of();

    // TODO toglierlo da qua e metterlo nella roba del clie nt (generica, NON solo rest)?
    public static final Map<Class<? extends Annotation>, Function<RequestErrorResponse, ? extends RequestErrorException>> requestErrorResponseToExceptionMappers =
        Map.of(
            NotFound.class,
            rer -> new BarNotFoundException(
                new Id("00000"),
                rer.errors().getFirst().params().get(ResourceIdClientException.RESOURCE_ID_KEY),
                MapUtil.remove(rer.errors().getFirst().params(), ResourceIdClientException.RESOURCE_ID_KEY)
            )
        );
  }

  // retrieve
  public static final class Retrieve {

    private Retrieve() {
    }

    public static final Semantic semantic = Semantic.GET;
    public static final String endpoint = "/bar/{id}";
    public static final Class<Bar> requestBodyClazz = null;
    public static final java.util.List<String> pathVariables = java.util.List.of("id");
    public static final java.util.List<String> queryParameters = java.util.List.of();
  }

  // list
  public static final class List {

    private List() {
    }

    public static final Semantic semantic = Semantic.GET;
    public static final String endpoint = "/bar";
    public static final Class<Bar> requestBodyClazz = null;
    public static final java.util.List<String> pathVariables = java.util.List.of();
    public static final java.util.List<String> queryParameters = java.util.List.of(
        "typeIs", "severityIn", "stateAtLeast", "tbefore", "tafter", "tin");
    public static final Sort sort= Sort.by(Order.desc("severity"));
    public static final Pageable pageable= Pageable.ofSize(25);
  }

//  @NonNull
//  PagedModel<Bar> barList(
//      @Nullable @RequestParam(required = false) String typeIs,
//      @Nullable @RequestParam(required = false) Severity[] severityIn,
//      @Nullable @RequestParam(required = false) Bar.State stateAtLeast,
//      @Nullable @RequestParam(required = false) ZonedDateTime tbefore,
//      @Nullable @RequestParam(required = false) ZonedDateTime tafter, ZonedDateTime[] tin,
//      @NonNull @SortDefault(sort = "severity", direction = Direction.DESC) final Pageable pageable) throws RequestErrorException;
//
//  // update full
//  @NonNull
//  Bar barPut(@NonNull @PathVariable final String id, @NonNull @RequestBody final Bar bar)
//      throws RequestErrorException;
//
//  // update partial
//  @NonNull
//  Bar barPatch(@NonNull @PathVariable final String id, @NonNull @RequestBody final BarService.BarPatcher barPatcher)
//      throws RequestErrorException;
//
//  // delete one
//  void barDelete(@NonNull @PathVariable final String id) throws RequestErrorException;
//
//  // delete all
//  public void barDeleteAll(@NonNull final Ids ids) throws RequestErrorException;
}
