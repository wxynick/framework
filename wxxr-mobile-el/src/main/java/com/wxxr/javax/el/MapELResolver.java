/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
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

package com.wxxr.javax.el;

import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.wxxr.mobile.core.util.FeatureDescriptor;

/**
 * Defines property resolution behavior on instances of {@link java.util.Map}.
 *
 * <p>This resolver handles base objects of type <code>java.util.Map</code>.
 * It accepts any object as a property and uses that object as a key in
 * the map. The resulting value is the value in the map that is associated with 
 * that key.</p>
 *
 * <p>This resolver can be constructed in read-only mode, which means that
 * {@link #isReadOnly} will always return <code>true</code> and 
 * {@link #setValue} will always throw
 * <code>PropertyNotWritableException</code>.</p>
 *
 * <p><code>ELResolver</code>s are combined together using 
 * {@link CompositeELResolver}s, to define rich semantics for evaluating 
 * an expression. See the javadocs for {@link ELResolver} for details.</p>
 *
 * @see CompositeELResolver
 * @see ELResolver
 * @see java.util.Map
 * @since JSP 2.1
 */
public class MapELResolver extends ELResolver {

    /**
     * Creates a new read/write <code>MapELResolver</code>.
     */
    public MapELResolver() {
        this.isReadOnly = false;
    }

