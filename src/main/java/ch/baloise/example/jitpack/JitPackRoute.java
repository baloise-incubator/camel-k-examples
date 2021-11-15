// camel-k: name=jit-pack-route
// camel-k: dependency=github:baloise-incubator/camel-k-examples/84261f26b0f7f5d9fe9453ada8997af2a0df6884

package ch.baloise.example.jitpack;

import ch.baloise.example.jitpack.subpackage.MyProcessor;
import org.apache.camel.builder.RouteBuilder;

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
