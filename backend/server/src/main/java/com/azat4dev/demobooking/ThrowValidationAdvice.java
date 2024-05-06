package com.azat4dev.demobooking;


import com.azat4dev.demobooking.common.DomainException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Aspect
@Component
public class ThrowValidationAdvice {

    public ThrowValidationAdvice() {
        System.out.println("ThrowValidationAdvice");
    }

//    @Around("execution(* com.azat4dev.demobooking..*(..))")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            return joinPoint.proceed();
//        } catch (DomainException e) {
//            // Handle the exception
//            System.out.println("Exception occurred: " + e.getMessage());
//
//            // Optionally, rethrow the exception
//            throw e;
//        }
//    }
}