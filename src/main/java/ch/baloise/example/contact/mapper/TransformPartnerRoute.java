package ch.baloise.example.contact.mapper;

import ch.baloise.example.contact.Contact;
import ch.baloise.example.contact.ContactLogger;
import ch.baloise.example.contact.Partner;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

/**
 * Transforms a list of partner objects into contact objects.
 */
public class TransformPartnerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        JacksonDataFormat partnerJsonDataFormat = new JacksonDataFormat(Partner.class);
        JacksonDataFormat contactJsonDataFormat = new JacksonDataFormat(Contact.class);

        from("file:src/data/partner?noop=true")
                .log(LoggingLevel.INFO, "Process file ${header.CamelFileName}")
                .doTry().unmarshal(partnerJsonDataFormat)
                .process(new PartnerMappingProcessor())
                .process(new ContactLogger())
                .marshal(contactJsonDataFormat)
                .log(LoggingLevel.INFO, "Mapped Contact json: ${body}")
                .doCatch(Exception.class).process(new UnmarshallExceptionProcessor());
    }
}
