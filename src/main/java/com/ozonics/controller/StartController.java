package com.ozonics.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.api.client.http.HttpHeaders;
import com.google.gson.Gson;
import com.ozonics.bean.AllBean;
import com.ozonics.dao.AdminDao;

@Controller
@EnableWebMvc

public class StartController {

	@Autowired
	AdminDao adminDao;

	@RequestMapping("/login")
	public void subscribeMagazine(HttpServletRequest request, HttpServletResponse response, @RequestBody String json)
			throws IOException {

		JSONObject obj = new JSONObject(json);
		String username = obj.getString("username");
		String password = obj.getString("password");

		// send email to above customer, lido and save in db
		System.out.println("Now Done");
		int result = adminDao.verifyUser(username, password);
		JSONObject myobj = new JSONObject();
		if (result == 1) {
			myobj.put("msg", "SUCCESS");
		} else {
			myobj.put("msg", "FAILURE");
		}
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}

	@RequestMapping(value = "/saveImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
	public void saveImage(HttpServletResponse response, @RequestParam MultipartFile file,
			MultipartHttpServletRequest request) throws IOException {

		String name = file.getOriginalFilename();
		System.out.println(name);
		JSONObject obj = new JSONObject();
		String file_details = request.getParameter("id");
		String[] file_arr = file_details.split("/");
		System.out.println(file_details);
		AllBean bean = new AllBean();
		bean.setProduct_name(file_arr[0]);
		bean.setCategory(file_arr[1]);
		bean.setLevel1(file_arr[2]);
		bean.setLevel2(file_arr[3]);

//		bean.setLevel3(file_arr[4]);
//		bean.setImage_b64(obj.getString("imageStr"));
//		bean.setFile_name(obj.getString("fileName"));
//		System.out.println("image str:"+bean.getImage_b64());
		AllBean result = adminDao.saveMultipartFile(file);
		int saveInDb = 0;
		if(result.getStatus() ==1) {
			bean.setFile_name(result.getFile_name());
			saveInDb = adminDao.saveFileInDb(bean);
		}
		JSONObject myobj = new JSONObject();
		myobj.put("msg", "SUCCESS");

		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}

	@RequestMapping("/sendFile")
	public void sendFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String filename = request.getParameter("id");
		   response.setContentType("image/png");
 	        String p = "/home/garima/Documents/reactfiles/"+filename;
	        OutputStream os = response.getOutputStream();
            byte[] encoded = Files.readAllBytes(Paths.get(p));
            FileCopyUtils.copy(encoded, os);
	        os.close();	


		
	}

	@RequestMapping("sendAllFiles")
	public void sendAllFiles(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String file_details = request.getParameter("id");
		AllBean bean = new AllBean();
		String[] file_arr = file_details.split("/");
		System.out.println(file_details);
		bean.setProduct_name(file_arr[0]);
		bean.setCategory(file_arr[1]);
		bean.setLevel1(file_arr[2]);
		bean.setLevel2(file_arr[3]);
//		bean.setLevel3(file_arr[4]);

		JSONArray result = adminDao.sendFilesList(bean);
		Gson gson = new Gson();

		JSONObject myobj = new JSONObject();
		myobj.put("files", result);
		myobj.put("msg", "SUCCESS");

		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}

	public Resource load(String filename) {
		Path root = Paths.get("/home/garima/Downloads/");

		try {
			System.out.println("yes");
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}

	}
	
	@RequestMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
	public void test(HttpServletResponse response, @RequestParam MultipartFile file,
			MultipartHttpServletRequest request) throws IOException {
		System.out.println(file.getOriginalFilename());
			File savedFile = adminDao.copyFile(file);
		
		        
	}
	
}
