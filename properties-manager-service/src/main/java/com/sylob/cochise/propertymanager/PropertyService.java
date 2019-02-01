package com.sylob.cochise.propertymanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PropertyService {

	private static final ConcurrentHashMap<String, PropertySpec> INT_MAP = new ConcurrentHashMap<>();

	private static final Pattern PATTERN_STATIC = Pattern.compile("\\$\\{(.*?)\\}");
	private static final Pattern PATTERN_DYNAMIC = Pattern.compile("#\\{(.*?)\\}");

	/**
	 * Private constructor.
	 */
	private PropertyService() {
		// Just prevent call to constructor
	}

	/**
	 * Set a property.
	 * 
	 * @param key
	 *            key of the property to set
	 * @param value
	 *            value of the property to set. Can contains ${} or #{} property
	 *            link.
	 */
	public static void setProperty(final String key, final String value) {
		setProperty(key, value, true);
	}

	/**
	 * Set a property.
	 * 
	 * @param key
	 *            key of the property to set
	 * @param value
	 *            value of the property to set. Can contains ${} or #{} property
	 *            link.
	 * @param resolve
	 *            use property resolution if true.
	 */
	public static void setProperty(final String key, final String value, final boolean resolve) {
		if (INT_MAP.containsKey(key)) {
			INT_MAP.remove(key);
		}
		PropertySpec spec = new PropertySpec();
		spec.setName(key);
		spec.setInitialeValue(value);
		spec.setResolvedValue(value);
		if (resolve) {
			if (isDynamic(value)) {
				spec.setNature(PropertyNatureType.DYNAMIC);
			} else {
				spec.setNature(PropertyNatureType.STATIC);
			}
			resolve(spec);
		} else {
			spec.setNature(PropertyNatureType.STATIC);
			spec.setState(PropertyStateType.RESOLVED);
		}
		INT_MAP.put(key, spec);
	}

	/**
	 * Resolve property link for $ and # references.
	 * 
	 * @param spec
	 *            the current property spec
	 * @return the property spec resolved
	 */
	private static PropertySpec resolve(final PropertySpec spec) {
		PropertySpec result = resolveStatic(spec);
		result = resolveDynamic(result);
		return result;
	}

	/**
	 * Resolve property link for # references.
	 * 
	 * @param spec
	 *            the current property spec
	 * @return the property spec resolved
	 */
	private static PropertySpec resolveDynamic(final PropertySpec spec) {
		spec.setDynamicValue(spec.getResolvedValue());
		Matcher m = PATTERN_DYNAMIC.matcher(spec.getDynamicValue());
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String subName = m.group(1);
			String subSpec = getProperty(subName);
			if (subSpec == null) {
				break;
			} else {
				m.appendReplacement(sb, subSpec);
			}

		}
		m.appendTail(sb);
		spec.setDynamicValue(sb.toString());
		return spec;
	}

	/**
	 * Resolve property link for $ references.
	 * 
	 * @param spec
	 *            the current property spec
	 * @return the property spec resolved
	 */
	private static PropertySpec resolveStatic(final PropertySpec spec) {
		Matcher m = PATTERN_STATIC.matcher(spec.getResolvedValue());
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String subName = m.group(1);
			String subSpec = getProperty(subName);
			if (subSpec == null) {
				break;
			} else {
				m.appendReplacement(sb, subSpec);
				spec.setState(PropertyStateType.PARTIAL_RESOLVED);
			}

		}
		m.appendTail(sb);
		spec.setResolvedValue(sb.toString());
		// Check if full pattern is resolved
		m = PATTERN_STATIC.matcher(spec.getResolvedValue());
		if (!m.find()) {
			spec.setState(PropertyStateType.RESOLVED);
		}
		return spec;
	}

	/**
	 * Test if the String is dynamic or not.
	 * 
	 * @param value
	 *            value to test
	 * @return true is string contains #{} pattern
	 */
	private static boolean isDynamic(final String value) {
		if (PATTERN_DYNAMIC.matcher(value).find()) {
			return true;
		}
		return false;
	}

	/**
	 * Return the property value identified by key.
	 * 
	 * @param key
	 *            key of the property
	 * @return value of the property if found else, return null
	 */
	public static String getProperty(final String key) {
		PropertySpec spec = INT_MAP.get(key);
		String value = null;
		if (spec != null) {
			if (spec.getNature() == PropertyNatureType.DYNAMIC) {
				value = resolve(spec).getDynamicValue();
			} else {
				if (spec.getState() == PropertyStateType.RESOLVED) {
					value = spec.getResolvedValue();
				} else {
					value = resolve(spec).getResolvedValue();
				}
			}
		}
		return value;
	}

	/**
	 * Clear all data and restore ENV and System.properties.
	 */
	public static void clearAll() {
		INT_MAP.clear();
		init();
	}

	/**
	 * Add ENV and System.properties to cache
	 */
	private static void init() {
		Map<String, String> env = System.getenv();
		for (String key : env.keySet()) {
			String value = env.get(key);
			setProperty(key, value, false);
		}
		Properties sysProp = System.getProperties();
		for (Object key : sysProp.keySet()) {
			String value = sysProp.getProperty((String) key);
			setProperty((String) key, value, false);

		}
	}

	/**
	 * Load properties from file.
	 * 
	 * @param inputStream
	 * @throws IOException
	 *             IOException must be managed by caller.
	 */
	public static void loadFromFile(InputStream inputStream) throws IOException {
		Properties prop = new Properties();
		prop.load(inputStream);

		for (Object key : prop.keySet()) {
			String value = prop.getProperty((String) key);
			setProperty((String) key, value);
		}

	}


}
