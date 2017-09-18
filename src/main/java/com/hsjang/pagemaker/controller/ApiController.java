package com.hsjang.pagemaker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hsjang.pagemaker.common.constants.MapKeys;
import com.hsjang.pagemaker.common.model.Data;
import com.hsjang.pagemaker.service.ApiService;

@Controller
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	ApiService apiService;
	
	@RequestMapping("")
	public void api(Model model) {
	}
	
	@RequestMapping("/{p1}")
	public void api(@RequestParam Map<String, Object> params, @PathVariable String p1, Model model, HttpMethod hm) {
		process(hm, p1, params, model);
	}
	
	@RequestMapping("/{p1}/{p2}")
	public void api(@RequestParam Map<String, Object> params, @PathVariable String p1, @PathVariable String p2, Model model, HttpMethod hm ) {
		process(hm, addKeys(p1,p2), params, model);
	}
	
	@RequestMapping("/{p1}/{p2}/{p3}")
	public void api(@RequestParam Map<String, Object> params, @PathVariable String p1, @PathVariable String p2, @PathVariable String p3, Model model, HttpMethod hm) {
		process(hm, addKeys(p1,p2,p3), params, model);
	}
	
	@RequestMapping("/{p1}/{p2}/{p3}/{p4}")
	public void api(@RequestParam Map<String, Object> params, @PathVariable String p1, @PathVariable String p2, @PathVariable String p3, @PathVariable String p4, Model model, HttpMethod hm) {
		process(hm, addKeys(p1,p2,p3,p4), params, model);
	}
	
	@RequestMapping("/{p1}/{p2}/{p3}/{p4}/{p5}")
	public void api(@RequestParam Map<String, Object> params, @PathVariable String p1, @PathVariable String p2, @PathVariable String p3, @PathVariable String p4, @PathVariable String p5, Model model, HttpMethod hm) {
		process(hm, addKeys(p1,p2,p3,p4,p5), params, model);
	}
	
	private String addKeys(String... keys) {
		return String.join(".", keys);
	}
	
	private void process(HttpMethod hm, String k, Map<String,Object> p, Model m) {
		m.addAllAttributes(apiService.getData(hm, k, new Data(p)));
	}
	
}
