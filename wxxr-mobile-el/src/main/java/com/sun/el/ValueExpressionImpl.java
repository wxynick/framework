/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
 */

package com.sun.el;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;


import com.sun.el.lang.ELSupport;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.lang.ExpressionBuilder;
import com.sun.el.parser.AbstractNodeVisitor;
import com.sun.el.parser.AstDotSuffix;
import com.sun.el.parser.AstIdentifier;
import com.sun.el.parser.AstLiteralExpression;
import com.sun.el.parser.AstPropertySuffix;
import com.sun.el.parser.Node;
import com.sun.el.util.ReflectionUtil;
import com.wxxr.javax.el.ELContext;
import com.wxxr.javax.el.ELException;
import com.wxxr.javax.el.ELResolver;
import com.wxxr.javax.el.EvaluationListener;
import com.wxxr.javax.el.Expression;
import com.wxxr.javax.el.ExpressionFactory;
import com.wxxr.javax.el.FunctionMapper;
import com.wxxr.javax.el.PropertyNotFoundException;
import com.wxxr.javax.el.PropertyNotWritableException;
import com.wxxr.javax.el.ValueExpression;
import com.wxxr.javax.el.ValueReference;
import com.wxxr.javax.el.VariableMapper;

/**
 * An <code>Expression</code> that can get or set a value.
 * 
 * <p>
 * In previous incarnations of this API, expressions could only be read.
 * <code>ValueExpression</code> objects can now be used both to retrieve a
 * value and to set a value. Expressions that can have a value set on them are
 * referred to as l-value expressions. Those that cannot are referred to as
 * r-value expressions. Not all r-value expressions can be used as l-value
 * expressions (e.g. <code>"${1+1}"</code> or
 * <code>"${firstName} ${lastName}"</code>). See the EL Specification for
 * details. Expressions that cannot be used as l-values must always return
 * <code>true</code> from <code>isReadOnly()</code>.
 * </p>
 * 
 * <p>
 * <code>The {@link ExpressionFactory#createValueExpression} method
 * can be used to parse an expression string and return a concrete instance
 * of <code>ValueExpression</code> that encapsulates the parsed expression.
 * The {@link FunctionMapper} is used at parse time, not evaluation time, 
 * so one is not needed to evaluate an expression using this class.  
 * However, the {@link ELContext} is needed at evaluation time.</p>
 *
 * <p>The {@link #getValue}, {@link #setValue}, {@link #isReadOnly} and
 * {@link #getType} methods will evaluate the expression each time they are
 * called. The {@link ELResolver} in the <code>ELContext</code> is used to 
 * resolve the top-level variables and to determine the behavior of the
 * <code>.</code> and <code>[]</code> operators. For any of the four methods,
 * the {@link ELResolver#getValue} method is used to resolve all properties 
 * up to but excluding the last one. This provides the <code>base</code> 
 * object. At the last resolution, the <code>ValueExpression</code> will 
 * call the corresponding {@link ELResolver#getValue}, 
 * {@link ELResolver#setValue}, {@link ELResolver#isReadOnly} or 
 * {@link ELResolver#getType} method, depending on which was called on 
 * the <code>ValueExpression</code>.
 * </p>
 *
 * <p>See the notes about comparison, serialization and immutability in 
 * the {@link Expression} javadocs.
 *
 * @see com.wxxr.javax.el.ELResolver
 * @see com.wxxr.javax.el.Expression
 * @see com.wxxr.javax.el.ExpressionFactory
 * @see com.wxxr.javax.el.ValueExpression
 * 
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author$
 */
