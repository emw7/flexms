package com.github.emw7.platform.service.core.request.context;

import io.micrometer.tracing.Span;
import java.util.Locale;
import org.springframework.lang.NonNull;

/**
 * REV:V The request context information.
 * <p>
 * The {@link #originator()} is who started the call chains.<br/>
 * The {@link #caller()} is who started the current request.<br/>
 * Let's say that for completing operation a provided by service A, this service needs to use
 * operation b provided by service B. Then entity X requests service A to do operation a: entity X
 * is the originator. Then service A requests service B to do operation b: in service B, entity X
 * is the originator and service A is the caller.</br>
 * In service A {@code originator} and {@code caller} matches.
 * <p>
 * The {@link #locale()} is the locale valid for this request, how it is set depends on the
 * implementation. Note that {@code originator} and {@code caller} have its own locale and there is
 * no need that the locale of the request context matches one or the other. Ideally, the request
 * context locale is a mean to forse application to consistently use a locale.
 */
public sealed interface RequestContext permits DefaultRequestContext {

  @NonNull
  Locale locale();

  @NonNull
  Originator originator();

  @NonNull
  Caller caller();
}
