package be.rd.interceptor;

import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.ControlFlowPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.Pointcuts;

import be.rd.beans.MessageBean;
import be.rd.pointcut.MethodNamePointCut;

public class MethodLoggingAdviceTest {

	/**
	 * This creates an advice using the default advisor
	 * advice: the extra code added to the target
	 * advisor: to which parts of the target, default: all methods
	 */
	@Test
	public void testMethodLoggingAdvice(){		
		MessageBean msgBean = new MessageBean();
		MethodLoggingAdvice advice = new MethodLoggingAdvice();
		
		ProxyFactory fac = new ProxyFactory();
		fac.addAdvice(advice);
		fac.setTarget(msgBean);
		
		MessageBean proxy = (MessageBean)fac.getProxy();			
		// prints with methodlogging
		proxy.printBananas();
		proxy.printHello();
	
		//only method logging fo	r printbananas
		// remove the advice and add it with a custom pointcut
		fac.removeAdvice(advice);
		fac.addAdvisor(new DefaultPointcutAdvisor(new MethodNamePointCut(), advice)); 
		proxy = (MessageBean)fac.getProxy();
		proxy.printBananas();
		proxy.printHello();
		
		// no method logging
		msgBean.printBananas();
		msgBean.printHello();		
	}
	
	/**
	 * This will only add advice if the target is triggered
	 * from via the "detour" method of this class
	 */
	@Test
	public void testMethodLoggingAdviceWithCFPointCut(){
		
		MessageBean msgBean = new MessageBean();
		MethodLoggingAdvice advice = new MethodLoggingAdvice();
		ControlFlowPointcut cfPointCut = 
				new ControlFlowPointcut(MethodLoggingAdviceTest.class, "detour");
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(cfPointCut, advice);
		ProxyFactory fac = new ProxyFactory();		
		fac.setTarget(msgBean);
		fac.addAdvisor(advisor);
		MessageBean proxy = (MessageBean)fac.getProxy();
		
		System.out.println("====Test flowcontrol point=====");
		// no advice
		proxy.printBananas();
		// advice because the proxy method will be called from the "detour" method
		detour(proxy);
	}
	
	/**
	 * This will use a composable pointcut.
	 * Advice one specific method of a class if triggerd via the "detour" method of this class
	 * ==> only the printBananas method is advices if it is triggered via the detour method
	 */
	@Test
	public void testMethodLoggingAdviceWithCFPointCutAndMethodMatcher(){
		
		MessageBean msgBean = new MessageBean();
		MethodLoggingAdvice advice = new MethodLoggingAdvice();
		
		ControlFlowPointcut cfPointCut = 
				new ControlFlowPointcut(MethodLoggingAdviceTest.class, "detour2");
		NameMatchMethodPointcut pc = new NameMatchMethodPointcut();
		pc.addMethodName("printBananas");
		
		/*ComposablePointcut composablePointCut = new ComposablePointcut((Pointcut)pc);
		composablePointCut.intersection((Pointcut)cfPointCut);*/
		// when only using 2 pointcuts the Pointcuts util is easier then the composablePointCut class
		// Use the latter when more hten 2 pointcuts are combined.
		Pointcut composablePointCut = Pointcuts.intersection(pc, cfPointCut);
		
		DefaultPointcutAdvisor advisor = 
				new DefaultPointcutAdvisor(composablePointCut, advice);
		ProxyFactory fac = new ProxyFactory();		
		fac.setTarget(msgBean);
		fac.addAdvisor(advisor);
		MessageBean proxy = (MessageBean)fac.getProxy();
		
		System.out.println("====Test flowcontrol and composable point cut=====");
		// one with and one without advice
		detour2(proxy);
	}
	
	private void detour2(MessageBean proxy){
		proxy.printHello();
		proxy.printBananas();
	}
	
	private void detour(MessageBean proxy){
		proxy.printHello();
		proxy.printBananas();
	}
}
