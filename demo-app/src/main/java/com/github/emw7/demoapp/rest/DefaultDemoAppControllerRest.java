package com.github.emw7.demoapp.rest;

import com.github.emw7.bar.client.BarClient;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.bar.model.Bar.Severity;
import com.github.emw7.bar.model.Bar.State;
import com.github.emw7.platform.app.request.context.RequestContextHolder;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.log.EventLogger;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
public class DefaultDemoAppControllerRest implements DemoAppControllerRest {

  private final Logger log = LoggerFactory.getLogger(DefaultDemoAppControllerRest.class);

  //private final MessageSource messageSource;
 private final Translator translator;

  //private final DemoAppService demoAppService;

  private final BarClient barClient;

  public DefaultDemoAppControllerRest(/*@NonNull final MessageSource messageSource*/
  @NonNull final Translator translator,
      @NonNull final BarClient barClient) {
    //this.messageSource = messageSource;
    this.translator= translator;
    this.barClient= barClient;
  }

  // region API
  // create
  @PostMapping
  public @NonNull Object test(@NonNull @RequestBody final Object o) throws RequestErrorException {
//    final Locale locale = RequestContextHolder.get().caller().locale();
//
//    final String y= translator.translate(locale, "com.github.emw7.platform.request.server-error",null);
//    final String z= translator.translate(locale, "com.github.emw7.platform.unknown-dependency-error",null);
//
//    final String message = translator.translate(locale, "app.message.test", Map.of("testName", 1 ));
//    EventLogger.notice(log).pattern("here we go: {}").params(message).log();
//    final String messagex = translator.translate(locale, "app.message.x",null);
//    EventLogger.notice(log).pattern("x: {}").params(messagex).log();


    Object oo= barClient.create(new Bar(null, "123", "x.y.z", Severity.NONE, State.END, ZonedDateTime.now()));
    return oo;
  }
  //endregion API

  //region Private methods
//  @NonNull
//  String translate(@NonNull final Locale locale, @NonNull final String code,
//      @Nullable final Map<String, Object> params) {
//    String translatedMessage = messageSource.getMessage(code, null, locale);
//    if (!(params == null) && !params.isEmpty()) {
//      StringSubstitutor sub = new StringSubstitutor(params, "{", "}");
//      sub.setEscapeChar('\\');
//      translatedMessage = sub.replace(translatedMessage);
//    }
//    return translatedMessage;
//    //endregion Private methods
//  }
}
