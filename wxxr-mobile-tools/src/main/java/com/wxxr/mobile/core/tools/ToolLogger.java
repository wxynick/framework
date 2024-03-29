/**
 * Copyright 2011 Nikolche Mihajlovski
 *
 * This file is part of JAnnocessor.
 *
 * JAnnocessor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JAnnocessor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JAnnocessor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wxxr.mobile.core.tools;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class ToolLogger extends MarkerIgnoringBase {

	public static boolean DEBUG = false;

	private static final long serialVersionUID = 4447256230980716124L;

	public static Messager messager = new ConsoleMessager();

	private final String name;

	ToolLogger(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isTraceEnabled() {
		return DEBUG;
	}

	@Override
	public void trace(String msg) {
		if (isTraceEnabled()) {
			messager.printMessage(Diagnostic.Kind.OTHER, msg);
		}
	}

	@Override
	public void trace(String format, Object arg) {
		if (isTraceEnabled()) {
			trace(formated(format, arg));
		}
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		if (isTraceEnabled()) {
			trace(formated(format, arg1, arg2));
		}
	}

	@Override
	public void trace(String format, Object[] argArray) {
		if (isTraceEnabled()) {
			trace(formated(format, argArray));
		}
	}

	@Override
	public void trace(String msg, Throwable t) {
		if (isTraceEnabled()) {
			trace(reportError(msg, t));
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return DEBUG;
	}

	@Override
	public void debug(String msg) {
		if (isDebugEnabled()) {
			messager.printMessage(Diagnostic.Kind.OTHER, msg);
		}
	}

	@Override
	public void debug(String format, Object arg) {
		if (isDebugEnabled()) {
			debug(formated(format, arg));
		}
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		if (isDebugEnabled()) {
			debug(formated(format, arg1, arg2));
		}
	}

	@Override
	public void debug(String format, Object[] argArray) {
		if (isDebugEnabled()) {
			debug(formated(format, argArray));
		}
	}

	@Override
	public void debug(String msg, Throwable t) {
		if (isDebugEnabled()) {
			debug(reportError(msg, t));
		}
	}

	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	@Override
	public void info(String msg) {
		messager.printMessage(Diagnostic.Kind.NOTE, msg);
	}

	@Override
	public void info(String format, Object arg) {
		info(formated(format, arg));
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		info(formated(format, arg1, arg2));
	}

	@Override
	public void info(String format, Object[] argArray) {
		info(formated(format, argArray));
	}

	@Override
	public void info(String msg, Throwable t) {
		info(reportError(msg, t));
	}

	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	@Override
	public void warn(String msg) {
		messager.printMessage(Diagnostic.Kind.WARNING, msg);
	}

	@Override
	public void warn(String format, Object arg) {
		warn(formated(format, arg));
	}

	@Override
	public void warn(String format, Object[] argArray) {
		warn(formated(format, argArray));
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		warn(formated(format, arg1, arg2));
	}

	@Override
	public void warn(String msg, Throwable t) {
		warn(reportError(msg, t));
	}

	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	@Override
	public void error(String msg) {
		messager.printMessage(Diagnostic.Kind.ERROR, msg);
	}

	@Override
	public void error(String format, Object arg) {
		error(formated(format, arg));
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		error(formated(format, arg1, arg2));
	}

	@Override
	public void error(String format, Object[] argArray) {
		error(formated(format, argArray));
	}

	@Override
	public void error(String msg, Throwable t) {
		error(reportError(msg, t));
	}

	private String formated(String format, Object... args) {
		return MessageFormatter.arrayFormat(format, args).getMessage();
	}

	private String reportError(String msg, Throwable t) {
		t.printStackTrace();
		return formated("Exception: {}", t.getMessage()); // FIXME improve
	}

}
