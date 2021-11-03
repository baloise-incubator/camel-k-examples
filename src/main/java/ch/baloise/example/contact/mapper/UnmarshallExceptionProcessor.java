package ch.baloise.example.contact.mapper;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnmarshallExceptionProcessor implements Processor {
    private static Logger LOGGER = LoggerFactory.getLogger(UnmarshallExceptionProcessor.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        LOGGER.error("Exception during unmarshalling of json object: " + cause);
    }
}
