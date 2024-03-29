package com.wxxr.mobile.core.rpc.rest.annotation;
import com.wxxr.javax.ws.rs.core.MediaType;

import java.lang.annotation.Annotation;

/**
 * Part of a generic decorator framework.
 * <p/>
 * Decorate a target.  For example, decorate a JAXB Marshaller with property values.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public interface DecoratorProcessor<T, A extends Annotation>
{
   /**
    * @param target      i.e. a Marshaller instance
    * @param annotation  the annotation that triggered the decorator
    * @param type
    * @param annotations
    * @param mediaType
    * @return should never return null
    */
   T decorate(T target, A annotation, Class type, Annotation[] annotations, MediaType mediaType);
}
