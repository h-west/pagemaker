package com.hsjang.pagemaker.jpa.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hsjang.pagemaker.jpa.model.User;

public interface UserRepository extends MongoRepository<User,String>{
	
	public User findByName(String name);
    public List<User> findByAge(int age);
	
}
