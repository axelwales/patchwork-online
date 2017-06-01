package com.patchwork.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpringHomeController {
	
	@RequestMapping("/spring")
	public String spring() {
		return "HomeSpring";
	}
}
