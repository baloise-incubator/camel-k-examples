// camel-k: language=java name=DemoJar trait=route.tls-termination=edge
// camel-k: trait=quarkus.enabled=true
// camel-k: dependency=camel-jackson
// camel-k: dependency=camel-openapi-java
// camel-k: dependency=mvn:org.projectlombok:lombok:1.18.22
// camel-k: resource=target/camel-k-examples-1.0-SNAPSHOT.jar
// camel-k: trait=jvm.classpath=/etc/camel/resources/camel-k-examples-1.0-SNAPSHOT.jar

// kamel run src/main/java/ch/baloise/example/demojar/ContactApiRoute.java

package ch.baloise.example.demojar;

import ch.baloise.example.demojar.model.Contact;
import ch.baloise.example.demojar.service.ContactNotFoundException;
import ch.baloise.example.demojar.service.ContactService;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Camel Route offering a Rest Api via Camel Rest DSL
 */
public class ContactApiRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        getContext().getRegistry().bind("contactService", new ContactService());

        restConfiguration()
                .component("platform-http")
                .bindingMode(RestBindingMode.json);

        onException(ContactNotFoundException.class)
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
