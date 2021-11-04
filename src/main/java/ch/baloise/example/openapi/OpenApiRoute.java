// camel-k: name=open-api-route
// camel-k: trait=route.tls-termination=edge
// camel-k: trait=quarkus.enabled=true
// camel-k: dependency=camel-jackson
// camel-k: dependency=camel-openapi-java
// camel-k: dependency=mvn:org.projectlombok:lombok:1.18.22

package ch.baloise.example.openapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.BindToRegistry;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

@Slf4j
public class OpenApiRoute extends RouteBuilder {
    @Override
    public void configure() {
        restConfiguration()
                .component("platform-http")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API").apiProperty("api.version", "1.2.3")
                .apiProperty("cors", "true")
                .clientRequestValidation(true);

        rest("/say")
                .get("/hello").to("direct:hello")
                .get("/bye").consumes("application/json").to("direct:bye")
                .post("/bye").to("mock:update");

        from("direct:hello")
                .transform().constant("Hello World")
                .log("log:info?showBody=true");

        from("direct:bye")
                .transform().constant("Bye World")
                .log("log:info?showBody=true");

        rest("/say")
                .get("/{name}")
                .route().transform().simple("Hello ${headers.name}")
                .log("log:info?showBody=true").endRest();

        rest("/users").description("User rest service")
                .consumes("application/json").produces("application/json")

                .get("/{id}").description("Find user by id").outType(User.class)
                .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("integer").endParam()
                .responseMessage().code(200).message("The user").endResponseMessage()
                .to("bean:userService?method=getUser(${header.id})")

                .put().description("Updates or create a user").type(User.class)
                .param().name("body").type(RestParamType.body).description("The user to update or create").required(true).endParam()
                .responseMessage().code(200).message("User created or updated").endResponseMessage()
                .to("bean:userService?method=updateUser")

                .get().description("Find all users").outType(User[].class)
                .responseMessage().code(200).message("All users").endResponseMessage()
                .to("bean:userService?method=listUsers")

                .get("/{id}/departments/{did}").description("Find all users").outType(User[].class)
                .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("integer").endParam()
                .param().name("did").type(RestParamType.path).description("The id of the department to get").dataType("integer").required(true).endParam()
                .responseMessage().code(200).message("All users").endResponseMessage()
                .to("bean:userService?method=listUsers");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private int id;
        @NotNull
        private String name;
    }

    @BindToRegistry("userService")
    @Slf4j
    public static class UserService {
        private final Map<String, User> users = new TreeMap<>();
        private Random ran = new Random();
        public UserService() {
            users.put("123", new User(123, "John Doe"));
            users.put("456", new User(456, "Donald Duck"));
            users.put("789", new User(789, "Slow Turtle"));
        }

        public User getUser(String id) {
            log.info("getUser", id);
            if ("789".equals(id)) {
                int delay = 500 + ran.nextInt(1500);
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {
                    // ignore
                }
            }
            return users.get(id);
        }

        public Collection<User> listUsers() {
            log.info("listUsers");
            return users.values();
        }

        public void updateUser(User user) {
            log.info("updateUser", user);
            users.put("" + user.getId(), user);
        }
    }
}
