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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.wxxr.mobile.core.util.FeatureDescriptor;

/**
 * Defines property resolution behavior on instances of
 * {@link java.util.ResourceBundle}.
 * 
 * <p>
 * This resolver handles base objects of type
 * <code>java.util.ResourceBundle</code>. It accepts any object as a property
 * and coerces it to a <code>java.lang.String</code> for invoking
 * {@link java.util.ResourceBundle#getObject(java.lang.String)}.
 * </p>
 * 
 * <p>
 * This resolver is read only and will throw a
 * {@link PropertyNotWritableException} if <code>setValue</code> is called.
 * </p>
 * 
 * <p>
 * <code>ELResolver</code>s are combined together using
 * {@link CompositeELResolver}s, to define rich semantics for evaluating an
 * expression. See the javadocs for {@link ELResolver} for details.
 * </p>
 * 
 * @see CompositeELResolver
 * @see ELResolver
 * @see java.util.ResourceBundle
 * @since JSP 2.1
 */
public class ResourceBundleELResolver extends ELResolver {

    /**
     * If the base object is an instance of <code>ResourceBundle</code>,
     * the provided property will first be coerced to a <code>String</code>.
     * The <code>Object</code> returned by <code>getObject</code> on
     * the base <code>ResourceBundle</code> will be returned.
     * </p>
     * If the base is <code>ResourceBundle</code>, the
     * <code>propertyResolved</code> property of the <code>ELContext</code>
     * object must be set to <code>true</code> by this resolver, before
     * returning. If this property is not <code>true</code> after this method
     * is called, the caller should ignore the return value.
     * </p>
     * @param context
     *            The context of this evaluation.
     * @param base
     *            The ResourceBundle to analyze.
     * @param property
     *            The name of the property to analyze. Will be coerced to a
     *            <code>String</code>.
     * @return If the <code>propertyResolved</code> property of
     *         <code>ELContext</code> was set to <code>true</code>, then
     *         <code>null</code> if property is <code>null</code>;
     *         otherwise the <code>Object</code> for the given key
     *         (property coerced to <code>String</code>) from the
     *         <code>ResourceBundle</code>.
     *         If no object for the given key can be found, then the 
     *         <code>String</code> "???" + key + "???".
     * @throws NullPointerException
     *             if context is <code>null</code>
     * @throws ELException
     *             if an exception was thrown while performing the property or
     *             variable resolution. The thrown exception must be included as
     *             the cause property of this exception, if available.
     */
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base instanceof ResourceBundle) {
            context.setPropertyResolved(true);
            if (property != null) {
                try {
                    return ((ResourceBundle) base).getObject(property
                            .toString());
                } catch (MissingResourceException e) {
                    return "???" + property + "???";
                }
            }
        }
        return null;
    }

    /**
     * If the base object is an instance of <code>ResourceBundle</code>,
     * return <code>null</code>, since the resolver is read only.
     * 
     * <p>
     * If the base is <code>ResourceBundle</code>, the
     * <code>propertyResolved</code> property of the <code>ELContext</code>
     * object must be set to <code>true</code> by this resolver, before
     * returning. If this property is not <code>true</code> after this method
     * is called, the caller should ignore the return value.
     * </p>
     * 
     * @param context
     *            The context of this evaluation.
     * @param base
     *            The ResourceBundle to analyze.
     * @param property
     *            The name of the property to analyze.
     * @return If the <code>propertyResolved</code> property of
     *         <code>ELContext</code> was set to <code>true</code>, then
     *         <code>null</code>; otherwise undefined.
     * @throws NullPointerException
     *             if context is <code>null</code>
     */
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base instanceof ResourceBundle) {
            context.setPropertyResolved(true);
        }
        return null;
    }

    /**
     * If the base object is a ResourceBundle, throw a
     * {@link PropertyNotWritableException}.
     * 
     * @param context
     *            The context of this evaluation.
     * @param base
     *            The ResourceBundle to be modified. Only bases that are of type
     *            ResourceBundle are handled.
     * @param property
     *            The String property to use.
     * @param value
     *            The value to be set.
     * @throws NullPointerException
     *             if context is <code>null</code>.
     * @throws PropertyNotWritableException
     *             Always thrown if base is an instance of ReasourceBundle.
     */
    public void setValue(ELContext context, Object base, Object property,
            Object value) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base instanceof ResourceBundle) {
            context.setPropertyResolved(true);
            throw new PropertyNotWritableException(
                    "ResourceBundles are immutable");
        }
    }

    /**
     * If the base object is not null and an instanceof {@link ResourceBundle},
     * return <code>true</code>.
     * 
     * @param context
     *            The context of this evaluation.
     * @param base
     *            The ResourceBundle to be modified. Only bases that are of type
     *            ResourceBundle are handled.
     * @param property
     *            The String property to use.
     * @return If the <code>propertyResolved</code> property of
     *         <code>ELContext</code> was set to <code>true</code>, then
     *         <code>true</code>; otherwise undefined.
     * @throws NullPointerException
     *             if context is <code>null</code>
     */
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base instanceof ResourceBundle) {
            context.setPropertyResolved(true);
            return true;
        }
        return false;
    }

    /**
     * If the base object is a ResourceBundle, returns an <code>Iterator</code>
     * containing the set of keys available in the <code>ResourceBundle</code>.
     * Otherwise, returns <code>null</code>.
     * 
     * <p>
     * The <code>Iterator</code> returned must contain zero or more instances
     * of {@link java.beans.FeatureDescriptor}. Each info object contains
     * information about a key in the ResourceBundle, and is initialized as
     * follows:
     * <dl>
     * <li>displayName - The <code>String</code> key
     * <li>name - Same as displayName property.</li>
     * <li>shortDescription - Empty string</li>
     * <li>expert - <code>false</code></li>
     * <li>hidden - <code>false</code></li>
     * <li>preferred - <code>true</code></li>
     * </dl>
     * In addition, the following named attributes must be set in the returned
     * <code>FeatureDescriptor</code>s:
     * <dl>
     * <li>{@link ELResolver#TYPE} - <code>String.class</code></li>
     * <li>{@link ELResolver#RESOLVABLE_AT_DESIGN_TIME} - <code>true</code></li>
     * </dl>
     * </p>
     * 
     * @param context
     *            The context of this evaluation.
     * @param base
     *            The bundle whose keys are to be iterated over. Only bases of
     *            type <code>ResourceBundle</code> are handled by this
     *            resolver.
     * @return An <code>Iterator</code> containing zero or more (possibly
     *         infinitely more) <code>FeatureDescriptor</code> objects, each
     *         representing a key in this bundle, or <code>null</code> if the
     *         base object is not a ResourceBundle.
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (base instanceof ResourceBundle) {
            ResourceBundle bundle = (ResourceBundle) base;
            List<FeatureDescriptor> features = new ArrayList<FeatureDescriptor>();
            String key = null;
            FeatureDescriptor desc = null;
            for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements();) {
                key = e.nextElement();
                desc = new FeatureDescriptor();
                desc.setDisplayName(key);
                desc.setExpert(false);
                desc.setHidden(false);
                desc.setName(key);
                desc.setPreferred(true);
                desc.setValue(TYPE, String.class);
                desc.setValue(RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
                features.add(desc);
            }
            return features.iterator();
        }
        return null;
    }

    /**
     * If the base object is a ResourceBundle, returns the most general type
     * that this resolver accepts for the <code>property</code> argument.
     * Otherwise, returns <code>null</code>.
     * 
     * <p>
     * Assuming the base is a <code>ResourceBundle</code>, this method will
     * always return <code>String.class</code>.
     * 
     * @param context
     *            The context of this evaluation.
     * @param base
     *            The bundle to analyze. Only bases of type
     *            <code>ResourceBundle</code> are handled by this resolver.
     * @return <code>null</code> if base is not a <code>ResourceBundle</code>;
     *         otherwise <code>String.class</code>.
     */
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base instanceof ResourceBundle) {
            return String.class;
        }
        return null;
    }
}
