package com.maco.api.annotations;

import com.maco.api.TimePeriod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeRange {
    TimePeriod value() default TimePeriod.MEDIUM_TERM;
}
