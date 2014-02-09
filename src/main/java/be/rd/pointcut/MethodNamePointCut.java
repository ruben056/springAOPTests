package be.rd.pointcut;

import java.lang.reflect.Method;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

/**
 * Note: this is just an example. There is allready an implementation for
 * for matching methods by mane provided by sprin gsee NameMethodMatcherPointcut... 
 * 
 * @author ruben
 *
 */
public class MethodNamePointCut extends StaticMethodMatcherPointcut {

	@Override
	public boolean matches(Method method, Class targetClass) {
		return method.getName().equalsIgnoreCase("printBananas");
		
	}
}
