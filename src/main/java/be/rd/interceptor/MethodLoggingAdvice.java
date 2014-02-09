package be.rd.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodLoggingAdvice implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		String methodName = invocation.getMethod().getName();
		System.out.println("Started " + methodName);
		
		Object result = invocation.proceed();
		
		System.out.println("Ended " + methodName);
		
		return result;
	}

}
