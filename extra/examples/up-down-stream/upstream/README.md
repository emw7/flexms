# Platform :: Examples :: ex-platform-service-runtime

Questo esempio mostra come utilizzare `platform-service-runtime` per scrivere un applicazione
basata su EMW7 platform.

In realtà il progetto `platform-service-runtime` non è pensato per essere utilizzato direttamente da
un'applicazione, ma per funzionare come base per implementazioni più specifiche, come ad esempio:
- `platform-service-runtime-rest`: per servizi che accettano richieste sotto forma di messaggi REST.
- `platform-service-runtime-grpc`: per servizi che accettano richieste sotto forma di messaggi GRPC.
- `platform-service-runtime-jms`: per servizi che accettano richieste sotto forma di messaggi JMS.

Quindi questo esempio mostra, indirettamente, come implementare un progetto specifico per un servizio,
come i 3 elencati sopra.
Inoltre, questo esempio mostra alcuni concetti fondamentali di una applicazione scritta
utilizzando EMW7 platform.  
Il fatto che venga implementata un'applicazione direttamente sopra di esso, non è molto significativo.

Il progetto dipende dal progetto `platform-service-core` che a sua volta dipende dal progetto
`platform-error`.

Il primo, che è WORK-IN-PROGRESS [WIP], mette a disposizione le parti comuni ai progetti di
service-runtime (la parte server, TODO[rinominare in service server?]) e service-client.
In questo momento il progetto contiene (potrebbe contenere) parti non comuni a entrambi i progetti,
ma di pertinenza o solo di un uno o solo dell'altro (WIP[creare 2 progetti: core-runtime,
core-client]).

Il secondo, anch'esso in WIP, serve da fondamenta per la gestione errori di EMW7 platform.

In questo documento, la parola servizio (a meno che non sia una servizio del livello `logic`, si veda di
seguito) è utilizzata come sinonimo di applicazione.

# Architettura e linee guida

