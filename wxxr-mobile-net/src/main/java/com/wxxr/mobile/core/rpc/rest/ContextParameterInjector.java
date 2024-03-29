package com.wxxr.mobile.core.rpc.rest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


import com.wxxr.javax.ws.rs.core.Application;
import com.wxxr.javax.ws.rs.ext.Providers;
import com.wxxr.mobile.core.rpc.rest.spi.ApplicationException;
import com.wxxr.mobile.core.rpc.rest.spi.HttpRequest;
import com.wxxr.mobile.core.rpc.rest.spi.HttpResponse;
import com.wxxr.mobile.core.rpc.rest.spi.ValueInjector;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public class ContextParameterInjector implements ValueInjector
{
   private Class type;
   private Class proxy;
   private ResteasyProviderFactory factory;

   public ContextParameterInjector(Class proxy, Class type, ResteasyProviderFactory factory)
   {
      this.type = type;
      this.proxy = proxy;
      this.factory = factory;
   }

   public Object inject(HttpRequest request, HttpResponse response)
   {
      // we always inject a proxy for interface types just in case the per-request target is a pooled object
      // i.e. in the case of an SLSB
      if (type.equals(Providers.class)) return factory;
      if (!type.isInterface())
      {
         return ResteasyProviderFactory.getContextData(type);
      }
      return createProxy();
   }

   private class GenericDelegatingProxy implements InvocationHandler
   {
      public Object invoke(Object o, Method method, Object[] objects) throws Throwable
      {
         try
         {
            Object delegate = ResteasyProviderFactory.getContextData(type);
            if (delegate == null)
               throw new LoggableFailure("Unable to find contextual data of type: " + type.getName());
            return method.invoke(delegate, objects);
         }
         catch (IllegalAccessException e)
         {
            throw new RuntimeException(e);
         }
         catch (IllegalArgumentException e)
         {
            throw new RuntimeException(e);
         }
         catch (InvocationTargetException e)
         {
            throw new ApplicationException(e.getCause());
         }
      }
   }

   public Object inject()
   {
      //if (type.equals(Providers.class)) return factory;
      if (type.equals(Application.class))
      {
         return ResteasyProviderFactory.getContextData(Application.class);
      }
      else if (!type.isInterface())
      {
         Object delegate = ResteasyProviderFactory.getContextData(type);
         if (delegate != null) return delegate;
         throw new RuntimeException("Illegal to inject a non-interface type into a singleton");
      }

      return createProxy();
   }

   protected Object createProxy()
   {
      if (proxy != null)
      {
         try
         {
            return proxy.getConstructors()[0].newInstance(new GenericDelegatingProxy());
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
      }
      else
      {
         Class[] intfs = {type};
         return Proxy.newProxyInstance(type.getClassLoader(), intfs, new GenericDelegatingProxy());
      }
   }
}