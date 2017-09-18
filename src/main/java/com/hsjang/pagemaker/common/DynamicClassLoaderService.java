package com.hsjang.pagemaker.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hsjang.pagemaker.common.model.Data;
import com.hsjang.pagemaker.jpa.model.DynamicApi;
import com.hsjang.pagemaker.jpa.model.DynamicModel;
import com.hsjang.pagemaker.jpa.repository.DynamicApiRepository;
import com.hsjang.pagemaker.jpa.repository.DynamicModelRepository;
import com.hsjang.pagemaker.service.ApiService;
import com.hsjang.pagemaker.service.DynamicApiService;
import com.hsjang.pagemaker.service.dynamic.DynamicApiServiceFactory;
import com.hsjang.pagemaker.service.dynamic.DynamicModelFactory;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DynamicClassLoaderService extends ClassLoader implements InitializingBean {
	
	@Autowired
	DynamicApiRepository dynamicApiRepository;
	
	@Autowired
	DynamicModelRepository dynamicModelRepository;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	MongoConverter converter;

	@Autowired
	ApiService apiService;

	public DynamicClassLoaderService() {
		super(DynamicClassLoaderService.class.getClassLoader());
    }

	/*@Override
	public void afterPropertiesSet() throws Exception {
		cache = new HashMap<String, Class>();

		List<Klass> klasses = repository.findAll();
		for (Klass klass : klasses) {
			String name = klass.getName();
			byte[] bytecode = Base64.decodeBase64(klass.getBytecode());
			cache.put(name, defineClass(name, bytecode, 0, bytecode.length));
		}
		Object a = Class.forName("com.hsjang.pagemaker.dynamic.controller.TestController", true, this).newInstance();
		//com.hsjang.pagemaker.Test test = new Test();
	}*/
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		// test insert
		setTestData();
		
		// set dynamic model 
		setDynamicModels();
		
		// set dynamic api services
		setDynamicApiServices();
	}
	
	public void setDynamicModels() throws Exception{
		List<DynamicModel> modelList = dynamicModelRepository.findAll();
		for(DynamicModel model : modelList) {
			String fullName = model.getFullClassName();
			byte[] bytecode = Base64.decodeBase64(model.getBytecode());
			
			try {
				Class.forName(fullName, true, this);
			} catch( ClassNotFoundException e ) {
				defineClass(fullName, bytecode, 0, bytecode.length);
			}
			
			String collectionName = StringUtils.uncapitalize(model.getClassName());
			if (!mongoTemplate.getCollectionNames().contains(collectionName)) {
			    mongoTemplate.createCollection(collectionName);
			}
		}
	}
	
	public void setDynamicApiServices() throws Exception{
		Map<String, DynamicApiService> dynamicServices = new HashMap<String, DynamicApiService>();
		
		List<DynamicApi> apiList = dynamicApiRepository.findAll();
		for(DynamicApi api : apiList) {
			String name = api.getName();
			byte[] bytecode = Base64.decodeBase64(api.getBytecode());
			defineClass(name, bytecode, 0, bytecode.length);
			dynamicServices.put(api.getKey(), ((Class<? extends DynamicApiService>) Class.forName(name, true, this)).getConstructor(MongoTemplate.class).newInstance(mongoTemplate));
		}
		
		apiService.setDynamicServices(dynamicServices);
	}
	
	public void setTestData() throws Exception{
		dynamicApiRepository.deleteAll();
		dynamicModelRepository.deleteAll();
		
		List<Data> vars = new  ArrayList<Data>();
		vars.add( new Data("type","String").add("name", "testString"));
		
		DynamicModelFactory fac = new DynamicModelFactory("TestModel1",null,vars);
		String str2 = fac.build();
		
		System.out.println("##########################["+fac.getFullClassName()+"]#########################");
		System.out.println(str2);
		System.out.println("#############################################################################");
		
		DynamicCompiler c = new DynamicCompiler();
		c.cook(str2);
		
		String name2 =  fac.getFullClassName();
		
		DynamicModel m = new DynamicModel();
		m.setFullClassName(name2);
		m.setClassName(fac.getClassName());
		m.setSource(str2);
		m.setBytecode( Base64.encodeBase64String(c.getBytecode(name2)) );
		
		dynamicModelRepository.save(m);
		
		
		setDynamicModels();
		
		
		List<String> imports = new  ArrayList<String>();
		imports.add("import com.hsjang.pagemaker.jpa.model.Klass;");
		imports.add("import com.hsjang.pagemaker.service.dynamic.model.TestModel1;");
		
		
		StringBuilder ssb = new StringBuilder();
		ssb.append("TestModel1 tm=new TestModel1();").append("\n");
		ssb.append("tm.setTestString(\"tttttt\");").append("\n");
		ssb.append("System.out.println(\"############\");").append("\n");
		//ssb.append("mt.save(tm);").append("\n");
		ssb.append("r.put(\"test111\",mt.findAll(Klass.class));").append("\n");
		
		DynamicApiServiceFactory fac2 = new DynamicApiServiceFactory("Test",imports,ssb.toString(),null);
		String str = fac2.build();
		
		System.out.println("##########################["+fac2.getFullClassName()+"]#########################");
		System.out.println(str);
		System.out.println("#############################################################################");
		
		//DynamicCompiler c = new DynamicCompiler();
		c.setParentClassLoader(this);
		c.cook(str);
		
		String name =  fac2.getFullClassName();
		
		DynamicApi k = new DynamicApi();
		k.setName(name);
		k.setKey("test");
		k.setSource(str);
		k.setBytecode( Base64.encodeBase64String(c.getBytecode(name)) );
		
		
		dynamicApiRepository.save(k);
		
		
		/*org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation<T, ID>*/
		
/*		org.springframework.data.mongodb.repository.support.MongoRepositoryFactory*/
		/*org.springframework.data.mongodb.repository.support.SimpleMongoRepository<T, ID>
		DefaultEntityInformationCreator*/
	}
	
}
