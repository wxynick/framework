/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author neillin
 *
 */
@Retention(RetentionPolicy.CLASS)
@Documented
@PresentationModel
public @interface Parameter {
	String name();
	String value();
	ValueType type() default ValueType.STRING;
}