public final class ValueExpressionImpl extends ValueExpression implements
        Externalizable {

    private Class expectedType;

    private String expr;

    private FunctionMapper fnMapper;

    private VariableMapper varMapper;

    private transient Node node;
    
    private List<String> referringBeanNames, referringPropertyNames;

    public ValueExpressionImpl() {

    }

    /**
     * 
     */
    public ValueExpressionImpl(String expr, Node node, FunctionMapper fnMapper,
            VariableMapper varMapper, Class expectedType) {
        this.expr = expr;
        this.node = node;
        this.fnMapper = fnMapper;
        this.varMapper = varMapper;
        this.expectedType = expectedType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof ValueExpressionImpl) {
            ValueExpressionImpl v = (ValueExpressionImpl) obj;
            return getNode().equals(v.getNode());
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.wxxr.javax.el.ValueExpression#getExpectedType()
     */
    public Class getExpectedType() {
        return this.expectedType;
    }

    /**
     * Returns the type the result of the expression will be coerced to after
     * evaluation.
     * 
     * @return the <code>expectedType</code> passed to the
     *         <code>ExpressionFactory.createValueExpression</code> method
     *         that created this <code>ValueExpression</code>.
     * 
     * @see com.wxxr.javax.el.Expression#getExpressionString()
     */
    public String getExpressionString() {
        return this.expr;
    }

    /**
     * @return The Node for the expression
     * @throws ELException
     */
    protected Node getNode() throws ELException {
        if (this.node == null) {
            this.node = ExpressionBuilder.createNode(this.expr);
        }
        return this.node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.wxxr.javax.el.ValueExpression#getType(com.wxxr.javax.el.ELContext)
     */
    public Class getType(ELContext context) throws PropertyNotFoundException,
            ELException {
        EvaluationContext ctx = new EvaluationContext(context, this.fnMapper,
                this.varMapper);
        return this.getNode().getType(ctx);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.wxxr.javax.el.ValueExpression#getValueReference(com.wxxr.javax.el.ELContext)
     */
    public ValueReference getValueReference(ELContext context)
            throws PropertyNotFoundException, ELException {
        EvaluationContext ctx = new EvaluationContext(context, this.fnMapper,
                this.varMapper);
        return this.getNode().getValueReference(ctx);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.wxxr.javax.el.ValueExpression#getValue(com.wxxr.javax.el.ELContext)
     */
    public Object getValue(ELContext context) throws PropertyNotFoundException,
            ELException {
        EvaluationContext ctx = new EvaluationContext(context, this.fnMapper,
                this.varMapper);
        ctx.notifyBeforeEvaluation(this.expr);
        Object value = this.getNode().getValue(ctx);
        if (this.expectedType != null) {
            try {
                value = context.convertToType(value, this.expectedType);
            } catch (IllegalArgumentException ex) {
                throw new ELException(ex);
            }
        }
        ctx.notifyAfterEvaluation(this.expr);
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getNode().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.wxxr.javax.el.ValueExpression#isLiteralText()
     */
    public boolean isLiteralText() {
        try {
            return this.getNode() instanceof AstLiteralExpression;
        } catch (ELException ele) {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.wxxr.javax.el.ValueExpression#isReadOnly(com.wxxr.javax.el.ELContext)
     */
    public boolean isReadOnly(ELContext context)
            throws PropertyNotFoundException, ELException {
        EvaluationContext ctx = new EvaluationContext(context, this.fnMapper,
                this.varMapper);
        return this.getNode().isReadOnly(ctx);
    }

    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        this.expr = in.readUTF();
        String type = in.readUTF();
        if (!"".equals(type)) {
            this.expectedType = ReflectionUtil.forName(type);
        }
        this.fnMapper = (FunctionMapper) in.readObject();
        this.varMapper = (VariableMapper) in.readObject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.wxxr.javax.el.ValueExpression#setValue(com.wxxr.javax.el.ELContext,
     *      java.lang.Object)
     */
    public void setValue(ELContext context, Object value)
            throws PropertyNotFoundException, PropertyNotWritableException,
            ELException {
        EvaluationContext ctx = new EvaluationContext(context, this.fnMapper,
                this.varMapper);
        this.getNode().setValue(ctx, value);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.expr);
        out.writeUTF((this.expectedType != null) ? this.expectedType.getName()
                : "");
        out.writeObject(this.fnMapper);
        out.writeObject(this.varMapper);
    }

    public String toString() {
        return "ValueExpression["+this.expr+"]";
    }

	@Override
	public List<String> getReferringBeanNames() {
		if(this.referringBeanNames == null){
			initReferringNames();
		}
		return this.referringBeanNames;
	}

	/**
	 * 
	 */
	protected void initReferringNames() {
		final ArrayList<String> names = new ArrayList<String>();
		final ArrayList<String> properties = new ArrayList<String>();
		this.node.accept(new AbstractNodeVisitor(){

			/* (non-Javadoc)
			 * @see com.sun.el.parser.AbstractNodeVisitor#visit(com.sun.el.parser.AstIdentifier)
			 */
			@Override
			public void visit(AstIdentifier node) {
				String name = node.getImage();
				if((!names.contains(name))&&((varMapper == null)||(varMapper.resolveVariable(name) == null))){
					names.add(name);
				}
				super.visit(node);
			}

			/* (non-Javadoc)
			 * @see com.sun.el.parser.AbstractNodeVisitor#visit(com.sun.el.parser.AstPropertySuffix)
			 */
			@Override
			public void visit(AstDotSuffix node) {
				if(node.jjtGetNumChildren() == 0){
					String name = node.getImage();
					if(!properties.contains(name)){
						properties.add(name);
					}
				}
				super.visit(node);
			}
			
		});
		this.referringBeanNames = names;
		this.referringPropertyNames = properties;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.javax.el.ValueExpression#getReferringPropertyNames()
	 */
	@Override
	public List<String> getReferringPropertyNames() {
		if(this.referringPropertyNames == null){
			initReferringNames();
		}
		return this.referringPropertyNames;
	}
}
