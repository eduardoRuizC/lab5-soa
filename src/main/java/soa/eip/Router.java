package soa.eip;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Router extends RouteBuilder {

  public static final String DIRECT_URI = "direct:twitter";

  @Override
  public void configure() {
    from(DIRECT_URI)
      .log("Body contains \"${body}\"").process(exchange -> {
        String[] consult = exchange.getIn().getBody(String.class).split("max:");
        if (consult.length > 1 ) {
          String newConsult = consult[0];
          newConsult += "?count=" + consult[1];
          exchange.getOut().setBody(newConsult);
        } else {
          exchange.getOut().setBody(consult[0]);
        }
      })
      .log("Searching twitter for \"${body}\"!")
      .toD("twitter-search:${body}")
      .log("Body now contains the response from twitter:\n${body}");
  }
}
