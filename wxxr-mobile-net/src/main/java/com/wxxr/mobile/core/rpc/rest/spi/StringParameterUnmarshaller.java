package com.wxxr.mobile.core.rpc.rest.spi;
import java.lang.annotation.Annotation;

/**
 * Similar to StringConverter except specific to a parameter injection only.  It is annotation sensitive.
 * <p/>
 * Instances of this class are created per parameter injection.
 * setAnnotations() is called when the object is instantiated
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 * @See StringConverter
 * @See org.jboss.resteasy.annotationsStringParameterUnmarshallerBinder
 */
public interface StringParameterUnmarshaller<T>
{
   void setAnnotations(Annotation[] annotations);

   T fromString(String str);
}
