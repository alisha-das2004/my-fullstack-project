package com.example.backend.aspect;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
     

@Around("execution(* com.example.backend.service.UserServiceImpl.register(..))")

	public  Object logRegistration(Joinpoint joinPoint) throws Throwable {
		 
		long startTime = System.currentTimeMillis();
		System.out.println("Customer Registration Started");
		try {
			Object result = joinPoint.proceed();
			System.out.println("Customer registration completed successfully");
			return result;
		} catch (Throwable exception) {
			System.out.println("Customer registration failed");
			throw exception;
		}finally {
			long endTime = System.currentTimeMillis();
			long executionTime = endTime-startTime;
			System.out.println("Registration execution time: "
                    + executionTime + " ms");
		}
	}
}
