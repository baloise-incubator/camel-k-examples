package ch.baloise.example.jitpack;

import ch.baloise.example.jitpack.subpackage.MyProcessor;
import org.apache.camel.builder.RouteBuilder;

//  kamel run -d github:baloise-incubator/camel-k-examples/main-SNAPSHOT ./src/main/java/ch/baloise/example/jitpack/JitPackRoute.java

public class JitPackRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        org.apache.camel.Processor myProc = new MyProcessor();

        from( "timer:java?period=1000" )
                .log("Timer tick")
                .process(myProc)
                .to("log:api?showAll=true&multiline=true")
                .setBody()
                .simple("Hello ${headers.name}");
    }
}
