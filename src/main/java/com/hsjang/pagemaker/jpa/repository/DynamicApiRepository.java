package com.hsjang.pagemaker.jpa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hsjang.pagemaker.jpa.model.DynamicApi;

public interface DynamicApiRepository extends MongoRepository<DynamicApi,String>{

}
