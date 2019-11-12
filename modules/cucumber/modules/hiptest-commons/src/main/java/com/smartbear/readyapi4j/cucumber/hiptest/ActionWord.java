package com.smartbear.readyapi4j.cucumber.hiptest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionWord {
    String value();

    boolean addFreetext() default false;

    String description() default "";
}
