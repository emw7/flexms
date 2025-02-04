package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.service.core.ServiceCoreConstants;
import java.util.Locale;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * REV:V In a request chain who called the first ring.
 *
 * @see Requester
 */
public final class Originator implements Requester {

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

    public Builder(@NonNull final Caller caller) {
      this.tenant = caller.tenant();
      this.id = caller.id();
      this.locale = caller.locale();
      this.isService = caller.isService();
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

  /**
   * Default, likely useless, {@link Originator} that gets:
   * <ul>
   * <li>{@code tenant} from {@link ServiceCoreConstants#SYSTEM_TENANT}</li>
   * <li>{@code id} from {@link ServiceCoreConstants#SYSTEM_ID}</li>
   * <li>{@code locale} from {@link ServiceCoreConstants#SYSTEM_LOCALE}</li>
   * <li>{@code isService} from {@link ServiceCoreConstants#SYSTEM_IS_SERVICE}</li>
   * </ul>
   */
  public static final Originator DEFAULT = new Originator(ServiceCoreConstants.SYSTEM_TENANT, ServiceCoreConstants.SYSTEM_ID, ServiceCoreConstants.SYSTEM_LOCALE,
      ServiceCoreConstants.SYSTEM_IS_SERVICE);

  private final String tenant;
  private final String id;
  private final Locale locale;
  private final boolean isService;

  private Originator(@NonNull final String tenant, @NonNull final String id,
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

  @NonNull
  public String toString() {
    return (new ToStringBuilder(this)).append("tenant", this.tenant()).append("id", this.id()).append("locale", this.locale()).append("isService", this.isService()).toString();
  }

}
