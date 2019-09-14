package io.vepo.access.lang;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Any
@Decorator
public abstract class HelloServiceValidator implements HelloService {

	private static final Logger logger = LoggerFactory.getLogger(HelloServiceValidator.class);

	@Inject
	@Delegate
	private HelloService delegate;

	@Override
	public String sayHello(String username) {
		logger.info("Acessing delegate class! username: {}, beanClass: {}", username, delegate.getClass());
		return delegate.sayHello(username);
	}
}
