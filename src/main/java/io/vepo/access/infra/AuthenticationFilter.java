package io.vepo.access.infra;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.vepo.access.user.User;
import io.vepo.access.user.UserRepository;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	private static final String REALM = "example";
	private static final String AUTHENTICATION_SCHEME = "Bearer";

	@Inject
	private Credentials authenticatedCredentials;

	@Inject
	private UserRepository userRepository;

	@Inject
	private Jwt jwt;

	private void abortWithUnauthorized(ContainerRequestContext requestContext) {
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"").build());
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.info("[BEGIN] Filter: {}", requestContext);
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (!isTokenBasedAuthentication(authorizationHeader)) {
			abortWithUnauthorized(requestContext);
			return;
		}

		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

		try {
			validateToken(token);
		} catch (JwtException jwte) {
			logger.info("Invalid JWT!", jwte);
			abortWithUnauthorized(requestContext);
		} finally {
			logger.info("[END] Filter!");
		}
	}

	private boolean isTokenBasedAuthentication(String authorizationHeader) {
		return authorizationHeader != null
				&& authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
	}

	private void validateToken(String token) throws JwtException {
		authenticatedCredentials.setToken(token);
		Claims claims = jwt.decodeJWT(token);
		User user = this.userRepository.findByUsername(claims.getSubject())
				.orElseThrow(() -> new NotAuthorizedException("Cannot find user!"));
		authenticatedCredentials.setToken(token);
		authenticatedCredentials.setUsername(user.getUsername());
		authenticatedCredentials.setEmail(user.getEmail());

	}
}