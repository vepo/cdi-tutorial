package io.vepo.cdi.hello;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class HelloEndpoint {

    @Inject
    private HelloGenerator generator;

    @GET
    public String sayHello() {
        return generator.hello();
    }
}