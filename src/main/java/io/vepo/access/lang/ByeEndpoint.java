package io.vepo.access.lang;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.vepo.access.infra.Credentials;
import io.vepo.access.infra.Secured;

@Path("bye")
public class ByeEndpoint {

	@Inject
	private Credentials credentials;

	@Inject
	@Named("pt-br")
	private GoodByeService ptBrGoodByeService;

	@Inject
	@Named("us")
	private GoodByeService defaultGoodByeService;

	@Inject

	@GET
	@Path("pt-br")
	@Secured
	public String sayByePtBr() {
		return ptBrGoodByeService.sayGoodBye(this.credentials.getUsername());
	}

	@GET
	@Secured
	public String sayByeUs() {
		return defaultGoodByeService.sayGoodBye(this.credentials.getUsername());
	}

}