Questo capitolo è una guida pratica ad alto livello e concettuale
su come scrivere un'applicazione basata su EMW7 platform e/o
estendere `platform-service-runtime` per una soluzione specifica (come può essere REST o GRPC). 
Si deve prestare attenzione al fatto che, mentre alcune cose possono essere forzate a livello di 
codice sorgente e quindi può essere forzato il fatto che vadano rispettate, altre non possono 
essere forzate e per cui queste cose vengono indicate come best practise da seguire.
Alcune cose possono essere forzate a codice, altre invece sono delle best practise che vanno
rispettate.  
Nel capitolo [How to write an application](#how-to-write-an-application) si approfondiscono i
concetti qui visti su un esempio concreto.

L'idea che sta alla base di una applicazione EMW7 platform è quella per cui un'applicazione possa 
esporre più tipi di API (o possa passare da un tipo a un altro il minimo impatto). Per cui l'applicazione 
è suddivisa in livelli: il più alto (`api`) dialoga con i client esponendo i vari tipi di API che sono necessari e 
l'unico compito degli elementi di questo livello è di passare le richieste e ricevere le risposte al livello 
sottostante (`logic`) che sarà unico per tutti i tipi di API.

Quindi un'applicazione scritta utilizzando EMW7 platform è suddivisa in livelli (che novità).

Nel livello `api` risiedono i `controller` che sono gli elementi che ricevono le `request` dai client 
e gli forniscono le `response`. Le `request` e le `response` fanno parte dell'API e il formato dipende dal tipo
di API (ad esempio una `request` REST e formata da più 
componenti, come ad esempio query parameter, header, cookie, body, mentre una `request` GRPC è un 
singolo oggetto). Anche il formato delle `response` dipende dal tipo di API (per REST potrebbe essere un json, mentre per GRPC è un oggetto), 
ma la risposta di una stessa API implementata in tipi diversi deve riportare la stessa informazione.
Compito del `controller` è quindi quello di mappare la `request` del 
client in una `request` del livello `logic`, invocare l'operazione corrispondente del livello `logic` 
e mappare la `response` (corretta o di errore che sia) nella `response` da dare al `client`.

Il livello `logic`, oltre alla definizione dell'operazione, deve definire la struttura per le 
richieste e la struttura per risposte per ogni operazione.  
In caso di errore l'operazione deve lanciare un'eccezione che sia nella gerarchia 
`com.github.emw7.platform.service.core.common.request.error.RequestErrorException`.  
Si distinguono 2 tipi di errore:
- Client: l'errore è dovuto al `client`, ad esempio un valore numerico è fuori dall'intervallo consentito.
- Server: l'errore è dovuto a problemi lato server, ad esempio il database non è disponibile.

Il `controller` deve quindi gestire gli errori segnalati dal livello `logic` gestendo 
eccezioni nella gerarchia `com.github.emw7.platform.service.core.common.request.error.RequestErrorException` 
e segnalare, di conseguenza, l'errore al `client`. Si approfondisce quindi di seguito la gestione 
degli errori.

## Error management

La gestione errori di EMW7 platform distingue 2 tipi di errore:
- Errore di tipo client: quando l'errore è dovuto a chi fa la richiesta, ad esempio se un ingresso non è valido.
- Errore di tipo server: quando l'errore è dovuto al server, ad esempio se la connessione al db non è disponibile.

Inoltre, la gestione errori di EMW7 platform prevede che la risposta data al client abbia sempre los tesso formato 
a prescindere dal tipo di errore. Tale formato è definito da:
`com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse` in cui è contenuta 
l'informazione necessaria per identificare l'errore occorso.

Un elemento del livello `logic` in caso di errore deve lanciare un eccezione che estende 
- ClientRequestErrorException in caso di errore di tipo client. 
- ServerRequestErrorException in caso errore di tipo server.

Il `controller` deve catturare questi due tipi di eccezioni e delegare la generazione della risposta 
o la restituzione della risposta stessa a una implementazione di, rispettivamente:
- `com.github.emw7.platform.service.runtime.error.handler.AbstractClientExceptionHandler`.
- `com.github.emw7.platform.service.runtime.error.handler.AbstractServerExceptionHandler`.

Se delegare solo la generazione della risposta o se delegare anche la restituzione della risposta (di errore) 
dipende dalla specifica implementazione per il tipo di API. Ad esempio per REST, integrandosi con Spring, 
è possibile delegare anche la restituzione della risposta.

La generica struttura di un `controller` sarà quindi la seguente:
```java
public Object action (@NonNull final ApiRequest request) {
  try {
    ServiceRequest serviceRequest= new ServiceRequest(request);
    final ServiceResponse serviceResponse = service.action(serviceRequest);
    return new ApiResponse(serviceResponse);
  } catch (ClientRequestErrorException e) {
    return clientExceptionHandler.handle(e);
  } catch (ServerRequestErrorException e) {
    return serverExceptionHandler.handle(e);
  }
}
```
Per capire come funzionano gli errori e la loro gestione in EMW7 platform è necessario vedere 
alcune delle proprietà che definiscono un errore:
- `type`: server o client.
- Un errore ha una tipologia e la tipologia induce:
  - Lo `status`: un codice numerico che identifica la tipologia; esempio: 404 => entità specificata non trovata.
  - Una parte della proprietà `ref` (si veda di seguito per i dettagli di questa proprietà);
    esempio: D4FAA.
  - `label` e `params` 
    [DESIGN{params 9 oggetto di valutazione per decidere se rimuoverli perché è difficile se non impossible specificarli}]; 
    ogni tipologia ha la sua label (esempio: com.github.emw7.platform.error.client.not-found è in 
    relazione biunivoca con lo la parte di `ref` indotta dalla tipologia).
- `errors`: una lista di errori (spesso un solo errore) che hanno portato alla specifica tipologia 
  di errore; esempio: se il client ha richiesto di agire sull'entità X ma questa non esiste, allora 
  l'applicazione segnalerà la cosa con un errore con tipologia not-found (ref (first part) D4FAA, label 
  com.github.emw7.platform.error.client.not-found) il cui campo `error` specifica che non è stata 
  trovata l'entità X.

