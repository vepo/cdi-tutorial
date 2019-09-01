package io.vepo.access.lang;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.vepo.access.infra.Credentials;
import io.vepo.access.infra.Secured;

@Path("hello")
public class HelloEndpoint {

	@Inject
	private Credentials credentials;

	@Inject
	@PtBr
	private HelloService ptBrHelloService;

	@Inject
	private HelloService defaultHelloService;

	@Inject

	@GET
	@Path("pt-br")
	@Secured
	public String sayHelloPtBr() {
		return ptBrHelloService.sayHello(this.credentials.getUsername());
	}

	@GET
	@Secured
	public String sayHelloUs() {
		return defaultHelloService.sayHello(this.credentials.getUsername());
	}

}
