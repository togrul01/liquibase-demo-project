package org.company.springliquibase.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Annotation only applies to methods
@Retention(RetentionPolicy.RUNTIME) // Retain at runtime for reflection
public @interface Loggable {
    String action() default ""; // Action to log
}