`status` non è in relazione (stretta) con la parte di `ref` e `label` perché ad esempio lo 
`status` 500 è proprio degli errori di tipo server, ma usato per tutte le tipologie e lo stato 
`status` 400 è usato per diverse tipologie di errori legati a errori negli ingressi specificati dal 
client.

In EMW7 platform sono definite le tipologie di errore più comuni di tipo ma permette 
all'applicazione di definirne di propri.

### Come definire i propri errori

Prima di vedere gli errori standard definiti da EMW7 platform è utile vedere come un'applicazione può definire i propri errori. Infatti, come può definirli l'applicazione è (esattamente) come li definisce EMW7 platform. Per completezza vengono mostrati 3 modi. Sicuramente esistono infiniti modi di definire gli errori, ma quelli presentati sono quelli consigliati e dei 3 il terzo il preferibile.


#### First way

Il primo modo che viene presentato non permette di specificare un errore specifico, facendo così coincidere l'errore con la tipologia. Questo modo è meno flessibile, dal punto di vista della 
personalizzazione dell'errore rispetto al secondo modo che verrà illustrato di seguito.

La prima cosa da fare è definire la tipologia tramite una annotation come segue:
```java
// Target TYPE as it will annotate the exception class representing the error.
@Target({ElementType.TYPE})
// Retention RUNTIME as it is retrieved by exception handler.
@Retention(RetentionPolicy.RUNTIME)
@Documented
// Meta-annotated by @RequestError to define status and label 
//  that is part of the error tipology.
@RequestError(errorCode = 429, label = "app.i18n.error.client.too-many-requests")
public @interface AcmeTooManyRequestsClientError {}
```

La retention deve essere `RUNTIME` dato che l'annotazione viene usata dall'exception handler. L'exception handler recupera le informazioni definite in `@RequestError` per popolare la risposta di errore.

L'annotazione appena definita viene usata per annotare la l'eccezione che rapresenta l'errore:
```java
// Annotated in this way to "inherit" errorCode and label from 
//  @RequestError.
@TooManyRequestsClientError
// NOTE: extends ClientRequestErrorException so this represented 
//  an error caused by the client.
public final class TooManyRequestsClientException extends ClientRequestErrorException {
  
  // This the unique code that is part of the error tipology; 
  //  the other parts are in the @RequestError (TODO I know this name can create confusion with errorCode of the @RequestError annotation, to be adjusted).
  // Generated via https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new
  public static final Code CODE= new Code("KCYVD");

  // The constructor accepts only the error instace id, to the 
  //  super construcor is passed an empty list of errors, so 
  //  the error tipology match with error it-self.
  public TooManyRequestsClientException(@NonNull final Id id) {
    super(CODE, id, (List<Error>) null);
  }
}
```

Executing the following code `throw new TooManyRequestsClientException(new Id("V26YW"));` and managing it through the default client exception manager the obtained request converted to json is the following:
```json
{"timestamp":"2025-01-23T13:36:20.575273908Z","type":"CLIENT","status":429,"ref":"KCYVD-DBKD7","traceId":"13e1686c759927ab6f80c98b6dd60a06","spanId":"1480452d266278d8","message":"Too many requests","label":"app.i18n.error.client.too-many-requests","errors":[]}
```
In the detail above please note:
- The first part of the `ref` property that is the `CODE` defined in the error exception.
- The `status` property that is the value defined in the `@RequestError` with which is annotated the `@TooManyRequestsClientError` annotation.
- The `label` property that is the value defined in the `@RequestError` with which is annotated the `@TooManyRequestsClientError` annotation.

#### Second way

