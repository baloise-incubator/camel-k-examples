// camel-k: language=java name=RouteReceiver trait=route.tls-termination=edge open-api=openapi.yaml

// kamel run OpenApiReceiver.java --name RouteReceiver --open-api openapi.yaml --trait route.tls-termination=edge

package ch.baloise.examples.route;

import org.apache.camel.builder.RouteBuilder;

public class OpenApiReceiver extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Get
        from("direct:greeting-id")
                .setBody()
                .simple("OpenApiReceiver: Hello Sender ${headers.name}")
                .to("log:info");

    }
}