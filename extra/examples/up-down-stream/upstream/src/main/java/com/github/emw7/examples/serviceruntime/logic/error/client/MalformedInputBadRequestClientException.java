package com.github.emw7.examples.serviceruntime.logic.error.client;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.service.core.common.request.error.BadRequestClientException;
import java.util.Map;
import org.springframework.lang.NonNull;

public final class MalformedInputBadRequestClientException extends BadRequestClientException {

  private static final Code CODE= new Code("1U7EX");

  @I18nLabel(params={})
  private static final String I18N_LABEL = "app.i18n.error.malformed-input";

  public MalformedInputBadRequestClientException(
      @NonNull final Id id, @NonNull final String malformation) {
    super(null, CODE, id, new Error(I18N_LABEL, Map.of("malformation",malformation)));
  }

}
