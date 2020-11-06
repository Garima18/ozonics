package com.ozonics.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ozonics.bean.AllBean;
import com.ozonics.dao.AdminDao;

@Controller
@EnableWebMvc

public class StartController {

	@Autowired
	AdminDao adminDao;
	
	@RequestMapping("/login")
	public void subscribeMagazine(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) throws IOException {
		
		JSONObject obj = new JSONObject(json);
		String username  = obj.getString("username");
		String password = obj.getString("password");
		
		// send email to above customer, lido and save in db
		System.out.println("Now Done");
		int result = adminDao.verifyUser(username, password);
		JSONObject myobj = new JSONObject();
		if(result == 1) {
			myobj.put("msg", "SUCCESS");
		}else {
			myobj.put("msg", "FAILURE");
		}
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}
	
	@RequestMapping(value="/saveImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
	public void saveImage(HttpServletResponse response, @RequestParam("file") MultipartFile file, MultipartHttpServletRequest request) throws IOException {
		
		String name = file.getOriginalFilename();
		System.out.println(name);
		JSONObject obj = new JSONObject();
		String file_details = request.getParameter("id");
		String[] file_arr = file_details.split("/");
		
		AllBean bean = new AllBean();
		bean.setProduct_name(file_arr[0]);
		bean.setCategory(file_arr[1]);
		bean.setLevel1(file_arr[2]);
		bean.setLevel2(file_arr[3]);
		bean.setLevel3(file_arr[4]);
//		bean.setImage_b64(obj.getString("imageStr"));
//		bean.setFile_name(obj.getString("fileName"));
//		System.out.println("image str:"+bean.getImage_b64());
		int result = adminDao.saveMultipartFile(file);
		int saveInDb =0;
//		if(result ==1) {
//			saveInDb = adminDao.saveFileInDb(bean);
//		}
		JSONObject myobj = new JSONObject();
			myobj.put("msg", "SUCCESS");
		
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}
	
	
}
