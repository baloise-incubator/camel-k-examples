# camel-k-examples
dev environment for camel-k examples

# route exmaple

Example for calling a camel-k micro service from within another camel-k micro service. Used two easy open-api 
micro services passing a paramter "name" which is concatenated by the second service call. 

Findings: the rest parameter "name" is put into the message header by camel. In order to avoid a multiple definition 
by the inner service call, it need to be removed.
Using setProperty() for variables only works inside the Exchange object but there is no way to access these with
getProperty() inside the same dsl route, e.g. in the toD :(. 


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

### Transform partner json to contact json
To execute this example run the Camal Main app `./src/main/java/ch/baloise/contact/mapper/ContactMapperMainApp.java`
This route reads json files from the folder `./src/data/partner` and maps the incoming json eventually to a json representing a contact object.
Uses Camel marshalling and unmarshalling.