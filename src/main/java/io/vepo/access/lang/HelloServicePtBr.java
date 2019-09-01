package io.vepo.access.lang;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@PtBr
public class HelloServicePtBr implements HelloService {

	@Override
	public String sayHello(String username) {
		return String.format("Olá! %s", username);
	}

}
