package com.pearson.ptb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Test {
	
	
	@RequestMapping(value = "/work", method = RequestMethod.GET)
	public String name() {
		return "Working";
	}

}
