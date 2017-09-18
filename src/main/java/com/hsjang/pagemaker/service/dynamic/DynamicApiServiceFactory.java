package com.hsjang.pagemaker.service.dynamic;

import java.util.List;

import org.springframework.util.StringUtils;

@lombok.Data
public class DynamicApiServiceFactory {

	String packageName;
	String className;
	String fullClassName;
	List<String> imports;
	String getLogic;
	String postLogic;
	
	public DynamicApiServiceFactory() {
		super();
	}
	public DynamicApiServiceFactory(String className, List<String> imports, String getLogic, String postLogic) {
		this("com.hsjang.pagemaker.service.dynamic",className,imports,getLogic,postLogic);
	}
	
	public DynamicApiServiceFactory(String packageName, String className, List<String> imports, String getLogic, String postLogic) {
		this.packageName = packageName;
		this.className = className;
		this.imports = imports;
		this.getLogic = getLogic;
		this.postLogic = postLogic;
		this.fullClassName = packageName + "." + className;
	}
	
	public String build(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("package ").append(packageName==null?"com.hsjang.pagemaker.service.dynamic":packageName).append(";").append("\n");
		
		sb.append("import org.springframework.data.mongodb.core.MongoTemplate;").append("\n");
		sb.append("import com.hsjang.pagemaker.common.model.Data;").append("\n");
		sb.append("import com.hsjang.pagemaker.service.DynamicApiService;").append("\n");
		
		if(imports!=null) {
			for(String ipt : imports) {
				if(!ipt.startsWith("import ")) {
					sb.append("import ");
				}
				sb.append(ipt).append("\n");
			}
		}
		
		className = StringUtils.capitalize(className);
		
		sb.append("public class ").append(className).append(" implements DynamicApiService{").append("\n");
		sb.append(" MongoTemplate mt;").append("\n");
		sb.append(" public ").append(className).append("(MongoTemplate mt){").append("\n");
		sb.append("  this.mt=mt;").append("\n");
		sb.append(" }").append("\n");
		sb.append(" @Override").append("\n");
		sb.append(" public Data get(Data p) {").append("\n");
		sb.append("  Data r = new Data();").append("\n");
		
		sb.append(getLogic==null?"":getLogic).append("\n");
		
		sb.append("  return r;").append("\n");
		sb.append("	}").append("\n");
		
		sb.append(" @Override").append("\n");
		sb.append(" public Data post(Data p) {").append("\n");
		sb.append("  Data r = new Data();").append("\n");
		
		sb.append(postLogic==null?"":postLogic).append("\n");
		
		sb.append("  return r;").append("\n");
		sb.append(" }").append("\n");
		sb.append("}").append("\n");
		
		return sb.toString();
	}
}
