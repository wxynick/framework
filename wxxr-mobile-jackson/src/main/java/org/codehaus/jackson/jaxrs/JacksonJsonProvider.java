/**
 * 
 */
package org.codehaus.jackson.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.VersionUtil;

import com.wxxr.javax.ws.rs.core.Context;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.javax.ws.rs.core.MultivaluedMap;
import com.wxxr.javax.ws.rs.core.StreamingOutput;
import com.wxxr.javax.ws.rs.ext.ContextResolver;
import com.wxxr.javax.ws.rs.ext.MessageBodyReader;
import com.wxxr.javax.ws.rs.ext.MessageBodyWriter;
import com.wxxr.javax.ws.rs.ext.Providers;
import com.wxxr.mobile.core.rpc.api.Response;

/**
 * @author wangyan
 *
 */
public class JacksonJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
	

    /**
     * Default annotation sets to use, if not explicitly defined during
     * construction: only Jackson annotations are used for the base
     * class. Sub-classes can use other settings.
     */
    public final static Annotations[] BASIC_ANNOTATIONS = {
        Annotations.JACKSON
    };

    /**
     * Looks like we need to worry about accidental
     *   data binding for types we shouldn't be handling. This is
     *   probably not a very good way to do it, but let's start by
     *   blacklisting things we are not to handle.
     *<p>
     *  (why ClassKey? since plain old Class has no hashCode() defined,
     *  lookups are painfully slow)
     */
    public final static HashSet<ClassKey> _untouchables = new HashSet<ClassKey>();
    static {
        // First, I/O things (direct matches)
        _untouchables.add(new ClassKey(java.io.InputStream.class));
        _untouchables.add(new ClassKey(java.io.Reader.class));
        _untouchables.add(new ClassKey(java.io.OutputStream.class));
        _untouchables.add(new ClassKey(java.io.Writer.class));

        // then some primitive types
        _untouchables.add(new ClassKey(byte[].class));
        _untouchables.add(new ClassKey(char[].class));
        // 24-Apr-2009, tatu: String is an edge case... let's leave it out
        _untouchables.add(new ClassKey(String.class));

        // Then core JAX-RS things
        _untouchables.add(new ClassKey(StreamingOutput.class));
        _untouchables.add(new ClassKey(Response.class));
    }

    /**
     * These are classes that we never use for reading
     * (never try to deserialize instances of these types).
     */
    public final static Class<?>[] _unreadableClasses = new Class<?>[] {
        InputStream.class, Reader.class
    };

    /**
     * These are classes that we never use for writing
     * (never try to serialize instances of these types).
     */
    public final static Class<?>[] _unwritableClasses = new Class<?>[] {
        OutputStream.class, Writer.class,
        StreamingOutput.class, Response.class
    };

    /**
     * Helper object used for encapsulating configuration aspects
     * of {@link ObjectMapper}
     */
    protected final MapperConfigurator _mapperConfig;

    /**
     * Set of types (classes) that provider should ignore for data binding
     * 
     * @since 1.5
     */
    protected HashSet<ClassKey> _cfgCustomUntouchables;

    /**
     * JSONP function name to use for automatic JSONP wrapping, if any;
     * if null, no JSONP wrapping is done.
     */
    protected String _jsonpFunctionName;
    
    /*
    /**********************************************************
    /* Context configuration
    /**********************************************************
     */

    /**
     * Injectable context object used to locate configured
     * instance of {@link ObjectMapper} to use for actual
     * serialization.
     */
    @Context
    protected Providers _providers;

    /*
    /**********************************************************
    /* Configuration
    /**********************************************************
     */

    /**
     * Whether we want to actually check that Jackson has
     * a serializer for given type. Since this should generally
     * be the case (due to auto-discovery) and since the call
     * to check availability can be bit expensive, defaults to false.
     */
    protected boolean _cfgCheckCanSerialize = false;

    /**
     * Whether we want to actually check that Jackson has
     * a deserializer for given type. Since this should generally
     * be the case (due to auto-discovery) and since the call
     * to check availability can be bit expensive, defaults to false.
     */
    protected boolean _cfgCheckCanDeserialize = false;

    /*
    /**********************************************************
    /* Construction
    /**********************************************************
     */

    /**
     * Default constructor, usually used when provider is automatically
     * configured to be used with JAX-RS implementation.
     */
    public JacksonJsonProvider()
    {
        this(null, BASIC_ANNOTATIONS);
    }

    /**
     * @param annotationsToUse Annotation set(s) to use for configuring
     *    data binding
     */
    public JacksonJsonProvider(Annotations... annotationsToUse)
    {
        this(null, annotationsToUse);
    }

    public JacksonJsonProvider(ObjectMapper mapper)
    {
        this(mapper, BASIC_ANNOTATIONS);
    }
    
    /**
     * Constructor to use when a custom mapper (usually components
     * like serializer/deserializer factories that have been configured)
     * is to be used.
     */
    public JacksonJsonProvider(ObjectMapper mapper, Annotations[] annotationsToUse)
    {
        _mapperConfig = new MapperConfigurator(mapper, annotationsToUse);
    }

    /**
     * Method that will return version information stored in and read from jar
     * that contains this class.
     * 
     * @since 1.6
     */
    public Version version() {
        return VersionUtil.versionFor(getClass());
    }
    
    /*
    /**********************************************************
    /* Configuring
    /**********************************************************
     */

    /**
     * Method for defining whether actual detection for existence of
     * a deserializer for type should be done when {@link #isReadable}
     * is called.
     */
    public void checkCanDeserialize(boolean state) { _cfgCheckCanDeserialize = state; }

    /**
     * Method for defining whether actual detection for existence of
     * a serializer for type should be done when {@link #isWriteable}
     * is called.
     */
    public void checkCanSerialize(boolean state) { _cfgCheckCanSerialize = state; }

    /**
     * Method for configuring which annotation sets to use (including none).
     * Annotation sets are defined in order decreasing precedence; that is,
     * first one has the priority over following ones.
     * 
     * @param annotationsToUse Ordered list of annotation sets to use; if null,
     *    default
     */
    public void setAnnotationsToUse(Annotations[] annotationsToUse) {
        _mapperConfig.setAnnotationsToUse(annotationsToUse);
    }
    
    /**
     * Method that can be used to directly define {@link ObjectMapper} to use
     * for serialization and deserialization; if null, will use the standard
     * provider discovery from context instead. Default setting is null.
     */
    public void setMapper(ObjectMapper m) {
        _mapperConfig.setMapper(m);
    }

    public JacksonJsonProvider configure(DeserializationConfig.Feature f, boolean state) {
        _mapperConfig.configure(f, state);
        return this;
    }

    public JacksonJsonProvider configure(SerializationConfig.Feature f, boolean state) {
        _mapperConfig.configure(f, state);
        return this;
    }

    public JacksonJsonProvider configure(JsonParser.Feature f, boolean state) {
        _mapperConfig.configure(f, state);
        return this;
    }

    public JacksonJsonProvider configure(JsonGenerator.Feature f, boolean state) {
        _mapperConfig.configure(f, state);
        return this;
    }

    public JacksonJsonProvider enable(DeserializationConfig.Feature f, boolean state) {
        _mapperConfig.configure(f, true);
        return this;
    }

    public JacksonJsonProvider enable(SerializationConfig.Feature f, boolean state) {
        _mapperConfig.configure(f, true);
        return this;
    }

    public JacksonJsonProvider enable(JsonParser.Feature f, boolean state) {
        _mapperConfig.configure(f, true);
        return this;
    }

    public JacksonJsonProvider enable(JsonGenerator.Feature f, boolean state) {
        _mapperConfig.configure(f, true);
        return this;
    }

    public JacksonJsonProvider disable(DeserializationConfig.Feature f, boolean state) {
        _mapperConfig.configure(f, false);
        return this;
    }

    public JacksonJsonProvider disable(SerializationConfig.Feature f, boolean state) {
        _mapperConfig.configure(f, false);
        return this;
    }

    public JacksonJsonProvider disable(JsonParser.Feature f, boolean state) {
        _mapperConfig.configure(f, false);
        return this;
    }

    public JacksonJsonProvider disable(JsonGenerator.Feature f, boolean state) {
        _mapperConfig.configure(f, false);
        return this;
    }

    /**
     * Method for marking specified type as "untouchable", meaning that provider
     * will not try to read or write values of this type (or its subtypes).
     * 
     * @param type Type to consider untouchable; can be any kind of class,
     *   including abstract class or interface. No instance of this type
     *   (including subtypes, i.e. types assignable to this type) will
     *   be read or written by provider
     * 
     * @since 1.5
     */
    public void addUntouchable(Class<?> type)
    {
        if (_cfgCustomUntouchables == null) {
            _cfgCustomUntouchables = new HashSet<ClassKey>();
        }
        _cfgCustomUntouchables.add(new ClassKey(type));
    }

    public void setJSONPFunctionName(String fname) {
    	this._jsonpFunctionName = fname;
    }
    
    /*
    /**********************************************************
    /* MessageBodyReader impl
    /**********************************************************
     */

    /**
     * Method that JAX-RS container calls to try to check whether
     * values of given type (and media type) can be deserialized by
     * this provider.
     * Implementation will first check that expected media type is
     * a JSON type (via call to {@link #isJsonType}; then verify
     * that type is not one of "untouchable" types (types we will never
     * automatically handle), and finally that there is a deserializer
     * for type (iff {@link #checkCanDeserialize} has been called with
     * true argument -- otherwise assumption is there will be a handler)
     */
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        if (!isJsonType(mediaType)) {
            return false;
        }

        /* Ok: looks like we must weed out some core types here; ones that
         * make no sense to try to bind from JSON:
         */
        if (_untouchables.contains(new ClassKey(type))) {
            return false;
        }
        // and there are some other abstract/interface types to exclude too:
        for (Class<?> cls : _unreadableClasses) {
            if (cls.isAssignableFrom(type)) {
                return false;
            }
        }
        // as well as possible custom exclusions
        if (_containedIn(type, _cfgCustomUntouchables)) {
            return false;
        }

        // Finally: if we really want to verify that we can serialize, we'll check:
        if (_cfgCheckCanSerialize) {
            if (!locateMapper(type, mediaType).canDeserialize(_convertType(type))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Method that JAX-RS container calls to deserialize given
     * value.
     */
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String,String> httpHeaders, InputStream entityStream) 
        throws IOException
    {
        ObjectMapper mapper = locateMapper(type, mediaType);
        JsonParser jp = mapper.getJsonFactory().createJsonParser(entityStream);
        /* Important: we are NOT to close the underlying stream after
         * mapping, so we need to instruct parser:
         */
        jp.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        return mapper.readValue(jp, _convertType(genericType));
    }

    /*
    /**********************************************************
    /* MessageBodyWriter impl
    /**********************************************************
     */

    /**
     * Method that JAX-RS container calls to try to figure out
     * serialized length of given value. Since computation of
     * this length is about as expensive as serialization itself,
     * implementation will return -1 to denote "not known", so
     * that container will determine length from actual serialized
     * output (if needed).
     */
    public long getSize(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        /* In general figuring output size requires actual writing; usually not
         * worth it to write everything twice.
         */
        return -1;
    }

    /**
     * Method that JAX-RS container calls to try to check whether
     * given value (of specified type) can be serialized by
     * this provider.
     * Implementation will first check that expected media type is
     * a JSON type (via call to {@link #isJsonType}; then verify
     * that type is not one of "untouchable" types (types we will never
     * automatically handle), and finally that there is a serializer
     * for type (iff {@link #checkCanSerialize} has been called with
     * true argument -- otherwise assumption is there will be a handler)
     */
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        if (!isJsonType(mediaType)) {
            return false;
        }

        /* Ok: looks like we must weed out some core types here; ones that
         * make no sense to try to bind from JSON:
         */
        if (_untouchables.contains(new ClassKey(type))) {
            return false;
        }
        // but some are interface/abstract classes, so
        for (Class<?> cls : _unwritableClasses) {
            if (cls.isAssignableFrom(type)) {
                return false;
            }
        }
        // and finally, may have additional custom types to exclude
        if (_containedIn(type, _cfgCustomUntouchables)) {
            return false;
        }

        // Also: if we really want to verify that we can deserialize, we'll check:
        if (_cfgCheckCanSerialize) {
            if (!locateMapper(type, mediaType).canSerialize(type)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that JAX-RS container calls to serialize given
     * value.
     */
    public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String,Object> httpHeaders, OutputStream entityStream) 
        throws IOException
    {
        /* 27-Feb-2009, tatu: Where can we find desired encoding? Within
         *   HTTP headers?
         */
        ObjectMapper mapper = locateMapper(type, mediaType);
        JsonGenerator jg = mapper.getJsonFactory().createJsonGenerator(entityStream, JsonEncoding.UTF8);
        jg.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);

        // Want indentation?
        if (mapper.getSerializationConfig().isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
            jg.useDefaultPrettyPrinter();
        }
        // 04-Mar-2010, tatu: How about type we were given? (if any)
        JavaType rootType = null;
        
        if (genericType != null && value != null) {
            if (genericType != value.getClass()) {
                rootType = TypeFactory.type(genericType);
            }
        }
        // [JACKSON-245] Allow automatic JSONP wrapping
        if (_jsonpFunctionName != null) {
            mapper.writeValue(jg, new JSONPObject(_jsonpFunctionName, value, rootType));
        } else {
            // !!! TODO: 04-Mar-2010, tatus: As per [JACKSON-195], use root type!
            mapper.writeValue(jg, value);
        }
    }

    /*
    /**********************************************************
    /* Public helper methods
    /**********************************************************
     */

    /**
     * Helper method used to check whether given media type
     * is JSON type or sub type.
     * Current implementation essentially checks to see whether
     * {@link MediaType#getSubtype} returns "json" or something
     * ending with "+json".
     */
    protected boolean isJsonType(MediaType mediaType)
    {
        /* As suggested by Stephen D, there are 2 ways to check: either
         * being as inclusive as possible (if subtype is "json"), or
         * exclusive (major type "application", minor type "json").
         * Let's start with inclusive one, hard to know which major
         * types we should cover aside from "application".
         */
        if (mediaType != null) {
            // Ok: there are also "xxx+json" subtypes, which count as well
            String subtype = mediaType.getSubtype();
            return "json".equalsIgnoreCase(subtype) || subtype.endsWith("+json");
        }
        /* Not sure if this can happen; but it seems reasonable
         * that we can at least produce json without media type?
         */
        return true;
    }

    /**
     * Method called to locate {@link ObjectMapper} to use for serialization
     * and deserialization. If an instance has been explicitly defined by
     * {@link #setMapper} (or non-null instance passed in constructor), that
     * will be used. 
     * If not, will try to locate it using standard JAX-RS
     * {@link ContextResolver} mechanism, if it has been properly configured
     * to access it (by JAX-RS runtime).
     * Finally, if no mapper is found, will return a default unconfigured
     * {@link ObjectMapper} instance (one constructed with default constructor
     * and not modified in any way)
     *
     * @param type Class of object being serialized or deserialized;
     *   not checked at this point, since it is assumed that unprocessable
     *   classes have been already weeded out,
     *   but will be passed to {@link ContextResolver} as is.
     * @param mediaType Declared media type for the instance to process:
     *   not used by this method,
     *   but will be passed to {@link ContextResolver} as is.
     */
    public ObjectMapper locateMapper(Class<?> type, MediaType mediaType)
    {
        // First: were we configured with a specific instance?
        ObjectMapper m = _mapperConfig.getConfiguredMapper();
        if (m == null) {
            // If not, maybe we can get one configured via context?
            if (_providers != null) {
                ContextResolver<ObjectMapper> resolver = _providers.getContextResolver(ObjectMapper.class, mediaType);
                /* Above should work as is, but due to this bug
                 *   [https://jersey.dev.java.net/issues/show_bug.cgi?id=288]
                 * in Jersey, it doesn't. But this works until resolution of
                 * the issue:
                 */
                if (resolver == null) {
                    resolver = _providers.getContextResolver(ObjectMapper.class, null);
                }
                if (resolver != null) {
                    m = resolver.getContext(type);
                }
            }
            if (m == null) {
                // If not, let's get the fallback default instance
                m = _mapperConfig.getDefaultMapper();
            }
        }
        return m;
    }

    /*
    /**********************************************************
    /* Private/sub-class helper methods
    /**********************************************************
     */

    /**
     * Method used to construct a JDK generic type into type definition
     * Jackson understands.
     */
    protected JavaType _convertType(Type jdkType)
    {
        return TypeFactory.type(jdkType);
    }

    protected static boolean _containedIn(Class<?> mainType, HashSet<ClassKey> set)
    {
        if (set != null) {
            ClassKey key = new ClassKey(mainType);
            // First: type itself?
            if (set.contains(key)) return true;
            // Then supertypes (note: will not contain Object.class)
            for (Class<?> cls : ClassUtil.findSuperTypes(mainType, null)) {
                key.reset(cls);
                if (set.contains(key)) return true;
            }
        }
        return false;
    }

}
