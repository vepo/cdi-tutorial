package io.vepo.access.lang;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named("us")
public class GoodByeDefaultService implements GoodByeService {

	@Override
	public String sayGoodBye(String username) {
		return String.format("Bye %s!!!!", username);
	}

}
