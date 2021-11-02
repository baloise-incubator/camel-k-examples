package ch.baloise;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Test;

public class HelloTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new HelloRoute();
    }

    @Test
    public void testHello() throws Exception {
        Object reply = template.requestBodyAndHeader("direct:hello", "", "name", "World");
        Assert.assertEquals("Hello World", reply);
    }
}