Il secondo modo che viene presentato permette di specificare uno o più errori specifici, rendendo possibile specializzare la tipologia: una tipologia di errore può essere dovuta a più problemi. Questo modo è più flessibile, dal punto di vista della 
personalizzazione dell'errore rispetto al primo modo visto in precedenza.

Anche in questo caso la prima cosa da fare è definire la tipologia tramite una annotation, come segue:
```java

// Target TYPE as it will annotate the exception class representing the error.
@Target({ElementType.TYPE})
// Retention RUNTIME as it is retrieved by exception handler.
@Retention(RetentionPolicy.RUNTIME)
@Documented
// Meta-annotated by @RequestError to define status and label 
//  that is part of the error tipology.
@RequestError(errorCode = 423, label = "app.i18n.error.client.locked")
public @interface LockedClientError {}
```

Anche in questo caso l'annotazione appena definita viene usata per annotare la l'eccezione che rapresenta l'errore:
```java
@LockedClientError
public final class LockedClientException extends ClientRequestErrorException {

  public static final Code CODE = new Code("8OSOH");

  public static class ErrorWrapper {
    private final RequestErrorException.Error error;

    private ErrorWrapper(String label, Map<String, Object> params) {
      this.error = new RequestErrorException.Error(label, params);
    }

    private RequestErrorException.Error map() {
      return error;
    }
  }

  public static ErrorWrapper nonRenewableResourceLocking(@NonNull final String resource,
      @NonNull final String owner, int lastedMilliseconds, int remainingMilliseconds) {
    return new ErrorWrapper("app.i18n.error.non-renewable-locking",
        Map.of("resource", resource, "owner", owner, "lastedMilliseconds", lastedMilliseconds,
            "remainingMilliseconds", remainingMilliseconds));
  }

  public static ErrorWrapper renewableResourceLocking(@NonNull final String resource,
      @NonNull final String owner, int consumedMilliseconds, int mustRenewWithinMilliseconds,
      int renewedTimes) {
    return new ErrorWrapper("app.i18n.error.renewable-locking",
        Map.of("resource", resource, "owner", owner, "consumedMilliseconds", consumedMilliseconds,
            "mustRenewWithinMilliseconds", mustRenewWithinMilliseconds, "renewedTimes",
            renewedTimes));
  }

  // constructor
  public LockedClientException(@NonNull final Id id, @NonNull final ErrorWrapper error, @NonNull final ErrorWrapper... errors) {
    super(CODE, id,
        Arrays.stream(errors).map(ErrorWrapper::map)
            .collect(() -> { final List<Error> l= new ArrayList<>(1+ errors.length); l.add(error.map()); return l;},
                List::add, List::addAll));
  }

}
```

`ErrorWrapper` is a (private) class that allows to limit the type of errors that can be specified to `LockedClientException`. The only allowed errors in this case are `nonRenewableResourceLocking` and `renewableResourceLocking`, definiti dai due rispettivi metodi.  
La classe `LockedClientException` ha un costruttore che 
oltre ad accettare l'`Id` accetta un errore obbligatorio e poi una 
lista, eventualmente vuota, di errori, ma questi errori devono essere di tipo `ErrorWrapper` che può essere istanziata solo da dentro `LockedClientException` limitando così i tipi di errore che possono essere specificati.

In pratica la tipologia `locked` porta con se la causa e, nell'esempio proposto, le cause possono essere 2:
- nonRenewableResourceLocking.
- renewableResourceLocking.

Chi riceve l'errore `locked` può inferire la causa analizzando la proprietà `errors` e agire di conseguenza.

#### Third way

Il terzo modo è quello indicato come preferito.

Consiste nel creare una classe astratta che definisce la tipologia:
```java
/**
 * The base class for locked client error The base class for locked client error.
 * <p>
 * Extends this class to specialize the error.
 */
@LockedClientError
public abstract class LockedClientException extends ClientRequestErrorException {

  public static final Code CODE = new Code("8OSOH");

  // constructor
  protected LockedClientException(@NonNull final Id id, @NonNull final Error error) {
    super(CODE, id, error);
  }
}
```

