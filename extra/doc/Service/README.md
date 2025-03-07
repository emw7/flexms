# Service

EMW7 platform provides support for implementing services that exposes API exploiting different technologies (REST, gRPC, GraphQL, ...).

With service, if not differently stated, it is intended:
> a process that consumes requests and provides responses to such requests;  
> a response can be an error response.

So, if not stated differently, with service it is not intended artifacts of the service software layer.

## Service modules

A service is composed by some common modules:

- The process runtime that consumes requests and provides responses.
- Clients that can be used by other services to make requests and to get responses;  
  clients can be multiple if clients for different programming languages are provided.
- TODO continuare con la lista dopo averla chiarita e mettere un digramma dell'architettura di un
  service  
  e un how to write a service (che sarà la guida per scrivere lo scaffolding)

## API

Con API ci si può riferire:
- All'insieme di operazioni che espone un servizio
- Un'operazione in particolare.

Esempio come insieme
- Create sensor
- Update sensor
- Retrieve sensor
- Delete sensor
- List sensors
- Read sensor
- Write sensor

Esempio come operazioni in particolare:
- Update sensor

# ?

Creare un progetto `common` che dipende da `service-core`.  
In questo progetto devono essere inclusi:
- I modelli; ad esempio la classe `Sensor`.
- Gli errori; ad esempio l'errore `SensorAlreadyExistsClientException` e l'errore `SystemErrorServerException`.

Creare un progetto `api` che dipende da `common` e che deve includere:
- L'interfaccia del controller; si veda, ad esempio, l'interfaccia `SensorController`.

In particolare l'interfaccia deve definire:
- Il payload della richiesta.
- Il payload della risposta.
- L'entry-point dell'API.

Di seguito un esempio:
```java
/**
 * The controller interface that defines the API request and response payloads and the available
 * endpoints. It is technology-agnostic.
 * <p>
 * The response classes bring both correct answer and error answer. This is a design solution of
 * this example. In the README.md of the project there is an alternative implementation.
 */
public interface SensorController {

  //region Create
  record CreateSensorRequest(@NonNull Sensor sensor) {

  }

  record CreateSensorResponse(@Nullable Sensor sensor) {

  }


  @NonNull
  CreateSensorResponse create(@NonNull final CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, SystemErrorServerException;
  //endregion Create

  // ...

}
```

<a name="project-logic"></a>
Creare un progetto `logic` che dipende da `common`.  
In questo progetto vanno definiti i bean che implementano le operazioni definite dall'API. Quindi l'implementazione dell'API (REST, gRPC, GraphQL, ...) dipenderà da questo progetto a cui delegherà la reale esecuzione delle operazioni.  
Questo progetto deve anche definire l'autoconfigurazione per far sì che vengano creati tutti i bean che sono stati definiti. Tali bean saranno poi utilizzati da chi implementa l'API (appunto per delegare l'esecuzione l'effetiva esecuzione delle operazioni).

Creare il progetto che implementa l'API. Ad esempio `service-rest` implementa la versione REST dell'API. Tale progetto dovrà dipendere dal progetto di EMW7 platform che offre il supporto alla tecnologia con cui è implementata l'API. Ad esempio `service-rest` dovrà dipendere da `service-runtime-rest`. Inoltre, per quanto detto sopra tale progetto dovrà dipendere da `api` `logic`.

TODO vale la pena fare service agnostic che dipende da `api` e `logic` e implementa le api in modo agnostico dalla tecnologia e poi `service-rest` implementa in dipenda da tencologia (cookie, header, query params, path arguments) e delega a agnostic?

La dipendenza da `service-runtime-<tecnology>` abilita alcuni bean tramite le sue dipendenze (anche transitive):

- Da `service-runtime`:
  - `@Bean com.github.emw7.platform.service.runtime.event.AppReadyEventListener applicationReadyEventListener`

- Da `service-core`:
  - /

- Da `i18n`: 
  - Si veda [Internationalization](../Internationalization/README.md)

