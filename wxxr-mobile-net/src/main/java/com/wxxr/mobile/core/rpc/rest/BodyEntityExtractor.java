package com.wxxr.mobile.core.rpc.rest;
import com.wxxr.javax.ws.rs.core.GenericType;
import com.wxxr.javax.ws.rs.core.Response;

import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * BodyEntityExtractor extract body objects from responses. This ends up calling
 * the appropriate MessageBodyReader through a series of calls
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @author <a href="mailto:sduskis@gmail.com">Solomon Duskis</a>
 * @version $Revision$
 * @see org.jboss.resteasy.client.core.extractors.EntityExtractorFactory
 * @see com.wxxr.javax.ws.rs.ext.MessageBodyReader
 */
@SuppressWarnings("unchecked")
public class BodyEntityExtractor implements EntityExtractor
{
   private final Method method;

   public BodyEntityExtractor(Method method)
   {
      this.method = method;
   }

   public Object extractEntity(ClientContext context, Object... args)
   {
      Response response = context.getClientResponse();

      // only release connection if it is not an instance of an
      // InputStream
      boolean releaseConnectionAfter = response.getStatus() >=200 && response.getStatus() < 300;
      try
      {
         // void methods should be handled before this method gets called, but it's worth being defensive   
         if (method.getReturnType() == null)
         {
            throw new RuntimeException(
                    "No type information to extract entity with.  You use other getEntity() methods");
         }
         GenericType gt = null;
         if (method.getGenericReturnType() != null)
         {
            gt = new GenericType(method.getGenericReturnType());
         }
         else
         {
            gt = new GenericType(method.getReturnType());
         }
         Object obj = ClientInvocation.extractResult(gt, response, method.getAnnotations());
         if (obj instanceof InputStream)
            releaseConnectionAfter = false;
         return obj;
      }
      finally
      {
         if (releaseConnectionAfter)
            response.close();
      }
   }
}