Note that:
- It reuses the `@LockedClientError` annotation.
- It defines the `CODE` property.

Then define a class for each specialization. In this case it has been decided to not allow more than 1 error per specialization:
```java
/**
 * Specialization of locked client error for renewable specialization.
 * <p>
 * An alternative to
 * {@code LockedClientException(new Id("3M9DD"),
 * LockedClientException.renewableResourceLocking("a-renewable-locking-resource", "other", 10,
 * 1));}
 */
public final class RenewableLockedClientException extends LockedClientException {

  public RenewableLockedClientException(@NonNull final Id id, @NonNull final String resource,
          @NonNull final String owner, int consumedMilliseconds, int mustRenewWithinMilliseconds,
          int renewedTimes) {
    super(id, new Error("app.i18n.error.renewable-locking",
        Map.of("resource", resource, "owner", owner, "consumedMilliseconds", consumedMilliseconds,
            "mustRenewWithinMilliseconds", mustRenewWithinMilliseconds, "renewedTimes",
            renewedTimes)));
  }
}



/**
 * Specialization of locked client error for non-renewable specialization.
 * <p>
 * An alternative to
 * {@code throw new LockedClientException(new Id("ISVXB"),
 * LockedClientException.nonRenewableResourceLocking("a-non-renewable-locking-resource", "other",
 * 15, 85));}
 */
public final class NonRenewableLockedClientException extends LockedClientException {

  public NonRenewableLockedClientException(@NonNull final Id id, @NonNull final String resource,
          @NonNull final String owner, int lastedMilliseconds, int remainingMilliseconds) {
    super(id, new Error("app.i18n.error.non-renewable-locking",
            Map.of("resource", resource, "owner", owner, "lastedMilliseconds", lastedMilliseconds,
                    "remainingMilliseconds", remainingMilliseconds)));
  }
}
```

#### Definizione di un error server

Definire un errore di tipo server è analogo a definire un errore di tipo client con la differenza che per gli errori di tipo server:
- Lo status è sempre `500`.
- La label è sempre `com.github.emw7.platform.error.internal-server-error`.

Dai punti precedenti segue che non è necessario definire l'annotazione.

Un errore di tipo server dovrà quindi definire il proprio `code` e l'errore specifico che l'ha causato:

Yes, there is a difference among the client errors and the server errors. 
It has been accepted as reasonable as while for the client error the application can only specialize the tipology for what concerns the server erros the application can defined its own. This is depicted in the diagram a the end of the _Error management_ part of this chapter.

```java
public final class StorageNotAvailabledServerRequestErrorException extends ServerRequestErrorException {

  //region Private static final properties
  // generated with
  //  https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
  private static final Code CODE = new Code("J5ESE");
  //endregion Private static final properties

  @I18nLabel(params = {"storage"})
  private static final String I18N_LABEL = "app.i18n.error.storage-not-available";
  //endregion Private static final properties

  //region Constructors
  public StorageNotAvailabledServerRequestErrorException(@NonNull final Id id,
      @NonNull final String storage) {
    super(CODE, id, new Error(I18N_LABEL, Map.of("storage", storage)));
  }
  //endregion Constructors
}
```

---

In this diagram is depicted the errors hierarchy and are given some hints.

![Errors hierarchy](./Errors%20hierarchy.png)


# How to write an application

In questo capitolo si spiega in modo pratico come scrivere un'applicazione utilizzando EMW7
platform e rispettando alcuni concetti di buon senso.  
Viene usato come riferimento il progetto di esempio 
[ex-platform-service-runtime](./../ex-platform-service-runtime/).  
Come detto in precedenza il progetto `platform-service-runtime` non è pensato per essere utilizzato direttamente da
un'applicazione, ma per funzionare come base per implementazioni più specifiche, comunque questo 
_How to_ e il progetto a cui fa riferimento è utile per apprendere i concetti base 
per poi sfruttarli sia per capire come implementare un progetto più specifico basato su 
`platform-service-runtime` e sia per utilizzare quelli già messi a disposizione da EMW7 platform.

