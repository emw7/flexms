package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.service.core.PlatformServiceCoreConstants;
import java.util.Locale;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The {@link Requestor} that originates the request, that is who spontaneously starts the flow.
 */
public final class Caller implements Requestor {

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

    public Caller.Builder tenant(@Nullable final String v) {
      if (v != null) {
        this.tenant = v;
      }
      return this;
    }

    public Caller.Builder id(@Nullable final String v) {
      if (v != null) {
        this.id = v;
      }
      return this;
    }

    public Caller.Builder locale(@Nullable final Locale v) {
      if (v != null) {
        this.locale = v;
      }
      return this;
    }

    public Caller.Builder isService(@Nullable final boolean v) {
      this.isService = v;
      return this;
    }

    public Caller build() {
      return new Caller(tenant, id, locale, isService);
    }
  }

  public static final Caller DEFAULT= new Caller(PlatformServiceCoreConstants.SYSTEM_TENANT,
      PlatformServiceCoreConstants.SYSTEM_ID, PlatformServiceCoreConstants.SYSTEM_LOCALE, PlatformServiceCoreConstants.SYSTEM_IS_SERVICE);

  private final String tenant;
  private final String id;
  private final Locale locale;
  private final boolean isService;

  public Caller(@NonNull final String tenant, @NonNull final String id,
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


  @Override
  public @NonNull String toString () {
    return new ToStringBuilder(this).append("tenant",tenant()).append("id",id())
        .append("locale",locale()).append("isService",isService()).toString();


  }
}
