// camel-k: language=java name=DemoInline trait=route.tls-termination=edge
// camel-k: trait=quarkus.enabled=true
// camel-k: dependency=camel-jackson
// camel-k: dependency=camel-openapi-java
// camel-k: dependency=mvn:org.projectlombok:lombok:1.18.22

// kamel run src/main/java/ch/baloise/example/demoinline/ContactApiRoute.java // --name DemoInline --trait route.tls-termination=edge

package ch.baloise.example.demoinline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Camel Route offering a Rest Api via Camel Rest DSL
 */
public class ContactApiRoute extends RouteBuilder {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Contact {
        private Long id;

        private String firstName;

        private String lastName;

        private String postalCode;

        private String city;
    }

    public static class ContactNotFoundException extends Exception {
        private long id;

        public ContactNotFoundException(long id) {
            super("Contact not found with id " + id);
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

    public static class ContactService {

        private static Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

        private final List<Contact> contacts;

        public ContactService() {
            contacts = createContactList();
        }

        public Collection<Contact> getContacts() {
            LOGGER.info("getContacts called...");
            return contacts;
        }

        public Contact getContact(long id) throws ContactNotFoundException {
            LOGGER.info("getContact with id " + id + " called...");
            Iterator<Contact> iterator = contacts.iterator();
            for (Contact contact : contacts) {
                if (contact.getId() == id) {
                    return contact;
                }
            }

            throw new ContactNotFoundException(id);
        }

        public Contact createContact(Contact contact) {
            LOGGER.info("createContact called...");
            int size = contacts.size();
            long nextId = size + 1;
            contact.setId(nextId);
            contacts.add(contact);
            return contact;
        }

        private List<Contact> createContactList() {
            List<Contact> contacts = new ArrayList<>();
            contacts.add(new Contact(1l, "Peter", "Meier", "4051", "Basel"));
            contacts.add(new Contact(2l, "Tom", "Eiger", "4052", "Basel"));
            contacts.add(new Contact(3l, "Tim", "Mönch", "4052", "Basel"));
            contacts.add(new Contact(4l, "Frank", "Müller", "4051", "Basel"));

            return contacts;
        }
    }

    @Override
    public void configure() throws Exception {
        getContext().getRegistry().bind("contactService", new ContactService());

        restConfiguration()
                .component("platform-http")
                .bindingMode(RestBindingMode.json);

        onException(Exception.class) // changed from ContactNotFoundException
                .handled(true)
                .log(LoggingLevel.ERROR, "${exception.message}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setBody(simple("${exception.message}\n"));

        rest("/contacts")
                .get()
                    .to("bean:contactService?method=getContacts")
                .get("{id}")
                    .to("bean:contactService?method=getContact(${header.id})")
                .post().type(Contact.class)
                    .to("bean:contactService?method=createContact");
    }
}
