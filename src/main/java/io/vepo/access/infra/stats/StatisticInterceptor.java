package io.vepo.access.infra.stats;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Measured
@Interceptor
public class StatisticInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(StatisticInterceptor.class);

    @AroundInvoke
    public Object calculateExecutionTime(InvocationContext invocationContext) throws Exception {
        long startTime = System.currentTimeMillis();
        Object returnedValue = invocationContext.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("The execution of {}.{} took {}ms", invocationContext.getMethod().getName(),
        invocationContext.getMethod().getDeclaringClass().getName(), endTime - startTime);
        return returnedValue;
    }
}