package com.wxxr.mobile.core.rpc.rest;
import com.wxxr.javax.ws.rs.core.NewCookie;
import com.wxxr.javax.ws.rs.ext.RuntimeDelegate;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public class NewCookieHeaderDelegate implements RuntimeDelegate.HeaderDelegate
{
   public Object fromString(String newCookie) throws IllegalArgumentException
   {
      if (newCookie == null) throw new IllegalArgumentException("NewCookie value is null");

      String cookieName = null;
      String cookieValue = null;
      String comment = null;
      String domain = null;
      int maxAge = NewCookie.DEFAULT_MAX_AGE;
      String path = null;
      boolean secure = false;
      int version = NewCookie.DEFAULT_VERSION;
      boolean httpOnly = false;


      String parts[] = newCookie.split("[;,]");

      for (String part : parts)
      {
         String nv[] = part.split("=", 2);
         String name = nv.length > 0 ? nv[0].trim() : "";
         String value = nv.length > 1 ? nv[1].trim() : "";
         if (value.startsWith("\"") && value.endsWith("\"") && value.length() > 1)
            value = value.substring(1, value.length() - 1);

         if (name.equalsIgnoreCase("Comment"))
            comment = value;
         else if (name.equalsIgnoreCase("Domain"))
            domain = value;
         else if (name.equalsIgnoreCase("Max-Age"))
            maxAge = Integer.parseInt(value);
         else if (name.equalsIgnoreCase("Path"))
            path = value;
         else if (name.equalsIgnoreCase("Secure"))
            secure = true;
         else if (name.equalsIgnoreCase("Version"))
            version = Integer.parseInt(value);
         else if (name.equalsIgnoreCase("HttpOnly"))
            httpOnly = true;
         else
         {
            cookieName = name;
            cookieValue = value;
         }
      }

     return new NewCookie(cookieName, cookieValue, path, domain, version, comment, maxAge, null, secure, httpOnly);

   }

   protected void quote(StringBuilder b, String value)
   {
      if (value.indexOf(' ') > -1)
      {
         b.append('"');
         b.append(value);
         b.append('"');
      }
      else
      {
         b.append(value);
      }
   }

   public String toString(Object value)
   {
      if (value == null) throw new IllegalArgumentException("param was null");
      NewCookie cookie = (NewCookie) value;
      StringBuilder b = new StringBuilder();

      b.append(cookie.getName()).append('=');
      quote(b, cookie.getValue());

      b.append(";").append("Version=").append(cookie.getVersion());

      if (cookie.getComment() != null)
      {
         b.append(";Comment=");
         quote(b, cookie.getComment());
      }
      if (cookie.getDomain() != null)
      {
         b.append(";Domain=");
         quote(b, cookie.getDomain());
      }
      if (cookie.getPath() != null)
      {
         b.append(";Path=");
         quote(b, cookie.getPath());
      }
      if (cookie.getMaxAge() != -1)
      {
         b.append(";Max-Age=");
         b.append(cookie.getMaxAge());
      }
      if (cookie.isSecure())
         b.append(";Secure");
      if (cookie.isHttpOnly())
         b.append(";HttpOnly");
      return b.toString();
   }
}