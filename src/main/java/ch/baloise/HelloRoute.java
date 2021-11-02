package ch.baloise;

import org.apache.camel.builder.RouteBuilder;

public class HelloRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:hello")
                .to("log:api?showAll=true&multiline=true")
                .setBody()
                .simple("Hello ${headers.name}");
    }
}
