// camel-k: name=transform-route
// camel-k: trait=route.tls-termination=edge
// camel-k: open-api=./../../../resources/openapi-camel-route.yaml
// camel-k: dependency=camel-jackson
// camel-k: dependency=mvn:org.projectlombok:lombok:1.18.22

package ch.baloise.example.transform;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@Slf4j
public class TransformRoute extends RouteBuilder {
    @Override
    public void configure() {
        restConfiguration().bindingMode(RestBindingMode.json);

        from("direct:create-data-id")
                .process(exchange -> {
                    Message message = exchange.getIn();
                    LinkedHashMap body = (LinkedHashMap)message.getBody();

                    Response response = new Response();
                    response.setId((int)body.get("id"));
                    response.setFullname(body.get("firstname") + " " + body.get("lastname"));
                    LocalDateTime dateTime = LocalDateTime.parse((String)body.get("shipDate"), ISO_OFFSET_DATE_TIME);
                    response.setShipTimestamp(dateTime.toEpochSecond(ZoneOffset.UTC));
                    response.setComplete((boolean)body.get("complete") ? "Yes" : "No");
                    response.setNewField(UUID.randomUUID().toString());

                    log.info("Some log message");

                    message.setBody(response);
                })
                .to("log:info?showBody=true");
    }

    @Data
    public static class Response {
        private int id;
        private String fullname;
        private long shipTimestamp;
        private String complete;
        private String newField;
    }
}
