/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author neillin
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
@Documented
public @interface DomainModel {
	String value();
}
