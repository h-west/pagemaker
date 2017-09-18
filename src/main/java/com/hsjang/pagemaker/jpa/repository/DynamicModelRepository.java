package com.hsjang.pagemaker.jpa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hsjang.pagemaker.jpa.model.DynamicModel;

public interface DynamicModelRepository extends MongoRepository<DynamicModel,String>{

}
