package com.hsjang.pagemaker.service.dynamic;

import java.util.List;

import org.springframework.util.StringUtils;

import com.hsjang.pagemaker.common.model.Data;

@lombok.Data
public class DynamicModelFactory {
	
	String packageName;
	String className;
	String fullClassName;
	List<String> imports;
	List<Data> vars;
	
	public DynamicModelFactory() {
		super();
	}
	public DynamicModelFactory(String className, List<String> imports, List<Data> vars) {
		this("com.hsjang.pagemaker.service.dynamic.model",className,imports,vars);
	}
	
	public DynamicModelFactory(String packageName, String className, List<String> imports, List<Data> vars) {
		this.packageName = packageName;
		this.className = className;
		this.imports = imports;
		this.vars = vars;
		this.fullClassName = packageName + "." + className;
	}
	
	public String build(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("package ").append(packageName==null?"com.hsjang.pagemaker.service.dynamic.model":packageName).append(";").append("\n");
		
		sb.append("import org.springframework.data.annotation.Id;").append("\n");
		//sb.append("import lombok.Data;").append("\n");
		
		if(imports!=null) {
			for(String ipt : imports) {
				sb.append(ipt).append("\n");
			}
		}
		
		className = StringUtils.capitalize(className);
		
		//sb.append("@Data").append("\n");
		sb.append("public class ").append(className).append(" {").append("\n");
		sb.append(" @Id").append("\n");
		sb.append(" String id;").append("\n");
		
		if(vars!=null) {
			for(Data var : vars) {
				sb.append(var.getString("type")).append(" ").append(var.getString("name")).append(";").append("\n");
			}
		}
		
		sb.append(" public String getId(){return this.id;}").append("\n");
		sb.append(" public void setId(String id){this.id=id;}").append("\n");
		if(vars!=null) {
			for(Data var : vars) {
				String type = var.getString("type");
				String name = var.getString("name");
				String uName = StringUtils.capitalize(var.getString("name"));
				sb.append(" public ").append(type).append(" get").append(uName).append("(){return this.").append(name).append(";}").append("\n");
				sb.append(" public void set").append(uName).append("(String ").append(name).append("){this.").append(name).append("=").append(name).append(";}").append("\n");
			}
		}
		sb.append("	}").append("\n");
		
		return sb.toString();
	}
}
