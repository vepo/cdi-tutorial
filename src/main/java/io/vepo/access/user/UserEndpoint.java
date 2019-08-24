package io.vepo.access.user;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.vepo.access.infra.Repository;

@Path("/user")
public class UserEndpoint {

	@Inject
	private Repository<User> userRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		return userRepository.findAll();
	}
}