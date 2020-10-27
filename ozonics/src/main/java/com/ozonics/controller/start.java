package com.ozonics.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@EnableWebMvc

public class start {

	@RequestMapping(value = "/log")
	public void login(HttpServletRequest req, HttpServletResponse respone) {
		
		System.out.println("csdvbk");
	}
}
