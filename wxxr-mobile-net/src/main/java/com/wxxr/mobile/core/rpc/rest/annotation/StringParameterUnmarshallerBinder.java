package com.wxxr.mobile.core.rpc.rest.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wxxr.mobile.core.rpc.rest.spi.StringParameterUnmarshaller;

/**
 * Meta-annotation to be placed on another annotation that triggers a StringParameterUnmarshaller to be applied
 * to a string based annotation injector i.e. @HeaderParam, @PathParam, @QueryParam
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 * @see org.jboss.resteasy.spi.StringParameterUnmarshaller
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringParameterUnmarshallerBinder
{
   Class<? extends StringParameterUnmarshaller<?>> value();
}