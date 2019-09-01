package io.vepo.access.user;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vepo.access.infra.Credentials;
import io.vepo.access.infra.Jwt;
import io.vepo.access.infra.PasswordEncrypter;
import io.vepo.access.infra.Secured;
import io.vepo.access.infra.log.Logged;
import io.vepo.access.user.events.UserCreated;
import io.vepo.access.user.events.UserRemoved;

@ApplicationScoped
@Path("/user")
public class UserEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(UserEndpoint.class);

	private static User newAdminUser() {
		User user = new User();
		user.setEmail("admin@vepo.io");
		user.setUsername("admin");
		user.setPassword("qwas1234");
		user.setName("Administrator");
		return user;
	}

	@Inject
	private UserRepository userRepository;

	@Inject
	private Credentials credentials;

	@Inject
	private Jwt jwt;

	@Inject
	@ConfigProperty(name = "JWT_TTL", defaultValue = "3600000")
	private long jwtTtl;

	@Inject
	private PasswordEncrypter passwordEncrypter;

	@Inject
	private Event<UserCreated> createdEvent;

	@Inject
	private Event<UserRemoved> removedEvent;

	@DELETE
	@Path("{username}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User createUser(@PathParam("username") String username) {
		logger.info("Credentials: {}", credentials);
		Optional<User> userToDelete = this.userRepository.delete(username);
		userToDelete.ifPresent(user -> this.removedEvent.fire(new UserRemoved(user.getUsername())));
		return userToDelete.orElseThrow(() -> new NotFoundException("User not found!"));
	}

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User createUser(User user) {
		logger.info("Credentials: {}", credentials);
		user.setPassword(this.passwordEncrypter.encrypt(user.getPassword()));
		this.userRepository.create(user);
		this.createdEvent.fire(new UserCreated(user.getUsername()));
		return user;
	}

	@GET
	@Secured
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLoggedInfo() {
		logger.info("Credentials: {}", credentials);
		return Response.ok().entity(new LoggedUser(credentials.getUsername(), credentials.getEmail())).build();
	}

	@Logged
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		logger.info("Credentials: {}", credentials);
		return this.userRepository.findAll();
	}

	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
		User admin = this.userRepository.findByUsername("admin").orElseGet(() -> this.createUser(newAdminUser()));
		logger.info("Admin created: {}", admin);
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(LoginCredentials credentials) {
		return this.userRepository
				.findByUsernameAndPassword(credentials.getUsername(),
						passwordEncrypter.encrypt(credentials.getPassword()))
				.map(user -> jwt.createJWT(user.getId().toHexString(), "CDI-Tutorial", user.getUsername(), this.jwtTtl))
				.orElseThrow(() -> new NotAuthorizedException("User or Password invalid!"));
	}
}