package com.hsjang.pagemaker.jpa.model;


import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class DynamicModel {
	
	@Id
	String id;
	String className;
	String fullClassName;
	String source;
	String bytecode;
}