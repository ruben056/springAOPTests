springAOPTest
===================

Simple test off spring AOP functionality.
The test use the ProxyFactory, meaning the advice is weaven programmatically.
When using spring normally you'll use the declarative (annotation or xml) way of defining aop config,
but doing so programmatically shows you how spring works internally when using aop.

How to use
==========
run mvn test
The maven tests don't actually perform any assertions, just look at the log and the comment in the code
to see what is going on ...
