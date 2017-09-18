package com.hsjang.pagemaker.jpa.model;


import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Klass {
	
	@Id
	String id;
	String key;
	String name;
	String source;
	String bytecode;
}