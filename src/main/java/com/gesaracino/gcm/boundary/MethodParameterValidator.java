package com.gesaracino.gcm.boundary;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Gerardo Saracino on 05/11/2014.
 */
public class MethodParameterValidator {
    @Resource
    private Validator validator;

    @AroundInvoke
    public Object validate(InvocationContext invocationcontext) throws Exception {
        Object[] params = invocationcontext.getParameters();
        Annotation[][] parameterAnnotations = invocationcontext.getMethod().getParameterAnnotations();

        for(int i = 0; i < parameterAnnotations.length; i ++) {
            for(Annotation annotation : parameterAnnotations[i]) {
                Set violations = validator.validate(params[i]);

                if(isABeanValidationAnnotation(annotation) && violations.size() > 0) {
                    throw new ConstraintViolationException(violations);
                }
            }
        }

        return invocationcontext.proceed();
    }

    private static boolean isABeanValidationAnnotation(Annotation annotation) {
        return annotation instanceof Valid ||
                annotation instanceof AssertFalse ||
                annotation instanceof AssertTrue ||
                annotation instanceof DecimalMax ||
                annotation instanceof DecimalMin ||
                annotation instanceof Digits ||
                annotation instanceof Future ||
                annotation instanceof Max ||
                annotation instanceof Min ||
                annotation instanceof NotNull ||
                annotation instanceof Null ||
                annotation instanceof Past ||
                annotation instanceof Pattern ||
                annotation instanceof Size;
    }
}
