package com.wxxr.mobile.core.rpc.rest;
import com.wxxr.javax.ws.rs.client.ClientResponseContext;
import com.wxxr.javax.ws.rs.core.EntityTag;
import com.wxxr.javax.ws.rs.core.Link;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.javax.ws.rs.core.MultivaluedMap;
import com.wxxr.javax.ws.rs.core.NewCookie;
import com.wxxr.javax.ws.rs.core.Response;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public class ClientResponseContextImpl implements ClientResponseContext
{
   protected final ClientResponse response;

   public ClientResponseContextImpl(ClientResponse response)
   {
      this.response = response;
   }

   @Override
   public int getStatus()
   {
      return response.getStatus();
   }

   @Override
   public void setStatus(int code)
   {
      response.setStatus(code);
   }

   @Override
   public Response.StatusType getStatusInfo()
   {
      return response.getStatusInfo();
   }

   @Override
   public void setStatusInfo(Response.StatusType statusInfo)
   {
      response.setStatus(statusInfo.getStatusCode());
   }

   @Override
   public MultivaluedMap<String, String> getHeaders()
   {
      Object obj = response.getHeaders();
      return (MultivaluedMap<String, String>)obj;
   }

   @Override
   public Set<String> getAllowedMethods()
   {
      return response.getAllowedMethods();
   }

   @Override
   public Date getDate()
   {
      return response.getDate();
   }

   @Override
   public Locale getLanguage()
   {
      return response.getLanguage();
   }

   @Override
   public int getLength()
   {
      return response.getLength();
   }

   @Override
   public MediaType getMediaType()
   {
      return response.getMediaType();
   }

   @Override
   public Map<String, NewCookie> getCookies()
   {
      return response.getCookies();
   }

   @Override
   public EntityTag getEntityTag()
   {
      return response.getEntityTag();
   }

   @Override
   public Date getLastModified()
   {
      return response.getLastModified();
   }

   @Override
   public URI getLocation()
   {
      return response.getLocation();
   }

   @Override
   public Set<Link> getLinks()
   {
      return response.getLinks();
   }

   @Override
   public boolean hasLink(String relation)
   {
      return response.hasLink(relation);
   }

   @Override
   public Link getLink(String relation)
   {
      return response.getLink(relation);
   }

   @Override
   public Link.Builder getLinkBuilder(String relation)
   {
      return response.getLinkBuilder(relation);
   }

   @Override
   public boolean hasEntity()
   {
      return response.hasEntity();
   }

   @Override
   public InputStream getEntityStream()
   {
      return response.getInputStream();
   }

   @Override
   public void setEntityStream(InputStream entityStream)
   {
      response.setInputStream(entityStream);
   }

   @Override
   public String getHeaderString(String name)
   {
      return response.getHeaderString(name);
   }
}
