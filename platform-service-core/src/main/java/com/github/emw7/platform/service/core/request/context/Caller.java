package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.service.core.ServiceCoreConstants;
import java.util.Locale;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * REV:V In a request chain who called the current ring.
 */
public final class Caller implements Requester {

  public static class Builder {

    private String tenant;
    private String id;
    private Locale locale;
    private boolean isService;

    public Builder() {
      this.tenant = ServiceCoreConstants.SYSTEM_TENANT;
      this.id = ServiceCoreConstants.SYSTEM_ID;
      this.locale = ServiceCoreConstants.SYSTEM_LOCALE;
      this.isService = ServiceCoreConstants.SYSTEM_IS_SERVICE;
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

  /**
   * Default, likely useless, {@link Caller} that gets:
   * <ul>
   * <li>{@code tenant} from {@link ServiceCoreConstants#SYSTEM_TENANT}</li>
   * <li>{@code id} from {@link ServiceCoreConstants#SYSTEM_ID}</li>
   * <li>{@code locale} from {@link ServiceCoreConstants#SYSTEM_LOCALE}</li>
   * <li>{@code isService} from {@link ServiceCoreConstants#SYSTEM_IS_SERVICE}</li>
   * </ul>
   */
  public static final Caller DEFAULT= new Caller(ServiceCoreConstants.SYSTEM_TENANT,
      ServiceCoreConstants.SYSTEM_ID, ServiceCoreConstants.SYSTEM_LOCALE, ServiceCoreConstants.SYSTEM_IS_SERVICE);

  private final String tenant;
  private final String id;
  private final Locale locale;
  private final boolean isService;

  private Caller(@NonNull final String tenant, @NonNull final String id,
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
