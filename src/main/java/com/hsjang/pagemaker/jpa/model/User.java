package com.hsjang.pagemaker.jpa.model;


import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class User {
	
	@Id
	String id;
	String name;
	int age;
}