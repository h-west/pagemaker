package com.hsjang.pagemaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {
	
	@RequestMapping("")
	public void page(Model model) {
		
	}
	
	@RequestMapping("/{p1}")
	public void page(@PathVariable String p1, Model model) {
		
	}
	
	@RequestMapping("/{p1}/{p2}")
	public void page(@PathVariable String p1, @PathVariable String p2, Model model) {
		
	}
	
	@RequestMapping("/{p1}/{p2}/{p3}")
	public void page(@PathVariable String p1, @PathVariable String p2, @PathVariable String p3, Model model) {
		
	}
	
	@RequestMapping("/{p1}/{p2}/{p3}/{p4}")
	public void page(@PathVariable String p1, @PathVariable String p2, @PathVariable String p3, @PathVariable String p4, Model model) {
		
	}
	
	@RequestMapping("/{p1}/{p2}/{p3}/{p4}/{p5}")
	public void page(@PathVariable String p1, @PathVariable String p2, @PathVariable String p3, @PathVariable String p4, @PathVariable String p5, Model model) {
		
	}
	
	
}
