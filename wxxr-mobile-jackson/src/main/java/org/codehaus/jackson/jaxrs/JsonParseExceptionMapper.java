package org.codehaus.jackson.jaxrs;

import org.codehaus.jackson.JsonParseException;

import com.wxxr.javax.ws.rs.core.Response;
import com.wxxr.javax.ws.rs.ext.ExceptionMapper;
import com.wxxr.javax.ws.rs.ext.Provider;

/**
 * Implementation of {@link ExceptionMapper} to send down a "400 Bad Request"
 * in the event unparsable JSON is received.
 */
@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {
    // uncomment when baseline is 1.6
    //@Override
    public Response toResponse(JsonParseException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).type("text/plain").build();
    }
}
