package ch.baloise.example.contact.mapper;

import ch.baloise.example.contact.Contact;
import ch.baloise.example.contact.Partner;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps a given partner object into a contact object using the PartnerContactMapper.
 * Enriches the city based on the postalcode.
 */
public class PartnerMappingProcessor implements Processor {

    private static Logger LOGGER = LoggerFactory.getLogger(PartnerMappingProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        Partner partner = (Partner) exchange.getIn().getBody();
        LOGGER.info("processing partner: " + partner.getFirstName() + " " + partner.getLastName());

        Contact contact = new PartnerToContactMapper().mapToContact(partner);
        exchange.getIn().setBody(contact);
    }
}
