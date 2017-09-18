package com.hsjang.pagemaker.common.model;

import java.util.HashMap;
import java.util.Map;

public class Data extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Data() {
		super();
	}
	
	public Data(Map<String, Object> map) {
		super(map);
	}
	
	public Data(String key, Object obj) {
		super();
		put(key, obj);
	}
	
	public Data add(String key, Object obj) {
		put(key, obj);
		return this;
	}
	
	public String getString(String key) {
		return getString(key,"");
	}
	
	public String getString(String key, String def) {
		return containsKey(key)? get(key).toString() : def;
	}
	
}
