package com.hsjang.pagemaker.service;

import com.hsjang.pagemaker.common.model.Data;

public interface DynamicApiService {
	
	public Data get(Data params);
	public Data post(Data params);
}
