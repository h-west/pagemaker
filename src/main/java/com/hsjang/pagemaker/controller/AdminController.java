package com.hsjang.pagemaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@RequestMapping("")
	public String admin(Model model) {
		model.addAttribute("title", "타이틀 테스트");
		model.addAttribute("msg", "Hello!!");
		return "admin";
	}
	
	@RequestMapping("/html")
	public void html(Model model) {
		
	}
	
	@RequestMapping("/api")
	public void api(Model model) {
		
	}
}