    /**
     * Creates a new <code>MapELResolver</code> whose read-only status is
     * determined by the given parameter.
     *
     * @param isReadOnly <code>true</code> if this resolver cannot modify
     *     maps; <code>false</code> otherwise.
     */
    public MapELResolver(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * If the base object is a map, returns the most general acceptable type 
     * for a value in this map.
     *
     * <p>If the base is a <code>Map</code>, the <code>propertyResolved</code>
     * property of the <code>ELContext</code> object must be set to
     * <code>true</code> by this resolver, before returning. If this property
     * is not <code>true</code> after this method is called, the caller 
     * should ignore the return value.</p>
     *
     * <p>Assuming the base is a <code>Map</code>, this method will always
     * return <code>Object.class</code>. This is because <code>Map</code>s
     * accept any object as the value for a given key.</p>
     *
     * @param context The context of this evaluation.
     * @param base The map to analyze. Only bases of type <code>Map</code>
     *     are handled by this resolver.
     * @param property The key to return the acceptable type for.
     *     Ignored by this resolver.
     * @return If the <code>propertyResolved</code> property of 
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     the most general acceptable type; otherwise undefined.
     * @throws NullPointerException if context is <code>null</code>
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    public Class<?> getType(ELContext context,
                         Object base,
                         Object property) {

        if (context == null) {
            throw new NullPointerException();
        }
      
        if (base != null && base instanceof Map) {
            context.setPropertyResolved(true);
            return Object.class;
        }
        return null;
    }

    /**
     * If the base object is a map, returns the value associated with the
     * given key, as specified by the <code>property</code> argument. If the
     * key was not found, <code>null</code> is returned.
     *
     * <p>If the base is a <code>Map</code>, the <code>propertyResolved</code>
     * property of the <code>ELContext</code> object must be set to
     * <code>true</code> by this resolver, before returning. If this property
     * is not <code>true</code> after this method is called, the caller 
     * should ignore the return value.</p>
     *
     * <p>Just as in {@link java.util.Map#get}, just because <code>null</code>
     * is returned doesn't mean there is no mapping for the key; it's also
     * possible that the <code>Map</code> explicitly maps the key to
     * <code>null</code>.</p>
     *
     * @param context The context of this evaluation.
     * @param base The map to be analyzed. Only bases of type <code>Map</code>
     *     are handled by this resolver.
     * @param property The key whose associated value is to be returned.
     * @return If the <code>propertyResolved</code> property of 
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     the value associated with the given key or <code>null</code>
     *     if the key was not found. Otherwise, undefined.
     * @throws ClassCastException if the key is of an inappropriate type 
     *     for this map (optionally thrown by the underlying <code>Map</code>).
     * @throws NullPointerException if context is <code>null</code>, or if 
     *     the key is null and this map does not permit null keys (the
     *     latter is optionally thrown by the underlying <code>Map</code>).
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    public Object getValue(ELContext context,
                           Object base,
                           Object property) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base != null && base instanceof Map) {
            context.setPropertyResolved(base, property);
            Map map = (Map) base;
            return map.get(property);
        }
        return null;
    }

    static private Class<?> theUnmodifiableMapClass =
        Collections.unmodifiableMap(new HashMap<Object, Object>()).getClass();

    /**
     * If the base object is a map, attempts to set the value associated with
     * the given key, as specified by the <code>property</code> argument.
     *
     * <p>If the base is a <code>Map</code>, the <code>propertyResolved</code>
     * property of the <code>ELContext</code> object must be set to
     * <code>true</code> by this resolver, before returning. If this property
     * is not <code>true</code> after this method is called, the caller 
     * can safely assume no value was set.</p>
     *
     * <p>If this resolver was constructed in read-only mode, this method will
     * always throw <code>PropertyNotWritableException</code>.</p>
     *
     * <p>If a <code>Map</code> was created using 
     * {@link java.util.Collections#unmodifiableMap}, this method must
     * throw <code>PropertyNotWritableException</code>. Unfortunately, 
     * there is no Collections API method to detect this. However, an 
     * implementation can create a prototype unmodifiable <code>Map</code> 
     * and query its runtime type to see if it matches the runtime type of 
     * the base object as a workaround.</p>
     *
     * @param context The context of this evaluation.
     * @param base The map to be modified. Only bases of type <code>Map</code>
     *     are handled by this resolver.
     * @param property The key with which the specified value is to be
     *     associated.
     * @param val The value to be associated with the specified key.
     * @throws ClassCastException if the class of the specified key or 
     *     value prevents it from being stored in this map.
     * @throws NullPointerException if context is <code>null</code>, or if 
     *     this map does not permit <code>null</code> keys or values, and
     *     the specified key or value is <code>null</code>.
     * @throws IllegalArgumentException if some aspect of this key or 
     *     value prevents it from being stored in this map.
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     * @throws PropertyNotWritableException if this resolver was constructed
     *     in read-only mode, or if the put operation is not supported by 
     *     the underlying map.
     */
    public void setValue(ELContext context,
                         Object base,
                         Object property,
                         Object val) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base != null && base instanceof Map) {
            context.setPropertyResolved(base, property);
            // The cast is safe
            @SuppressWarnings("unchecked")
            Map<Object, Object> map = (Map)base;
            if (isReadOnly || map.getClass() == theUnmodifiableMapClass) {
                throw new PropertyNotWritableException();
            }
            try {
                map.put(property, val);
            } catch (UnsupportedOperationException ex) {
                throw new PropertyNotWritableException();
            }
        }
    }

    /**
     * If the base object is a map, returns whether a call to 
     * {@link #setValue} will always fail.
     *
     * <p>If the base is a <code>Map</code>, the <code>propertyResolved</code>
     * property of the <code>ELContext</code> object must be set to
     * <code>true</code> by this resolver, before returning. If this property
     * is not <code>true</code> after this method is called, the caller 
     * should ignore the return value.</p>
     *
     * <p>If this resolver was constructed in read-only mode, this method will
     * always return <code>true</code>.</p>
     *
     * <p>If a <code>Map</code> was created using 
     * {@link java.util.Collections#unmodifiableMap}, this method must
     * return <code>true</code>. Unfortunately, there is no Collections API
     * method to detect this. However, an implementation can create a
     * prototype unmodifiable <code>Map</code> and query its runtime type
     * to see if it matches the runtime type of the base object as a 
     * workaround.</p>
     *
     * @param context The context of this evaluation.
     * @param base The map to analyze. Only bases of type <code>Map</code>
     *     are handled by this resolver.
     * @param property The key to return the read-only status for.
     *     Ignored by this resolver.
     * @return If the <code>propertyResolved</code> property of 
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     <code>true</code> if calling the <code>setValue</code> method
     *     will always fail or <code>false</code> if it is possible that
     *     such a call may succeed; otherwise undefined.
     * @throws NullPointerException if context is <code>null</code>
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    public boolean isReadOnly(ELContext context,
                              Object base,
                              Object property) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base != null && base instanceof Map) {
            context.setPropertyResolved(true);
            Map map = (Map) base;
            return isReadOnly || map.getClass() == theUnmodifiableMapClass;
        }
        return false;
    }

    /**
     * If the base object is a map, returns an <code>Iterator</code>
     * containing the set of keys available in the <code>Map</code>. 
     * Otherwise, returns <code>null</code>.
     *
     * <p>The <code>Iterator</code> returned must contain zero or more 
     * instances of {@link java.beans.FeatureDescriptor}. Each info object 
     * contains information about a key in the Map, and is initialized as 
     * follows:
     * <dl>
     *     <li>displayName - The return value of calling the 
     *         <code>toString</code> method on this key, or 
     *         <code>"null"</code> if the key is <code>null</code>.</li>
     *     <li>name - Same as displayName property.</li>
     *     <li>shortDescription - Empty string</li>
     *     <li>expert - <code>false</code></li>
     *     <li>hidden - <code>false</code></li>
     *     <li>preferred - <code>true</code></li>
     * </dl>
     * In addition, the following named attributes must be set in the
     * returned <code>FeatureDescriptor</code>s:
     * <dl>
     *     <li>{@link ELResolver#TYPE} - The return value of calling the <code>getClass()</code>
     *         method on this key, or <code>null</code> if the key is
     *         <code>null</code>.</li>
     *     <li>{@link ELResolver#RESOLVABLE_AT_DESIGN_TIME} - <code>true</code></li>
     * </dl>
     * </p>
     * 
     * @param context The context of this evaluation.
     * @param base The map whose keys are to be iterated over. Only bases 
     *     of type <code>Map</code> are handled by this resolver.
     * @return An <code>Iterator</code> containing zero or more (possibly
     *     infinitely more) <code>FeatureDescriptor</code> objects, each
     *     representing a key in this map, or <code>null</code> if 
     *     the base object is not a map.
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(
                                          ELContext context,
                                          Object base) {

        if (base != null && base instanceof Map) {
            Map map = (Map) base;
            Iterator iter = map.keySet().iterator();
            List<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();
            while (iter.hasNext()) {
                Object key = iter.next();
                FeatureDescriptor descriptor = new FeatureDescriptor();
                String name = (key==null)? null: key.toString();
                descriptor.setName(name);
                descriptor.setDisplayName(name);
                descriptor.setShortDescription("");
                descriptor.setExpert(false);
                descriptor.setHidden(false);
                descriptor.setPreferred(true);
                if (key != null) {
                    descriptor.setValue("type", key.getClass());
                }
                descriptor.setValue("resolvableAtDesignTime", Boolean.TRUE);
                list.add(descriptor);
            }
            return list.iterator();
        }
        return null;
    }

    /**
     * If the base object is a map, returns the most general type that 
     * this resolver accepts for the <code>property</code> argument.
     * Otherwise, returns <code>null</code>.
     *
     * <p>Assuming the base is a <code>Map</code>, this method will always
     * return <code>Object.class</code>. This is because <code>Map</code>s
     * accept any object as a key.</p>
     *
     * @param context The context of this evaluation.
     * @param base The map to analyze. Only bases of type <code>Map</code>
     *     are handled by this resolver.
     * @return <code>null</code> if base is not a <code>Map</code>; otherwise
     *     <code>Object.class</code>.
     */
    public Class<?> getCommonPropertyType(ELContext context,
                                       Object base) {
        if (base != null && base instanceof Map) {
            return Object.class;
        }
        return null;
    }

    private boolean isReadOnly;
}

