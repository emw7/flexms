package com.github.emw7.platform.app.request.context;

import static com.github.emw7.platform.app.PlatformAppConstants.*;

import com.github.emw7.platform.log.EventLogger;
import java.util.IllformedLocaleException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The {@link Requestor} that does the request.
 */
public final class Originator implements Requestor {

  public static class Builder {

    private String tenant;
    private String id;
    private Locale locale;
    private boolean isService;

    public Builder() {
      this.tenant = SYSTEM_TENANT;
      this.id = SYSTEM_ID;
      this.locale = SYSTEM_LOCALE;
      this.isService = SYSTEM_IS_SERVICE;
    }

    public Builder tenant(@Nullable final String v) {
      if (v != null) {
        this.tenant = v;
      }
      return this;
    }

    public Builder id(@Nullable final String v) {
      if (v != null) {
        this.id = v;
      }
      return this;
    }

    public Builder locale(@Nullable final Locale v) {
      if (v != null) {
        this.locale = v;
      }
      return this;
    }

    public Builder isService(@Nullable final boolean v) {
      this.isService = v;
      return this;
    }

    public Originator build() {
      return new Originator(tenant, id, locale, isService);
    }
  }

  public static final Originator DEFAULT = new Originator(SYSTEM_TENANT, SYSTEM_ID, SYSTEM_LOCALE,
      SYSTEM_IS_SERVICE);

  private final String tenant;
  private final String id;
  private final Locale locale;
  private final boolean isService;

  public Originator(@NonNull final String tenant, @NonNull final String id,
      @NonNull final Locale locale, final boolean isService) {
    this.tenant = tenant;
    this.id = id;
    this.locale = locale;
    this.isService = isService;
  }


  @Override
  public @NonNull String tenant() {
    return tenant;
  }


  @Override
  public @NonNull String id() {
    return id;
  }


  @Override
  public @NonNull Locale locale() {
    return locale;
  }

  @Override
  public boolean isService() {
    return isService;
  }

}
