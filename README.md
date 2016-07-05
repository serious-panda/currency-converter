# Currency converter

Simple web based currency converter. Exchange rates pulled from openexchangerates.org.

## Version
1.0-SNAPSHOT

## Prerequisites
* Maven 3.x
* Java 8

## Build
```
mvn clean package -P <dev/prod>
```
dev - development environment with spring boot devtools jar  
prod - without devtools jar

## Run
```sh
java -jar target\currency-converter-<version>.jar
```
To run behind a proxy specify `proxy.host` & `proxy.port`. For example :
```sh
java -jar -Dproxy.host="host" -Dproxy.port=port target\currency-converter-1.0-SNAPSHOT.jar
```

### Usage note:
* There is no client side enforcement of input format therefore all dates should be provided in format of dd/MM/yyyy.
* If certain currency was not traded on a requested day, such as Bitcoin in 2007, "Symbol not supported" message will be displayed.
* Last conversion query will be displayed as a part of history only on next query or page reload.

## Implementation details
 - Project creates executable jar bundled with all necessary dependencies.
 - Based on Spring boot, MVC and JPA.
 - Conversion service implementation is tailored to limited capabilities of free 'openexchangerates' account. (Low quota and USD as a base currency.)
 - Requests to 'openexchangerates' are cached with Ehcache. See configuration file for TTL property.
 - User history of last 10 records is maintained per user.
 - Account information and user history is persisted in in-memory DB via JPA.
 - Very simple UI. No Javascript and minimal CSS.


## Bugs
1. Handling of badly formatted dates and conversion amount is not user friendly. Input such as 'abc' will cause to display long error message as a result of an exception.

## To Do
1. Add more testing.
2. Add CSS and Javascript for better UX.
3. Implement exceptions cache that will cache error responses from the exchange server to reduce reduce unnecessary.
4. Error pages (404, 500, etc.)

 

