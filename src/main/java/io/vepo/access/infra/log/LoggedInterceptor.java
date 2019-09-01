package io.vepo.access.infra.log;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vepo.access.infra.Credentials;

@Logged
@Interceptor
public class LoggedInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LoggedInterceptor.class);

	@Inject
	private Credentials credentials;

	@AroundInvoke
	public Object logMethodEntry(InvocationContext invocationContext) throws Exception {
		logger.info("Calling with credentials: {}", this.credentials);
		logger.info("Entering method: {}  in class {}", invocationContext.getMethod().getName(),
				invocationContext.getMethod().getDeclaringClass().getName());

		return invocationContext.proceed();
	}
}
