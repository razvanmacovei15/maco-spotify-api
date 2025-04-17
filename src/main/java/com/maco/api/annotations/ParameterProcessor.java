package com.maco.api.annotations;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class ParameterProcessor {
    public static Map<String, String> processParameters(Method method, Object[] args) {
        Map<String, String> parameters = new HashMap<>();
        Parameter[] methodParameters = method.getParameters();

        for (int i = 0; i < methodParameters.length; i++) {
            Parameter param = methodParameters[i];
            Object arg = args[i];

            if (param.isAnnotationPresent(TimeRange.class)) {
                TimeRange period = param.getAnnotation(TimeRange.class);
                parameters.put("time_range", period.value().getValue());
            }
            // Add more parameter types here
        }

        return parameters;
    }
}