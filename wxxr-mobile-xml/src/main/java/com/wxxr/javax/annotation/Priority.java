package com.wxxr.javax.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Replaces @Priority from previous spec version.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Priority
{
   int value();
}