Inoltre EMW7 platform service fa sì che sia messo a disposizione un contenitore (`com.github.emw7.platform.service.core.request.context.RequestContextHolder`) che contiene una istanza di `com.github.emw7.platform.service.core.request.context.RequestContextRetriever` con le informazioni della richiesta. Come vengono recuperate queste informazioni nello specifico dipende dalla tecnologia e quindi si rimanda alle sezioni specifiche per i dettagli. L'implementazione della tecnologia si appoggia però su una struttura ben definita messa a disposizione da EMW7 platform che viene [descritta di seguito](#recupero-informazioni-della-richiesta).

## service-runtime-rest

Qui sono elencati i bean che definisce `service-runtime-rest` e viene descritto come vengono recuperate le informazioni della richiesta messe nel contesto.

### Bean definiti:

Gli exception handler per gli errori client e server:
- @Bean com.github.emw7.platform.service.runtime.rest.error.ClientExceptionHandler clientExceptionHandler.
- @Bean com.github.emw7.platform.service.runtime.rest.error.ServerExceptionHandler serverExceptionHandler.

Un locale resolver: @Bean LocaleResolver(com.github.emw7.platform.service.runtime.rest.request.RequestCallerLocaleResolver) localeResolver.

### Recupero informazioni della richiesta per la tecnologia REST

`service-runtime-rest` autoconfigura un bean di tipo `com.github.emw7.platform.service.runtime.rest.aop.RestControllerAspect` che è un aspetto che interviene prima di ogni metodo di una classe annotata come `@RestController`.  
Per personalizzare il recupero, il servizio, deve creare o creare un bean di tipo 
`com.github.emw7.platform.service.runtime.rest.request.context.RestRequestContextRetriever` o un bean da cui dipende quest'ultimo componente. Si veda la documentazione del codice sorgente per maggiori dettagli e il diagramma alla fine di questa parte.  
Non è possibile personalizzare il recupero del locale della richiesta, che viene forzato ad essere il locale di default.

#### Implementazione di default

L'implementazione di default recupera le informazioni del contesto della richiesta dagli header http. Tali header sono definiti in `com.github.emw7.platform.rest.core.PlatformRestConstants`.

![SQ9 EMW7 platform service request context rest](SQ9%20EMW7%20platform%20service%20request%20context%20rest.png)

# Recupero informazioni della richiesta

Ogni tecnologia attiva e/o implementa il recupero delle informazioni della richiesta utilizzando una struttura ben definita messa a disposizione da EMW7 platform. Pur dando una implementazione di default completamente funzionanante, ogni implementazione permette un certo grado di personalizzazione di come tali informazioni vengono recuperate. Per i dettagli su come la tecnologia attiva e implementa il recupero delle informazioni della richiesta si faccia riferimento alle varie implementazioni (progetti `service-runtime-<technology>`).

La struttura message a disposizione da EMW7 platform è definita in `service-core` [TODO{magari tutta o in parte andrà spostata in service-core-runtime}] ed è mostrata nella figura seguente:
![SQ9 EMW7 platform service request context](./SQ9%20EMW7%20platform%20service%20request%20context.png)


Il recupero delle informazioni è personalizzabile dal servizio: per personalizzarlo deve creare alcuni bean.  
`service-runtime-rest` mette a disposizione una implementazione di default che le recupera dagli header della richiesta http.  
`service-runtime-rest` crea un bean di tipo `com.github.emw7.platform.service.runtime.rest.aop.RestControllerAspect` che è un aspetto che interviene prima di ogni metodo di una classe annotata come `@RestController`.  
Per personalizzare il recupero, il servizio, deve creare o creare un bean di tipo 
`com.github.emw7.platform.service.runtime.rest.request.context.RestRequestContextRetriever` o un bean da cui dipende quest'ultimo componente. Si veda la documentazione del codice sorgente per maggiori dettagli.  
Non è possibile personalizzare il recupero del locale della richiesta, che viene forzato ad essere il locale di default.


# Concetti

<a href="concepts-originator"></a>
> `Orginator` il primo anello della catena di chiamate di API.

<a href="concepts-caller"></a>
> `Caller` colui che invoca l'API; coincide con l'orginator se è il primo anello della catena; gli anelli successivi (solitamente) sono altri servizi (di backend).

