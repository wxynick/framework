package org.codehaus.jackson.node;

import java.io.IOException;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * This concrete value class is used to contain boolean (true / false)
 * values. Only two instances are ever created, to minimize memory
 * usage
 */
public final class BooleanNode
    extends ValueNode
{
    // // Just need two instances...

    public final static BooleanNode TRUE = new BooleanNode();
    public final static BooleanNode FALSE = new BooleanNode();

    private BooleanNode() { }

    public static BooleanNode getTrue() { return TRUE; }
    public static BooleanNode getFalse() { return FALSE; }

    public static BooleanNode valueOf(boolean b) { return b ? TRUE : FALSE; }

    // Interesting... two choices...
    @Override public JsonToken asToken() {
        return (this == TRUE) ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
    }

    @Override
    public boolean isBoolean() { return true; }

    @Override
    public boolean getBooleanValue() {
        return (this == TRUE);
    }

    @Override
    public String getValueAsText() {
        return (this == TRUE) ? "true" : "false";
    }

    @Override
    public int getValueAsInt(int defaultValue) {
        return (this == TRUE) ? 1 : 0;
    }
    @Override
    public long getValueAsLong(long defaultValue) {
        return (this == TRUE) ? 1L : 0L;
    }
    @Override
    public double getValueAsDouble(double defaultValue) {
        return (this == TRUE) ? 1.0 : 0.0;
    }
    
    @Override
    public final void serialize(JsonGenerator jg, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
        jg.writeBoolean(this == TRUE);
    }

    @Override
    public boolean equals(Object o)
    {
        /* Since there are only ever two instances in existence
         * can do identity comparison
         */
        return (o == this);
    }
}
