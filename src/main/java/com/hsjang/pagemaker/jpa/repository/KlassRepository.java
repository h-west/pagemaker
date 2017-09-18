package com.hsjang.pagemaker.jpa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hsjang.pagemaker.jpa.model.Klass;
import com.hsjang.pagemaker.jpa.model.User;

public interface KlassRepository extends MongoRepository<Klass,String>{

	public Klass findByKey(String key);
}
