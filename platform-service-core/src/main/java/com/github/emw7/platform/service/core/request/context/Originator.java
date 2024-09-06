package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.service.core.PlatformServiceCoreConstants;
import java.util.Locale;
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
      this.tenant = PlatformServiceCoreConstants.SYSTEM_TENANT;
      this.id = PlatformServiceCoreConstants.SYSTEM_ID;
      this.locale = PlatformServiceCoreConstants.SYSTEM_LOCALE;
      this.isService = PlatformServiceCoreConstants.SYSTEM_IS_SERVICE;
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

  public static final Originator DEFAULT = new Originator(PlatformServiceCoreConstants.SYSTEM_TENANT, PlatformServiceCoreConstants.SYSTEM_ID, PlatformServiceCoreConstants.SYSTEM_LOCALE,
      PlatformServiceCoreConstants.SYSTEM_IS_SERVICE);

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
