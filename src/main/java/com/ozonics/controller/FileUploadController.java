package com.ozonics.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ozonics.bean.StorageService;

@Controller
@EnableWebMvc
public class FileUploadController {

 StorageService storageService;

//
//	public FileUploadController(StorageService storageService) {
//		this.storageService = storageService;
//	}
	@RequestMapping("/addImage")
	public void addImage(@RequestParam("file") MultipartFile file,  MultipartHttpServletRequest request) throws IOException{
		System.out.println(file.getBytes());
		
		storageService.store(file);
		
	}
	

}
