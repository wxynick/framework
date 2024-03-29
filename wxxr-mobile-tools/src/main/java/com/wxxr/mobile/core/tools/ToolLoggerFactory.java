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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class ToolLoggerFactory implements ILoggerFactory {

	final static ToolLoggerFactory INSTANCE = new ToolLoggerFactory();

	private Map<String, Logger> loggerMap = new HashMap<String, Logger>();

	public Logger getLogger(String name) {
		Logger slogger = null;
		// protect against concurrent access of the loggerMap
		synchronized (this) {
			slogger = (Logger) loggerMap.get(name);
			if (slogger == null) {
				slogger = new ToolLogger(name);
				loggerMap.put(name, slogger);
			}
		}
		return slogger;
	}
}
