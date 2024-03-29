package com.wxxr.mobile.core.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wxxr.mobile.core.util.StringUtils;

public class ImportOrganizerImpl implements ImportOrganizer {

	private static final String JAVA_LANG = "java.lang.";

	private Set<String> importedSimple = new HashSet<String>();

	private Set<String> importedFull = new HashSet<String>();

	@Override
	public String[] getTypeImports(String type) {
		List<String> imports = new ArrayList<String>();
		calculateTypeImports(type, imports);
		return imports.toArray(new String[imports.size()]);
	}

	private void calculateTypeImports(String type, List<String> imports) {
		ParsedTypeName parsedName = parseType(type);
		String simpleName = parsedName.getClassName();
		String genericType = parsedName.getGenericType();

		if (!parsedName.isSimple() && !genericType.startsWith(JAVA_LANG)) {
			if (!importedSimple.contains(simpleName)) {
				importedSimple.add(simpleName);
				importedFull.add(genericType);
				imports.add(genericType);
			}
		}

		for (ParsedTypeNameParam generic : parsedName.getParams()) {
			calculateTypeImports(generic.getType(), imports);
		}
	}

	@Override
	public String getTypeUsage(String type) {
		ParsedTypeName parsedName = parseType(type);
		String simpleName = parsedName.getClassName();
		String genericType = parsedName.getGenericType();

		if (!parsedName.isSimple()) {
			if (genericType.startsWith(JAVA_LANG)
					|| importedFull.contains(genericType)) {
				return simpleName + renderParams(parsedName.getParams())
						+ parsedName.getArrayPart();
			} else {
				return type + renderParams(parsedName.getParams());
			}
		} else {
			return type; // a simple type
		}
	}

	private String renderParams(List<ParsedTypeNameParam> params) {
		if (!params.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("<");

			for (Iterator<ParsedTypeNameParam> it = params.iterator(); it
					.hasNext();) {
				ParsedTypeNameParam type = (ParsedTypeNameParam) it.next();

				if (StringUtils.isNotEmpty(type.getWildcard())) {
					sb.append(type.getWildcard());
					sb.append(" ");
				}

				sb.append(getTypeUsage(type.getType()));

				if (it.hasNext()) {
					sb.append(", ");
				}
			}

			sb.append(">");
			return sb.toString();
		} else {
			return "";
		}
	}

	private ParsedTypeName parseType(String type) {
		String regex = "^(?:([^<>]+)\\.)?([^.<>]+?)(?:\\s?<(.*)>)?((?:\\[\\])*)?$";
		Matcher matcher = Pattern.compile(regex).matcher(type);
		if (matcher.matches()) {
			List<ParsedTypeNameParam> params = extractParams(matcher.group(3));
			int arrayDimensions = calculateArrayDimensions(matcher.group(4));
			return new ParsedTypeName(matcher.group(1), matcher.group(2),
					params, arrayDimensions);
		} else {
			throw new IllegalArgumentException("Cannot parse type!");
		}
	}

	private int calculateArrayDimensions(String dim) {
		return StringUtils.isNotEmpty(dim) ? dim.length() / 2 : 0;
	}

	private List<ParsedTypeNameParam> extractParams(String params) {
		List<ParsedTypeNameParam> parts = new ArrayList<ParsedTypeNameParam>();

		if (StringUtils.isNotEmpty(params)) {

			StringBuffer sb = new StringBuffer(params);
			Pattern pattern = Pattern.compile("<[^<>]+?>");

			Matcher m = pattern.matcher(sb.toString());
			while (m.find()) {
				for (int i = m.start(); i < m.end(); i++) {
					sb.setCharAt(i, ' ');
				}
				m = pattern.matcher(sb.toString());
			}
			String projection = sb.toString();

			int from = 0;
			int pos = projection.indexOf(',');

			while (pos > 0) {
				String part = params.substring(from, pos).trim();
				parts.add(typeParam(part));

				from = pos + 1;
				pos = projection.indexOf(',', pos + 1);
			}

			String part = params.substring(from).trim();
			parts.add(typeParam(part));
		}

		return parts;
	}

	private ParsedTypeNameParam typeParam(String type) {
		String regex = "^((?:\\?|[\\w$]+)\\s(?:extends|super))\\s+(.*)$";
		Matcher matcher = Pattern.compile(regex).matcher(type);
		if (matcher.matches()) {
			return new ParsedTypeNameParam(matcher.group(1), matcher.group(2));
		} else {
			return new ParsedTypeNameParam("", type);
		}

	}

}