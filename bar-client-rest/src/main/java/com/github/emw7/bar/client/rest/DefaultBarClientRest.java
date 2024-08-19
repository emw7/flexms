package com.github.emw7.bar.client.rest;

import com.github.emw7.bar.client.BarClient;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.bar.model.Bar.Severity;
import com.github.emw7.bar.model.Bar.State;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public class DefaultBarClientRest implements BarClient {

  @Override
  public @NonNull Bar create(@NonNull final Bar bar) throws RequestErrorException {
    final String id = UUID.randomUUID().toString();
    return new Bar(id, bar.type(), bar.descriptionLabel(), bar.severity(), bar.state(),
        ZonedDateTime.now());
  }

  @Override
  public @NonNull Bar retrieve(@NonNull final String id) throws NotFoundClientException {
    return new Bar(id, "a bar", "a.bar", Severity.NONE, State.PUSH, ZonedDateTime.now());
  }

  @NonNull
  @Override
  public List<Bar> list(@NonNull final ListFilter listFilter, @NonNull final Pageable pageable)
      throws ServerRequestErrorException {

    int si = pageable.getPageNumber() * pageable.getPageSize();
    int ee = si + (int) pageable.getPageSize();
    List<Bar> bars = new ArrayList<>(ee-si);
    IntStream.range(si,ee).forEach(i -> { try { bars.add(
        retrieve(String.valueOf(i))); } catch ( Exception e ) {}} );

    return bars;
  }

  /**
   * bar.id() is ignored, that is returned entity has id equals to specified {@code id}.
   *
   * @param id  the id of the entity to be upated
   * @param bar
   * @return
   * @throws NotFoundClientException
   * @throws ServerRequestErrorException
   */
  @Override
  public @NonNull Bar update(@NonNull final String id, @NonNull final Bar bar)
      throws NotFoundClientException, ServerRequestErrorException {
    return new Bar(id, bar.type(), bar.descriptionLabel(), bar.severity(), bar.state(), bar.t());
  }

  @Override
  public @NonNull Bar patch(@NonNull final String id, @NonNull final BarPatcher barPatcher)
      throws NotFoundClientException, ServerRequestErrorException {
    return barPatcher.patch(retrieve(id));
  }

  @Override
  public void delete(@NonNull final String id) throws ServerRequestErrorException {
  }

  @Override
  public void delete(@NonNull final Set<String> ids) throws ServerRequestErrorException {
  }

  //region Private methods
  //endregion Private methods

}
