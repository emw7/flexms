package com.github.emw7.platform.i18n;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceSupport;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * A {@link MessageSource} that first try to resolve using a selected message source and if such
 * a resolving fails then delegates to the defined {@link MessageSource} beans stopping at the
 * first that resolves the code.
 * <p>
 * The selected message source it is a {@link MessageSource} bean with name {@code appMessageSource} and
 * if such a bean is not defined then the selected message source is used an internally
 * constructed {@link ReloadableResourceBundleMessageSource} initialized with basename = "messages"
 * and default encoding = "UTF-8"
 * <p>
 * For the sake of the performance, the message source that resolves the code is cached the
 * following resolution of the same code.
 * <p>
 * In case no message source resolves the code, and a default message is provided, it is "resolved"
 * through the internally constructed message source.
 */
public final class CompositeMessageSource extends MessageSourceSupport implements MessageSource {

  //region Private properties
  private final MessageSource appMessageSource;
  private final Map<String, MessageSource> messageSources;
  // code => message source.
  private final ConcurrentMap<String, MessageSource> cache;
  //endregion Private properties

  //region Private static methods
  private static MessageSource buildAppMessageSource ()
  {
    ReloadableResourceBundleMessageSource appMessageSource = new ReloadableResourceBundleMessageSource();
    appMessageSource.setBasename("messages");
    appMessageSource.setDefaultEncoding("UTF-8");
    return appMessageSource;
  }
  //endregion Private static methods

  //region Constructors
//  public CompositeMessageSource(@NonNull final MessageSource appMessageSource, @NonNull final Map<String, MessageSource> messageSources) {
//    this.appMessageSource = appMessageSource;
//    this.messageSources = messageSources;
//    this.cache = new ConcurrentHashMap<>();
//  }

  /**
   * Constructs the object with the defined {@link MessageSource} beans.
   * <p>
   * If in the defined {@link MessageSource} beans there is one which name is {@code appMessageSource}
   * then it is selected as the first one with which try label resolving. If such a bean does not
   * exist, then a default one is created to be used as the selected message source.
   *
   * @param messageSources the map of defined {@link MessageSource} beans
   */
  public CompositeMessageSource(@NonNull final Map<String, MessageSource> messageSources) {
    final MessageSource appMessageSource= messageSources.get("appMessageSource");
    if ( appMessageSource != null ) {
      // FOUND!
      this.appMessageSource= appMessageSource;
      this.messageSources= messageSources.entrySet().stream().filter(e -> !e.getKey().equals("appMessageSource")).collect(
                  Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    else {
      // NOT found!
      this.appMessageSource= buildAppMessageSource();
      this.messageSources= messageSources;
    }

    this.cache = new ConcurrentHashMap<>();
  }
  //endregion Constructors

  //region API

  /**
   * Returns the translation of {@code code} for the specified {@code locale}.
   * <p>
   * Returns {@code null} in case {@code code} is {@code null}<br/>
   * Searches {@code code} using the selected message source first and then in other message sources
   * if not found.<br/>
   * If eventually, the code cannot be found then returns what selected message source returns
   * with the call {@link MessageSource#getMessage(String, Object[], String, Locale)}.
   *
   * @param code code for which translation is wanted
   * @param args actual arguments for message's placeholder
   * @param defaultMessage message to be returned in case translation for {@code code} cannot be
   *                       found
   * @param locale locale for which translation of {@code code} is wanted
   *
   * @return the translation of the specified {@code code} or {@code defaultMessage} is such a code
   * translation cannot be found.
   */
  @Override
  public @Nullable String getMessage(@Nullable String code, @Nullable Object[] args,
      @Nullable String defaultMessage, @Nullable Locale locale) {
    try {
      return getMessage(code, args, locale);
    } catch (NoSuchMessageException e) {
      // return the default message if not found.
      if (code == null) {
        return null;
      } else {
        return appMessageSource.getMessage(code, args, defaultMessage,
            Optional.ofNullable(locale).orElse(Locale.getDefault()));
      }
    }
  }

  /**
   * Returns the translation of {@code code} for the specified {@code locale}.
   * <p>
   * Returns {@code null} in case {@code code} is {@code null}<br/>
   * Searches {@code code} using the selected message source first and then in other message sources
   * if not found.<br/>
   * If eventually, the code cannot be found then throws {@link NoSuchMessageException}.
   *
   * @param code code for which translation is wanted
   * @param args actual argument for message's placeholder
   * @param locale locale for which translation of {@code code} is wanted
   *
   * @return the translation of the specified {@code code} or {@code defaultMessage} is such a code
   * translation cannot be found.

   * @throws NoSuchMessageException if the code cannot be found
   */
  @Override
  public @NonNull String getMessage(@Nullable String code, @Nullable Object[] args,
      @Nullable Locale locale) throws NoSuchMessageException {

    return Optional.ofNullable(getMessageInternal(code, args, locale))
        .orElseThrow(() -> new NoSuchMessageException(code, locale));
  }

  @Override
  public @NonNull String getMessage(@NonNull final MessageSourceResolvable resolvable,
      @Nullable final Locale locale) throws NoSuchMessageException {

    String[] codes = resolvable.getCodes();
    if (codes != null) {
      for (final String code : codes) {
        String message = this.getMessageInternal(code, resolvable.getArguments(), locale);
        if (message != null) {
          return message;
        }
      }
    }

    return appMessageSource.getMessage(resolvable,
        Optional.ofNullable(locale).orElse(Locale.getDefault()));
  }
  //endregion API

  //region Private methods.
  @Nullable
  private String getMessageInternal(@Nullable String code, @Nullable Object[] args,
      @Nullable Locale locale) {

    if (code == null) {
      return null;
    }
    // else...

    try {
      return fromCache(code, args, locale);
    } catch (NoSuchMessageException e) {
      // continue...
    }
    // not in cache... let's try with appMessageSource.

    try {
      String translation = appMessageSource.getMessage(code, args,
          Optional.ofNullable(locale).orElse(Locale.getDefault()));
      cache.put(code, appMessageSource);
      return translation;
    } catch (NoSuchMessageException e) {
      // continue...
    }

    // not in appMessageSource... let's try with message sources.

    // there is not an order... the first that has the translation is used... moreover, only one
    //  should have the translation
    for (MessageSource messageSource : messageSources.values()) {
      try {
        String translation = messageSource.getMessage(code, args,
            Optional.ofNullable(locale).orElse(Locale.getDefault()));
        cache.put(code, messageSource);
        return translation;
      } catch (NoSuchMessageException e) {
        // continue to the next message source...
      }
    }

    return null;
  }

  private @NonNull String fromCache(@Nullable String code, @Nullable Object[] args,
      @Nullable Locale locale) throws NoSuchMessageException {
    if (code == null) {
      //noinspection ConstantValue
      throw new NoSuchMessageException(code, locale);
    } else {
      return Optional.ofNullable(cache.get(code))
          .orElseThrow(() -> new NoSuchMessageException(code, locale))
          .getMessage(code, args, Optional.ofNullable(locale).orElse(Locale.getDefault()));
    }
  }
  //endregion Private methods.
}
