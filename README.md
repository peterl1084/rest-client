# RestClient #

RestClient is Java Command/Handler pattern based REST Client allowing easy Command based REST invocations in any Java App using Spring.

## Setup ##

* RestClient requires SpringBoot to run.
* RestClient can be used by applying `@EnableRestClient` annotation on your project's Configuration bean.
* Your application.properties should have the following definitions:

```java
rest.client.serverBaseUrl = http://url-to-server:port
rest.client.connectionTimeout = timeout-in-ms
rest.client.readTimeout = timeout-in-ms
```

## RestMethod ##

All invocations that can be executed with this RestClient are to implement `RestMethod<RESULT_TYPE>` interface. The `RESULT_TYPE` maybe either a direct return value DTO type or a `Collection<DTO>`, for example:

`class GetCustomersMethod implements RestMethod<List<Customer>>`

or

`class GetCustomerMethod implements RestMethod<Customer>`

The methods then need to be annotated with `RestMethodConfiguration` that specifies the serviceUri's relative part as well as which HttpMethod is used (GET, POST...)

Once done the @Autowired RestClient can be used to invoke the methods directly for example.

```property
@Autowired
private RestClient client;

List<Customer> customers = client.invoke(new GetCustomersMethods());
Customer customer = client.invoke(new GetCustomerMethods(123));
```

The client will automatically take care of all type conversion and return type handling as long as the invoked REST method really returns JSON or XML that can be mapped to expected <RESULT_TYPE>.


