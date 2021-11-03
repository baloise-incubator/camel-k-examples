package ch.baloise.contact;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactLogger implements Processor {
    private static Logger LOGGER = LoggerFactory.getLogger(ContactLogger.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        Contact contact = (Contact)exchange.getIn().getBody();
        LOGGER.info("Contact: " + contact.getFirstName() + " " + contact.getLastName() + ", City: " + contact.getCity());
    }
}
