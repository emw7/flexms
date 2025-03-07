package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.service.core.common.request.error.ErrorResponseToExceptionMapper.CannotMapException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;

public final class CompositeErrorResponseToExceptionMapper implements ErrorResponseToExceptionMapper<RequestErrorException>{

  public static final class Builder {
    // 5 because... what's the probability a method throws more than 5 exception?
    private final List<ErrorResponseToExceptionMapper<?>> mappers;

    private Builder () {
      this.mappers= new ArrayList<>(5);
    }

    public final Builder add (ErrorResponseToExceptionMapper<?> mapper) {
      mappers.add(mapper);
      return this;
    }

    public CompositeErrorResponseToExceptionMapper build () {
      return new CompositeErrorResponseToExceptionMapper(mappers);
    }
  }

  public static Builder builder () {
    return new Builder();
  }

  private final List<ErrorResponseToExceptionMapper<?>> mappers;

  public CompositeErrorResponseToExceptionMapper(
      @NonNull final ErrorResponseToExceptionMapper<?> mapper, @NonNull final ErrorResponseToExceptionMapper<?>... mappers) {
    this.mappers = Arrays.stream(mappers).collect( () -> { final List<ErrorResponseToExceptionMapper<?>> l= new ArrayList<>(mappers.length+1); l.add(mapper); return l;},
        List::add, List::addAll);
  }

  private CompositeErrorResponseToExceptionMapper(@NonNull final List<ErrorResponseToExceptionMapper<?>> mappers) {
    this.mappers= mappers;
  }

  /**
   * If mapper returns {@code null} then proceed with the next mapper, if mapper throws {@link com.github.emw7.platform.service.core.common.request.error.ErrorResponseToExceptionMapper.CannotMapException}
   * then returns {@code null} else return mapper result. If all mappers return {@code null} then
   * return {@code null}.
   *
   * @param requestErrorResponse the request error response to be mapped to request error exception.
   * @return see method description
   */
  @Override
  public @NonNull RequestErrorException map(final RequestErrorResponse requestErrorResponse) throws CannotMapException {
   for ( ErrorResponseToExceptionMapper<?> mapper: mappers ) {
     try {
       return mapper.map(requestErrorResponse);
     } catch (CannotMapException ignored) {}
   }
   // TODO I18N
   throw new CannotMapException(
       String.format("None of the defined mappers can map request error response %s to exception",requestErrorResponse));
  }
}