Come esempio verrà implementato un'API che permette di creare e rimuovere sensori.  
L'API sarà implementata/simulata tramite command line, dove il primo argomento è l'endpoint da invocare e i seguenti sono i parametri dell'endpoint stesso:
- ./run.sh create 100000 "Sensor A" 1
                  ^code  ^name      ^type
- ./run.sh delete 100000
                  ^code

Inoltre, specificando dei code particolari è possibile simulare dell'eccezioni. Si veda il codice sorgente per i dettagli.

Nel package `model` creare la classe `Sensor`.  
Nel package `logic` creare l'interfaccia `SensorService` che dichiara i metodi `create` e `delete` e le rispettive classi di 
richiesta e risposta. I metodi dichiarano anche le eccezioni che possono lanciare.  
Definire tali eccezioni nel package `logic.error.client` e `logic.error.server`.  

Nel package `api` creare l'interfaccia `SensorController` che dichiara gli endpoint (`create` e `delete`) e le rispettive classi di richiesta e risposta.  

> *Nota*: in questo caso le risposte contengono oltre alla parte di payload in caso di esecuzione corretta anche una parte di payloaf per l'errore. Questa è una scelta arbitraria di questo esempio, in cui è stato deciso di far invocare l'exception handler al controller. Si veda [Controlloer alternativo](#controller-alternativo) per una versione alternativa.

Nel pacakge `error` sono stati definiti l'exception handler per gli errori client e gli errori server. Non sono stati usati gli exception handler di default per mostrare come è possibile implementare un exception handler. Si noti che in questo caso viene restituita una istanza di `RequestErrorResponse`, in altri casi potrebbe essere necessario restituire un'altra sua rapresenzazione.  
Le due implementazioni (client e server) sono del tutto analoghe e l'unica istruzione veramente necessaria è `final RequestErrorResponse errorResponse= buildRequestErrorResponse(error);` il resto è solo per rendere un po' più articolato l'esempio stampando la rapresentazione json dell'istanza di delle risposta di errore.

# Appendici

## Controller alternativo

In questo capitolo viene mostrata una versione alternativa al controller implementato nell'esempio. Una versione simile potrebbe essere utilizzata in un'API REST Spring oriented in quanto in questo caso gli errori vengono mandati in gestione ad un `@ControllerAdvice`:
```java

// SensorController (interface).
  // Error payload has been removed from the response.
  record CreateSensorResponse (@Nullable Sensor sensor) {}

// SensorControllerImpl / endpoint :: Create.
  @Observed(name = "ControllerSensor#create")
  @Override
  public @NonNull CreateSensorResponse create(@NonNull final CreateSensorRequest request) throws RequestErrorException {
    final SensorService.CreateSensorRequest serviceRequest = new SensorService.CreateSensorRequest(request.sensor());

      final SensorService.CreateSensorResponse serviceResponse = getServiceSensor().create(serviceRequest);

      return new CreateSensorResponse(
          new Sensor(serviceResponse.sensor().code(),
                    serviceResponse.sensor().name(),
                    serviceResponse.sensor().type()));

  }

// AcmeApplication.

  // The try-catch that was in the controller implementation has been moved here.
  private void create(@NonNull final String... args) {
    final CreateSensorRequest request = new CreateSensorRequest(new Sensor(args[1], args[2], Integer.parseInt(args[3])));
    
    final Object res;

    try {
      res= controllerSensor.create(request);
    } catch (ClientRequestErrorException e) {
      res= getClientExHandler().handle(e);
    } catch (ServerRequestErrorException e) {
      res= getServerExHandler().handle(e);
    }

    System.out.printf("res: %s%n", res);
  }
```
