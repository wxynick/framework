/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.wxxr.javax.ws.rs.core;

import java.io.IOException;

/**
 * An I/O exception thrown by {@link com.wxxr.javax.ws.rs.ext.MessageBodyReader} implementations
 * when reading a zero-length message content to indicate that the message body reader
 * is not able to produce an instance representing an zero-length message content.
 * <p>
 * This exception, when thrown while reading a server request entity, is automatically
 * translated by JAX-RS server runtime into a {@link com.wxxr.javax.ws.rs.BadRequestException}
 * wrapping the original {@code NoContentException} and rethrown for a standard processing by
 * the registered {@link com.wxxr.javax.ws.rs.ext.ExceptionMapper exception mappers}.
 * </p>
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @since 2.0
 */
public class NoContentException extends IOException {
    private static final long serialVersionUID = -3082577759787473245L;
    private Throwable cause;
    /**
     * Construct a new {@code NoContentException} instance.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     */
    public NoContentException(String message) {
        super(message);
    }

    /**
     * Construct a new {@code NoContentException} instance.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the underlying cause of the exception.
     */
    public NoContentException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * Construct a new {@code NoContentException} instance.
     *
     * @param cause the underlying cause of the exception.
     */
    public NoContentException(Throwable cause) {
        super();
        this.cause = cause;
    }

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getCause()
	 */
	@Override
	public Throwable getCause() {
		return this.cause;
	}
}
