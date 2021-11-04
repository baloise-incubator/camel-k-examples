package ch.baloise.example.jitpack.subpackage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyProcessor implements Processor {
    public void process(Exchange exchange) {
        exchange.getIn().setHeader("myProc", "called");
    }
}
