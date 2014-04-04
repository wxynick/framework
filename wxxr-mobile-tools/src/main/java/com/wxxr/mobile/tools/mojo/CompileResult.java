/*
 * Copyright (C) 2009-2013 The Project Lombok Authors.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wxxr.mobile.tools.mojo;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.tools.JavaFileObject;

import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;

public class CompileResult {
	private final List<CommentInfo> comments;
	private final JCCompilationUnit compilationUnit;
	private final boolean changed;
	
	public CompileResult(List<CommentInfo> comments, JCCompilationUnit compilationUnit, boolean changed) {
		this.comments = comments;
		this.compilationUnit = compilationUnit;
		this.changed = changed;
	}
	
	public void print(Writer out) throws IOException {
		if (!changed) {
			JavaFileObject sourceFile = compilationUnit.getSourceFile();
			if (sourceFile != null) {
				out.write(sourceFile.getCharContent(true).toString());
				return;
			}
		}
		
		out.write("// Generated by delombok at ");
		out.write(String.valueOf(new Date()));
		out.write(System.getProperty("line.separator"));
		
		com.sun.tools.javac.util.List<CommentInfo> comments_;
		if (comments instanceof com.sun.tools.javac.util.List) comments_ = (com.sun.tools.javac.util.List<CommentInfo>) comments;
		else comments_ = com.sun.tools.javac.util.List.from(comments.toArray(new CommentInfo[0]));
		
		compilationUnit.accept(new PrettyCommentsPrinter(out, compilationUnit, comments_));
	}
	
	public boolean isChanged() {
		return changed;
	}
}