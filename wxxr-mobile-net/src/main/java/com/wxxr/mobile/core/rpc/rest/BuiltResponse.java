package com.wxxr.mobile.core.rpc.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.wxxr.javax.ws.rs.core.EntityTag;
import com.wxxr.javax.ws.rs.core.GenericEntity;
import com.wxxr.javax.ws.rs.core.GenericType;
import com.wxxr.javax.ws.rs.core.HttpHeaders;
import com.wxxr.javax.ws.rs.core.Link;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.javax.ws.rs.core.MultivaluedMap;
import com.wxxr.javax.ws.rs.core.NewCookie;
import com.wxxr.javax.ws.rs.core.Response;
import com.wxxr.javax.ws.rs.ext.RuntimeDelegate;
import com.wxxr.mobile.core.rpc.http.api.HttpStatus;
import com.wxxr.mobile.core.rpc.util.CaseInsensitiveMap;
import com.wxxr.mobile.core.util.DateUtil;

/**
 * A response object not attached to a client or server invocation.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public class BuiltResponse extends Response
{
   protected Object entity;
   protected int status = HttpStatus.SC_OK;
   protected Headers<Object> metadata = new Headers<Object>();
   protected Annotation[] annotations;
   protected Class entityClass;
   protected Type genericType;
   protected HeaderValueProcessor processor;
   protected boolean isClosed;

   public BuiltResponse()
   {
   }

   public BuiltResponse(int status, Headers<Object> metadata, Object entity, Annotation[] entityAnnotations)
   {
      setEntity(entity);
      this.status = status;
      this.metadata = metadata;
      this.annotations = entityAnnotations;
   }

   public Class getEntityClass()
   {
      return entityClass;
   }

   public void setEntityClass(Class entityClass)
   {
      this.entityClass = entityClass;
   }

   protected HeaderValueProcessor getHeaderValueProcessor()
   {
      if (processor != null) return processor;
      return ResteasyProviderFactory.getInstance();
   }

   @Override
   public Object getEntity()
   {
      abortIfClosed();
      return entity;
   }

   @Override
   public int getStatus()
   {
      return status;
   }

   @Override
   public StatusType getStatusInfo()
   {
      StatusType statusType = Status.fromStatusCode(status);
      if (statusType == null)
      {
         statusType = new StatusType()
         {
            @Override
            public int getStatusCode()
            {
               return status;
            }

            @Override
            public Status.Family getFamily()
            {
               return Status.Family.OTHER;
            }

            @Override
            public String getReasonPhrase()
            {
               return "Unknown Code";
            }
         };
      }
      return statusType;
   }

   @Override
   public MultivaluedMap<String, Object> getMetadata()
   {
      return metadata;
   }

   public void setEntity(Object entity)
   {
      if (entity == null)
      {
         this.entity = null;
         this.genericType = null;
         this.entityClass = null;
      }
      else if (entity instanceof GenericEntity)
      {

         GenericEntity ge = (GenericEntity) entity;
         this.entity = ge.getEntity();
         this.genericType = ge.getType();
         this.entityClass = ge.getRawType();
      }
      else
      {
         this.entity = entity;
         this.entityClass = entity.getClass();
         this.genericType = null;
      }
   }

   public void setStatus(int status)
   {
      this.status = status;
   }

   public void setMetadata(MultivaluedMap<String, Object> metadata)
   {
      this.metadata = new Headers<Object>();
      this.metadata.putAll(metadata);
   }

   public Annotation[] getAnnotations()
   {
      return annotations;
   }

   public void setAnnotations(Annotation[] annotations)
   {
      this.annotations = annotations;
   }

   public Type getGenericType()
   {
      return genericType;
   }

   public void setGenericType(Type genericType)
   {
      this.genericType = genericType;
   }

   @Override
   public <T> T readEntity(Class<T> type, Annotation[] annotations)
   {
      return readEntity(type, null, annotations);
   }

   @Override
   public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations)
   {
      return readEntity((Class<T>) entityType.getRawType(), entityType.getType(), annotations);
   }

   @Override
   public <T> T readEntity(Class<T> type)
   {
      return readEntity(type, null, null);
   }

   @Override
   public <T> T readEntity(GenericType<T> entityType)
   {
      return readEntity((Class<T>) entityType.getRawType(), entityType.getType(), null);
   }

   public <T> T readEntity(Class<T> type, Type genericType, Annotation[] anns)
   {
      throw new IllegalStateException("Entity is not backed by an input stream");
   }


   @Override
   public boolean hasEntity()
   {
      abortIfClosed();
      return entity != null;
   }

   @Override
   public boolean bufferEntity()
   {
      abortIfClosed();
      // no-op
      return false;
   }

   public boolean isClosed()
   {
      return isClosed;
   }

   public void abortIfClosed()
   {
      if (isClosed()) throw new IllegalStateException("Response is closed.");
   }

   @Override
   public void close()
   {
      isClosed = true;
   }

   @Override
   public Locale getLanguage()
   {
      Object obj = metadata.getFirst(HttpHeaders.CONTENT_LANGUAGE);
      if (obj == null) return null;
      if (obj instanceof Locale) return (Locale) obj;
      return new Locale(obj.toString());
   }

   @Override
   public int getLength()
   {
      Object obj = metadata.getFirst(HttpHeaders.CONTENT_LENGTH);
      if (obj == null) return -1;
      if (obj instanceof Integer) return (Integer) obj;
      return Integer.valueOf(toHeaderString(obj));
   }

   @Override
   public MediaType getMediaType()
   {
      Object obj = metadata.getFirst(HttpHeaders.CONTENT_TYPE);
      if (obj instanceof MediaType) return (MediaType) obj;
      if (obj == null) return null;
      return MediaType.valueOf(toHeaderString(obj));
   }

   @Override
   public Map<String, NewCookie> getCookies()
   {
      Map<String, NewCookie> cookies = new HashMap<String, NewCookie>();
      List list = metadata.get(HttpHeaders.SET_COOKIE);
      if (list == null) return cookies;
      for (Object obj : list)
      {
         if (obj instanceof NewCookie)
         {
            NewCookie cookie = (NewCookie)obj;
            cookies.put(cookie.getName(), cookie);
         }
         else
         {
            String str = toHeaderString(obj);
            NewCookie cookie = NewCookie.valueOf(str);
            cookies.put(cookie.getName(), cookie);
         }
      }
      return cookies;
   }

   @Override
   public EntityTag getEntityTag()
   {
      Object d = metadata.getFirst(HttpHeaders.ETAG);
      if (d == null) return null;
      if (d instanceof EntityTag) return (EntityTag) d;
      return EntityTag.valueOf(toHeaderString(d));
   }

   @Override
   public Date getDate()
   {
      Object d = metadata.getFirst(HttpHeaders.DATE);
      if (d == null) return null;
      if (d instanceof Date) return (Date) d;
      return DateUtil.parseDate(d.toString());
   }

   @Override
   public Date getLastModified()
   {
      Object d = metadata.getFirst(HttpHeaders.LAST_MODIFIED);
      if (d == null) return null;
      if (d instanceof Date) return (Date) d;
      return DateUtil.parseDate(d.toString());
   }

   @Override
   public Set<String> getAllowedMethods()
   {
      Set<String> allowedMethods = new HashSet<String>();
      List<Object> allowed = metadata.get("Allow");
      if (allowed == null) return allowedMethods;
      for (Object header : allowed)
      {
         allowedMethods.add(toHeaderString(header).toUpperCase());
      }

      return allowedMethods;
   }

   protected String toHeaderString(Object header)
   {
      if (header instanceof String) return (String)header;
      //if (getHeaderValueProcessor() != null) return getHeaderValueProcessor().toHeaderString(header);
      // Javadoc and TCK requires that you only get from RuntimeDelegate.getInstance().createHeaderDelegate()
      RuntimeDelegate.HeaderDelegate delegate = RuntimeDelegate.getInstance().createHeaderDelegate(header.getClass());
      if (delegate != null)
         return delegate.toString(header);
      else
         return header.toString();
   }

   @Override
   public MultivaluedMap<String, String> getStringHeaders()
   {
      CaseInsensitiveMap<String> map = new CaseInsensitiveMap<String>();
      for (Map.Entry<String, List<Object>> entry : metadata.entrySet())
      {
         for (Object obj : entry.getValue())
         {
            map.add(entry.getKey(), toHeaderString(obj));
         }
      }
      return map;
   }

   @Override
   public String getHeaderString(String name)
   {
      List vals = metadata.get(name);
      if (vals == null) return null;
      StringBuilder builder = new StringBuilder();
      boolean first = true;
      for (Object val : vals)
      {
         if (first) first = false;
         else builder.append(",");
         val = toHeaderString(val);
         if (val == null) val = "";
         builder.append(val);
      }
      return builder.toString();
   }

   @Override
   public URI getLocation()
   {
      Object uri = metadata.getFirst(HttpHeaders.LOCATION);
      if (uri == null) return null;
      if (uri instanceof URI) return (URI)uri;
      String str = null;
      if (uri instanceof String) str = (String)uri;
      else str = toHeaderString(uri);
      return URI.create(str);
   }

   @Override
   public Set<Link> getLinks()
   {
      LinkHeaders linkHeaders = getLinkHeaders();
      Set<Link> links = new HashSet<Link>();
      links.addAll(linkHeaders.getLinks());
      return links;
   }

   protected LinkHeaders getLinkHeaders()
   {
      LinkHeaders linkHeaders = new LinkHeaders();
      linkHeaders.addLinkObjects(metadata, getHeaderValueProcessor());
      return linkHeaders;
   }

   @Override
   public boolean hasLink(String relation)
   {
      return getLinkHeaders().getLinkByRelationship(relation) != null;
   }

   @Override
   public Link getLink(String relation)
   {
      return getLinkHeaders().getLinkByRelationship(relation);
   }

   @Override
   public Link.Builder getLinkBuilder(String relation)
   {
      Link link = getLinkHeaders().getLinkByRelationship(relation);
      if (link == null) return null;
      return Link.fromLink(link);
   }

}
