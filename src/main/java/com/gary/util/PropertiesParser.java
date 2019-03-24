package com.gary.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesParser {
	private static final Map<String, String> keyValueMap;
	static {
		keyValueMap = new LinkedHashMap<>();
	}
	
	public static void loadProperties(String configFilePath) throws Exception {
		InputStream is = PropertiesParser
				.class.getResourceAsStream(configFilePath);
		loadding(is);
	}
	
	public static void loadProperties(InputStream is) throws Exception {
		loadding(is);
	}
	
	public static void loadProperties(File file) throws Exception {
		InputStream is;
		try {
			is = new FileInputStream(file);
			loadding(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadding(InputStream is) throws Exception {
		if (is == null) {
			throw new Exception("配置文件不存在！");
		}
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		Properties properties = new Properties();
		properties.load(isr);
		Enumeration<Object> keys = properties.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			key = new String(key.getBytes(), "utf-8");
			String value = properties.getProperty(key);
			keyValueMap.put(key, value);
		}
	}
	
	public static Set<String> keySet() {
		return keyValueMap.keySet();
	}
	
	public static String value(String key) {
		return keyValueMap.get(key);
	}
	
}
