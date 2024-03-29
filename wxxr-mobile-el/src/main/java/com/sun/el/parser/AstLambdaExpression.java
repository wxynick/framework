/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.el.parser;

import java.util.List;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.ValueExpressionImpl;
import com.sun.el.util.MessageFactory;
import com.wxxr.javax.el.ELException;
import com.wxxr.javax.el.LambdaExpression;
import com.wxxr.javax.el.ValueExpression;

/**
 * @author Kin-man Chung
 */
public
class AstLambdaExpression extends SimpleNode {

    public AstLambdaExpression(int id) {
      super(id);
    }

    public Object getValue(EvaluationContext ctx) throws ELException {
        // Create a lambda expression
        ValueExpression expr =
            new ValueExpressionImpl("#{Lambda Expression}",
                                    this.children[1],
                                    ctx.getFunctionMapper(),
                                    ctx.getVariableMapper(),
                                    null);
        List<String>parameters =
            ((AstLambdaParameters) this.children[0]).getParameters();
        LambdaExpression lambda = new LambdaExpression(parameters, expr);
        if (this.children.length <= 2) {
            return lambda;
        }

        // There are arguments following the lambda exprn, invoke it now.
        Object ret = null;
        for (int i = 2; i < this.children.length; i++) {
            if (ret != null) {
                if (!(ret instanceof LambdaExpression)) {
                    throw new ELException(MessageFactory.get(
                        "error.lambda.call"));
                }
                lambda = (LambdaExpression) ret;
            }
            AstMethodArguments args = (AstMethodArguments) this.children[i];
            ret = lambda.invoke(ctx, args.getParameters(ctx));
        }
        return ret;
    }
    
    public void accept(NodeVisitor visitor) throws ELException {
        visitor.visit(this);
    }
}
