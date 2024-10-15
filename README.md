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

Content: see [Logging](./extra/doc/Logging/README.md)

**STATUS**: da completare  
**Dependencies**: platform-core  
**Autoconfigure**: /

[**TODO**](./platform-log/TODO)

## platform-i18n

A set of functions related to internationalisation.

Content: see [Internationalization](./extra/doc/Internationalization/README.md)

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
