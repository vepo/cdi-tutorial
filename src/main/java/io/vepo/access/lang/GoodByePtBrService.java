package io.vepo.access.lang;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named("pt-br")
public class GoodByePtBrService implements GoodByeService {

	@Override
	public String sayGoodBye(String username) {
		return String.format("Tchau %s!!!", username);
	}

}
