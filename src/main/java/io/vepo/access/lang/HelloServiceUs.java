package io.vepo.access.lang;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloServiceUs implements HelloService {

	@Override
	public String sayHello(String username) {
		return String.format("Hello! %s", username);
	}

}
