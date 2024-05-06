package com.azat4dev.demobooking;

import com.azat4dev.demobooking.common.DomainException;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ThrowValidations.class)
public @interface ThrowValidation {
    Class<? extends DomainException>[] forException() default DomainException.class;

    String value();

    int statusCode() default 400;
}