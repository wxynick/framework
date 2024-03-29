package com.wxxr.mobile.core.rpc.rest.spi;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public interface ValueInjector
{
   /**
    * Inject outside the context of an HTTP request.  For instance, a singleton may have proxiable and injectable
    * jax-rs objects like Request, UriInfo, or HttpHeaders.
    *
    * @return
    */
   Object inject();

   /**
    * Inject inside the context of an HTTP request.
    *
    * @param request
    * @param response
    * @return
    */
   Object inject(HttpRequest request, HttpResponse response);
}
