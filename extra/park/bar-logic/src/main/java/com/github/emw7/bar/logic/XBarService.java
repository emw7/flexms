package com.github.emw7.bar.logic;

import com.github.emw7.bar.logic.api.BarService;
import com.github.emw7.bar.logic.api.ModelPatcher;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.bar.model.Bar.Severity;
import com.github.emw7.bar.model.Bar.State;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface XBarService {

  // create
  @NonNull
  Bar create(@NonNull final Bar bar)
      throws RequestErrorException;// throws /*BadPropertiesClientException,*/ ServerRequestErrorException;

  // retrieve
  @NonNull
  Bar retrieve(@NonNull final String id) throws NotFoundClientException;

  // list
  record ListFilter(String typeIs, Severity[] severityIn, State stateAtLeast, ZonedDateTime tbefore,
                    ZonedDateTime tafter, ZonedDateTime[] tin) {}

  @NonNull
  List<Bar> list(@NonNull final ListFilter listFilter, @NonNull final Pageable pageable)
      throws ServerRequestErrorException;

  // update
  @NonNull
  Bar update(@NonNull final String id, @NonNull final Bar bar) throws NotFoundClientException, ServerRequestErrorException;

  // patch
  record BarPatcher(boolean resetType, String type,
                    boolean resetDescriptionLabel, String descriptionLabel,
                    boolean resetSeverity, Severity severity,
                    boolean resetState, State state,
                    boolean resetT, ZonedDateTime t)  implements ModelPatcher<Bar> {

    public Bar patch(@NonNull final Bar bar)
    {
      return new Bar(
          bar.id(),
          ( resetType || type != null ) ? type : bar.type(),
          ( resetDescriptionLabel || descriptionLabel != null ) ? descriptionLabel : bar.descriptionLabel(),
          ( resetSeverity || severity != null ) ? severity : bar.severity(),
          ( resetState || state != null ) ? state : bar.state(),
          ( resetT || t != null ) ? t : bar.t());
    }
  }

  @NonNull Bar patch(@NonNull String id, @NonNull final BarService.BarPatcher barPatcher)
      throws NotFoundClientException, ServerRequestErrorException;


  // delete
  void delete(@NonNull String id) throws ServerRequestErrorException;

  void delete(@NonNull Set<String> ids) throws ServerRequestErrorException;

}
