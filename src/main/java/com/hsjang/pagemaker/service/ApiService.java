package com.hsjang.pagemaker.service;

import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.hsjang.pagemaker.common.model.Data;

@Service
public class ApiService {
	
	Map<String, DynamicApiService> dynamicServices;
	
	public Data getData(HttpMethod hm, String key, Data params) {
		switch(hm) {
		case GET:
			return dynamicServices.containsKey(key)?dynamicServices.get(key).get(params):null;
		case POST:
			return dynamicServices.containsKey(key)?dynamicServices.get(key).post(params):null;
		default:
			return null;
		}
	}
	
	public void setDynamicServices(Map<String, DynamicApiService> dynamicServices) {
		this.dynamicServices = dynamicServices;
	}
}
