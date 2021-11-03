// camel-k: language=java name=RouteSender trait=route.tls-termination=edge open-api=openapi.yaml

package ch.baloise.example.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class OpenApiSender extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:greeting-id")
                .log("Incoming Body of sender ${body}")
                .log("Outgoing headers.name is ${headers.name}")
                .setHeader("paramName", simple("${headers.name}"))
                .removeHeaders("CamelHttp*")
                .removeHeaders("Http*")
                .removeHeader("name")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("log:api?showAll=true&multiline=true")
                .toD("http://route-receiver/v1/${headers.paramName}Sender");
    }
}