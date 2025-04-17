package com.maco.service;

import com.maco.api.TimePeriod;
import com.maco.api.annotations.ParameterProcessor;
import com.maco.api.annotations.TimeRange;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class AnnotatedServiceInvoker {
    public static <T> T invokeAnnotatedMethod(Object service, Class<? extends T> returnType) 
            throws InvocationTargetException, IllegalAccessException {
        Method method = findAnnotatedMethod(service.getClass());
        if (method == null) {
            throw new IllegalStateException("No method with @TimeRange annotation found");
        }

        // Create parameters for the method
        Object[] args = createDefaultParameters(method);
        
        // Process parameters using ParameterProcessor
        Map<String, String> processedParams = ParameterProcessor.processParameters(method, args);
        
        // Invoke the method with the processed parameters
        return returnType.cast(method.invoke(service, args));
    }

    private static Method findAnnotatedMethod(Class<?> serviceClass) {
        for (Method method : serviceClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(TimeRange.class)) {
                return method;
            }
        }
        return null;
    }

    private static Object[] createDefaultParameters(Method method) {
        TimeRange timeRange = method.getAnnotation(TimeRange.class);
        TimePeriod timePeriod = timeRange != null ? timeRange.value() : TimePeriod.MEDIUM_TERM;
        
        return new Object[] {
            timePeriod,  // Use the annotation's value for timePeriod
            50,         // limit
            0           // offset
        };
    }
} 