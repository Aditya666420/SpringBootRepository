package com.example.employee.aop;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before(
      "execution(* com.employee.service.*.*(..))")
    public void beforeMethod(
            JoinPoint joinPoint) {

        System.out.println(
                "Entering Method : "
                        + joinPoint.getSignature()
                                  .getName());
    }

    @AfterReturning(
      pointcut =
      "execution(* com.employee.service.*.*(..))",
      returning = "result")
    public void afterMethod(
            JoinPoint joinPoint,
            Object result) {

        System.out.println(
                "Method Executed : "
                        + joinPoint.getSignature()
                                  .getName());

        System.out.println(
                "Returned : "
                        + result);
    }
}
