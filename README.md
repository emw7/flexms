# flexms

A shelf of components for building a microservice flexibly using Spring.

# Module descriptions and relationships

## platform-core

A set of core functions not related to any technology.

Content:

- Array util: common and frequent operations on arrays.
- I18n utils: minimal i18n stuff that could be used and needed by who does not need all the i18n  
  framework implemented in the platform-i18n framework.
- Map utils: common and frequent operations on arrays.
- Mappers: transformations from a type to another type:
    - Boolean: mapping from and to boolean.
    - Map: mapping from and to map.

**STATUS**: GA  
**Dependencies**: /  
**Autoconfigure**: /

[**TODO**](./platform-core/TODO)

## platform-log

A wrapper over slf4j that standardizes logging to a consistent format and uses an event-driven
approach.

Content:

- EventLogger: the class that provided the slf4j wrapper.

**STATUS**: da completare
**Dependencies**: /  
**Autoconfigure**: /

[**TODO**](./platform-log/TODO)

## platform-i18n

A set of functions related to internationalisation.

Content: see [Internationalisation](#internationalisation)

**STATUS**: GA  
**Dependencies**: platform-core  
**Autoconfigure**: see [Internationalisation](#internationalisation)

[**TODO**](./platform-i18n/TODO)

## platform-telemetry

TODO: produce documentation only after the refactoring (  a framework built after [Open Telemetry](https://opentelemetry.io/)
traces concepts)
**STATUS**: to be refactored
**Dependencies**: platform-log  
[**TODO**](./platform-telemetry/TODO)

## platform-error

TODO: produce documentation only after the refactoring
**STATUS**: to be refactored  
**Dependencies**: platform-i18n, platform-telemetry  
[**TODO**](./platform-error/TODO)

## platform-discovery-api

the interface supported by the platform for the service registry, that is supporting the hooks that
allow a service to register its location in a service registry and the hooks allowing other services
to query for the location of services on which depends on  
being this an api, in order to run in an integrated system, the application needs an actual
implementation of such a interface.  
implementations for third party server registry solutions will be facades.  
**STATUS**: misses tests
**Dependencies**: platform-error   
[**TODO**](./platform-discovery-api/TODO)

## platform-auth-api

the interface supported by the platform for authentication and authorization.  
being this an api, in order to run in an integrated system, the application needs an actual
implementation of such a interface.  
implementations for third party server registry solutions will be facades.  
**STATUS**: misses tests
**Dependencies**: /  
[**TODO**](./platform-auth-api/TODO)

## platform-service-core

the core shared by all the [modules of a service](#service-concept-modules)  
**STATUS**: misses tests  
**Dependencies**: platform-error  
[**TODO**](./platform-service-core/TODO)

## platform-service-runtime

the core for the [runtime modules of a service](#service-concept-modules)  
**STATUS**: misses tests  
**Dependencies**: platform-service-core  
[**TODO**](./platform-service-runtime/TODO)

## platform-service-runtime-rest

the core for the runtime of a rest service  
**STATUS**: misses tests  
**Dependencies**: platform-service-runtime  
[**TODO**](./platform-service-runtime-rest/TODO)

## platform-service-client-api

the core for the [client modules of a service](#service-concept-modules)  
**STATUS**: misses tests  
**Dependencies**: platform-service-core  
[**TODO**](./platform-service-client-api/TODO)

## platform-service-client-rest

the core for the client of a service  
**STATUS**: misses tests  
**Dependencies**: platform-service-client-api, platform-protocol-rest  
[**TODO**](./platform-service-client-rest/TODO)

## platform-protocol-api

the api for the protocol projects; a protocol (TODO move in section protocol concept as
done for service concept) is responsible for sending raw requests to and receiving raw responses
from
remote dependency (TODO add section remote dependency concept that explains what remote
dependencies is intended for)  
**Note**: the dependencies need to be reviewed as it only uses model of such a projects  
**STATUS**: misses tests  
**Dependencies**: platform-discovery-api, platform-auth-api  
[**TODO**](./platform-protocol-api/TODO)

## platform-protocol-rest

the rest implementation of the platform-protocol-api  
**STATUS**: misses tests  
**Dependencies**: platform-protocol-api, platform-rest-core  
[**TODO**](./platform-protocol-rest/TODO)

## platform-rest-core

the core shared by all the rest artifacts  
**Note**: Contains only the constants for Originator and Caller
**STATUS**: ready  
**Dependencies**: platform-protocol-api, platform-rest-core  
[**TODO**](./platform-rest-core/TODO)

# Service concept

With service, if not differently stated, it is intended:
> a process that consumes requests and provides responses to such requests

A response can be an error response.

So, normally, that is, if not stated differently, with service it is not intended artefacts of the
service software layer.

<a name="service-concept-modules"></a>
A service provides some common modules:

- The process runtime that consumes requests and provides responses.
- Clients that can be used by other services to make requests and to get responses;  
  clients can be multiple if clients for different programming languages are provided.
- TODO continuare con la lista dopo averla chiarita e mettere un digramma dell'architettura di un
  service  
  e un how to write a service (che sarà la guida per scrivere lo scaffolding)

# Containers

Containers are TODO : add description of containers that is not easy to be written.

# Internationalisation

The `platform-i18n` platform project provides a set of functions related to internationalisation as:
- An easy an a standard way for getting messages in the specified language for a code (example: code => com.github.emw7.i18n.this-is-a-test, language => es, translate(code, language) => esto es una prueba).
- A base class for exception that needs/wants localized message. 

## Goals

The goals to be reached with and that lead to the EMW7 internationalisation implementation are:

1. <a name="internationalisation-goal-1"></a>Platform projects provide their own translation in message sources.
2. <a name="internationalisation-goal-2"></a>Application can override the translation provided by EMW7 platform projects.

## Enable internationalisation in an application

To enable internationalisation, the following two things must be done:

1. Add to the application a dependency on [platform-i18n platform project](#platform-i18n).
2. In the application create a bean of type `CompositeMessageSource` and name it `messageSource` as described below.

Here follow and excerpt of application configuration where a bean of type `CompositeMessageSource` and name `messageSource` is created:  
```java
@Configuration
public class ApplicationConfig {

  @Bean
  public MessageSource messageSource(
      Map<String, MessageSource> messageSources) {
    return new CompositeMessageSource(messageSources);
  }

  // ... other application bean definitions
}
```

**Note**: such a bean should not be used directly by the application, but the application should use `Translator` described below.

Including the `platform-i18n` the following bean are autoconfigured:

- `I18nLabelPrefixes i18nLabelPrefixes`
- `@ConditionalOnBean(MessageSource.class) Translator messageSourceTranslator`
- `@ConditionalOnMissingBean Translator translator`
- `TranslatorContainer translatorContainer` [see Containers](#containers)

The `messageSourceTranslator` is constructed by using the `messageSource` created by the application.

> The `messageSourceTranslator` bean can be injected in the other (application) beans and used to retrieve 
message translations. Objects not managed by Spring (created with `new` for example) can use `TranslatorContainer#getTranslator()` to get the `messageSourceTranslator` bean.  

If the code to be translated is prefixed with EMW7 platform prefix (`com.github.emw7.platform.`, see `I18nLabelPrefixes#getPlatformPrefix`, all messages provided by 
each platform project are prefixed with such a prefix), and if 
`I18nLabelPrefixes#getCustomPrefix` returns a non-empty string, then `MessageSourceTranslator` will 
first attempt to retrieve the translation by searching for the code obtained by replacing that 
prefix with the one returned by `I18nLabelPrefixes#getCustomPrefix`. By default, the value returned 
by `I18nLabelPrefixes#getCustomPrefix` is an empty string and can be customized using the system property `com.github.emw7.platform.i18n.label-custom-prefix`.

![PE8 Componenet diagram](/extra/doc/V1P%20Internationalisation%20-%20PE8%20Componenet%20diagram.png)
*PE8 Components diagram.*

This is better explained with an example:  
Setting `com.github.emw7.platform.i18n.label-custom-prefix=foo.acme.app.` (if trailing dot is missing then it is added by platform), the statement 
`messageSourceTranslator.translate(..., "com.github.emw7.platform.?.test",...)` will first search 
for `foo.acme.app.?.test` and if the search fails, it will then search for 
`com.github.emw7.platform.?.test`. If the application defines a bean of type `MessageSource` and 
name `appMessageSource`, the search is first delegated to this bean; if the application does not 
provide such a bean, an internal message source is used, configured as follows:
```java
    ReloadableResourceBundleMessageSource appMessageSource = new ReloadableResourceBundleMessageSource();
    appMessageSource.setBasename("messages");
    appMessageSource.setDefaultEncoding("UTF-8");
```
In other words, if the application is satisfied with the default, it does not need to define the 
`appMessageSource` bean and can place its messages in resource bundles with the prefix messages. 
However, if the application requires more flexibility (e.g., to search for messages in a database), 
it can define its own appMessageSource. If appMessageSource does not find the message, the search 
is then delegated to the various message sources defined by the different platform projects.  
By setting `com.github.emw7.platform.i18n.label-custom-prefix` [goal #2](#goals) is reached.

![PE8 Componenet diagram](/extra/doc/V1P%20Internationalisation%20-%20IF8%20Sequence%20diagram%20for%20EMW7%20platform%20code.png)
*IF8 Sequence diagram for EMW7 platform code.*

Note that if the code to be translated is not prefixed by the EMW7 platform prefix (i.e., it is an application code) then the search for custom prefix is skipped.

![UEC Sequence diagram for application code](/extra/doc/V1P%20Internationalisation%20-%20UEC%20Sequence%20diagram%20for%20application%20code.png)
*UEC Sequence diagram for application code.*

## Internals

This section is not for end users, but it is for developers or for end users that want to understand how internationalisation has been designed and why internationalisation has been designed in such a way.

- *Why it is the application that must define the bean of type `MessageSource messageSource` and name `messageSource`?*  
  Because for reason (due to bean creation priorities I guess) it seems that if `platform-i18n` defines a bean 
  with such a name, it clashes with the bean that spring creates anyway.
- *How must be named the platform projects resource bundles for the messages?*  
  They must named after the root package of the project with dots (.) replaced by underscores (_) and suffixed by `messages`; with reference to `platform-service-client-api` (maven) platform project, the sources will apper as follow:
  ```
  src/main/resources/
      com_github_emw7_platform_service_client_api_messages.properties
      com_github_emw7_platform_service_client_api_messages_<language-tag>.properties
  ```
- *Why the resource bundles of the platform project are name after project's root package name?*  
  Because naming them after `messages` does not work because of caching of bundles; moreover, naming them after the name of the platform project's root package name gives a sense of ownership and cleanliness.
- *Please give an example on how message source of a platofrm project must be defined*.  
  In the followind code excerpt (based again on `platform-service-client-api` platform project) note the `platformServiceClientApiMessageSource` bean name that 
  is obtained from the project name by removing the dashes (-) and applying the camel case and adding the
  `MessageSource` suffix. 
  ```java
  @AutoConfiguration
  public class PlatformServiceClientApiAutoConfig {

    @Bean
    public MessageSource platformServiceClientApiMessageSource () {
      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
      messageSource.setBasename("com_github_emw7_platform_service_client_api_messages");
      messageSource.setDefaultEncoding("UTF-8");
      return messageSource;
    }
  }
  ```
  The message source implementation is a `ResourceBundleMessageSource` and it is not a
  `ReloadableResourceBundleMessageSource` because it is not needed reloading the messages are they
  are statically included with the EMW7 platform project and that is not updatable.
- *How is [goal #1](#goals) reached?*  
  It is reached with `CompositeMessageSource` that collects all message sources bean that are defined in the application 
  and that delegates message retrieval to them and treating in a special mean the `appMessageSource` (see [above](#enable-internationalisation-in-an-application) for more information).
- *How must be named the code in the platform prooject messages file?*  
  They must be prefixed by `I18nLabelPrefixes#getPlatformPrefix` followed by platform project root package; example:  
  <pre>
  com.github.emw7.platform.service.client.api.greetings.hello-world
   .........................
   platform prefix          ...................
                       project root package    .....................
                                               code specific
  </pre>

## Messages parametrization

It is possible to parametrize messages by adding placeholders between braces.  
For example, the message 
`calling {serviceName}@{serviceVersion} service {caller} encountered an unknown error: {cause}` 
contains four placeholders (`{serviceName}`, `{serviceVersion}`, {caller} and {cause}) that will 
be replaced with actual values when translated.

## Internationalised exception

The `platform-i18n` platform project exposes `I18nEnabledException` exception that acts as base for checked exception that needs/wants the message got from {@link Translator}.  
Refer to `I18nEnabledException` javadoc for more information.

The same is supported for runtime exceptions; refer to `I18nEnabledRuntimeException` javadoc for more information.

# Project structure

## Project name

The project name must follow these rules:
- Must be prefixed by `platform-` and followed by key words that describes the project it self.  
For example the platform project that provides the **api** for a **service** **client** will be named `platform-service-client-api`.
- Must terminate with `-api` if it provides nothing usefull to be used by the application but it is the foundations after on which other project can be constructed; it can be thoughs as an interface and the projects built on it as classes that implements such an interface; 
an example is the `platform-auth-api` project that does not implements anything actually; the application and other platform projects can depends on it but then an actual implementation is needed; actual implementations that could be: `platform-auth-keycloak`, `platform-auth-oath2`, `platform-auth-foo`, ...; TODO far vedere che devono mettere a disposizione property per scegliere una o l'altra implementazione (esclusività) oppure nome dei bean in modo che a può usare -keycloak e b può usare -foo, per esempio.
- TODO continuare con la struttura dei servizi: -rest, -logic, -model...


## Project root package

Project root package is an important concept as other concepts are based on it.  
It can be explained with an example.  
The project root package for a package which name is `platform-acme-foo-bar` is: `com.github.emw7.platform.acme.foo.bar`.

# Notes
