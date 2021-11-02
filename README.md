# camel-k-examples
dev environment for camel-k examples

## Run this example by using 
`mvn camel:run`

## Documentation
- https://camel.apache.org

## Examples
### Transform
You can use OpenAPI v3. Over the `operation-id` you can connect operations form the OpenAPI YAML to the Camel DSL.  
To run the integration you have to do `kamel run ./src/main/java/ch/baloise/example/transform/TransformRoute.java`.
The `tls-termination=edge` is for OpenShift to enable HTTPS.

In general a lot of the OpenAPI spec is not respected like payload validations or return types/codes.  
It makes more sense to use https://camel.apache.org/manual/rest-dsl.html to define the API by DSL and generate the OpenAPI.

