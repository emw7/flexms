# platform-i18n

# Miscellaneous

- MessageSource è inizializzato di default in automatico da spring, per cui a meno di cataclismi è sempre disponibile.
- I messages*.properties possono essere in giro per i vari classpath, per cui si possono mettere anche in platform.
- Viene usato org.apache.commons.text.StringSubstitutor che supporta **anche** (https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/StringSubstitutor.html):  
  Base64 Decoder:        ${base64Decoder:SGVsbG9Xb3JsZCE=}  
  Base64 Encoder:        ${base64Encoder:HelloWorld!}  
  Java Constant:         ${const:java.awt.event.KeyEvent.VK_ESCAPE}  
  Date:                  ${date:yyyy-MM-dd}  
  Environment Variable:  ${env:USERNAME}  
  File Content:          ${file:UTF-8:src/test/resources/document.properties}  
  Java:                  ${java:version}  
  Localhost:             ${localhost:canonical-name}  
  Properties File:       ${properties:src/test/resources/document.properties::mykey}  
  Resource Bundle:       ${resourceBundle:org.apache.commons.text.example.testResourceBundleLookup:mykey}  
  System Property:       ${sys:user.dir}  
  URL Decoder:           ${urlDecoder:Hello%20World%21}  
  URL Encoder:           ${urlEncoder:Hello World!}  
  XML XPath:             ${xml:src/test/resources/document.xml:/root/path/to/node}



