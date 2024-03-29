/**
 * Copyright 2006 Envoi Solutions LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.jettison.mapped;

import java.util.Map;

import org.codehaus.jettison.AbstractXMLInputFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import com.wxxr.javax.xml.stream.XMLStreamException;
import com.wxxr.javax.xml.stream.XMLStreamReader;

public class MappedXMLInputFactory extends AbstractXMLInputFactory {


    private MappedNamespaceConvention convention;

    public MappedXMLInputFactory(Map nstojns) {
        this(new Configuration(nstojns));
    }
    
    public MappedXMLInputFactory(Configuration config) {
        this.convention = new MappedNamespaceConvention(config);
    }
    
    public XMLStreamReader createXMLStreamReader(JSONTokener tokener) throws XMLStreamException {
        try {
            JSONObject root = new JSONObject(tokener);
            return new MappedXMLStreamReader(root, convention);
        } catch (JSONException e) {
            throw new XMLStreamException(e);
        }
    }
}
