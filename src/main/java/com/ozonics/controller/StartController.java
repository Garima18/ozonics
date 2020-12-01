package com.ozonics.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.gson.Gson;
import com.ozonics.api.TwilioSms;
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
		String sentOTPBytwilio = "1";
		
		//verifying user id and password
		AllBean result = adminDao.verifyUser(username, password);
		Random rand = new Random();

		String id = String.format("%04d", rand.nextInt(100000));
		int updateOTPdb =0;
		
		
		//updating otp and login time in db
		if(result.getCount() > 0) {
			updateOTPdb = adminDao.updateOTPindb(result.getUser_type(), id, result.getUsername());

		}
		//sending OTP with twilio
		if (updateOTPdb== 1) {
			TwilioSms sms = new TwilioSms();
//			sentOTPBytwilio = sms.sendMsgViaTwilio(id, result.getPhone_num());
			System.out.println("Message by twilio:" + sentOTPBytwilio);

		}
		JSONObject myobj = new JSONObject();
	
		
	
		if (sentOTPBytwilio.equals("1")) {
			myobj.put("user_type", result.getUser_type());
			myobj.put("msg", "SUCCESS");
			myobj.put("phone_num", result.getPhone_num());
			myobj.put("username", result.getUsername());
		} else {
			
			myobj.put("msg", "FAILURE");
		}
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}
	  private final Path root = Paths.get("/tmp/reactfiles/");

	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	public void saveImage(HttpServletResponse response, @RequestParam MultipartFile file,
			MultipartHttpServletRequest request) throws IOException {

		String name = file.getOriginalFilename();
		System.out.println(name);
		JSONObject obj = new JSONObject();
//		String file_details = request.getParameter("id");
//		String[] file_arr = file_details.split("/");
//		System.out.println(file_details);
		AllBean bean = new AllBean();
//		bean.setProduct_name(file_arr[0]);
//		bean.setCategory(file_arr[1]);
//		bean.setLevel1(file_arr[2]);
//		bean.setLevel2(file_arr[3]);

//		bean.setLevel3(file_arr[4]);
//		bean.setImage_b64(obj.getString("imageStr"));
//		bean.setFile_name(obj.getString("fileName"));
//		System.out.println("image str:"+bean.getImage_b64());
		
		try {
		      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		      System.out.println("running");
		    } catch (Exception e) {
		      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		    }
		  
//		AllBean result = adminDao.saveMultipartFile(file);
		int saveInDb = 0;
//		if (result.getStatus() == 1) {
//			bean.setFile_name(result.getFile_name());
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

	@RequestMapping("/sendFile")
	public void sendFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String filename = request.getParameter("id");
		response.setContentType("image/png");
		String p = "/home/garima/Documents/reactfiles/" + filename;
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
		request.setCharacterEncoding("UTF-8");
		System.out.println(file.getContentType());
		File savedFile = adminDao.copyFile(file);

	}

	@RequestMapping(value = "/fileUploadPage", method = RequestMethod.POST)
	public String fileUpload(@Validated AllBean file, BindingResult result, ModelMap model) throws IOException {
		if (result.hasErrors()) {
			System.out.println("validation errors");
			return "fileUploadPage";
		} else {
			System.out.println("Fetching file");
			MultipartFile multipartFile = file.getFile();
			// String uploadPath = context.getRealPath("") + File.separator + "temp" +
			// File.separator;
			String uploadPath = "/tmp/reactfiles/";
			System.out.println(uploadPath);

			FileCopyUtils.copy(file.getFile().getBytes(), new File(uploadPath + file.getFile().getOriginalFilename()));
			String fileName = multipartFile.getOriginalFilename();
			model.addAttribute("fileName", fileName);
			return "success";
		}

	}

	@RequestMapping("/searchQuery")
	public void searchQuery(HttpServletRequest request, HttpServletResponse response, @RequestBody String json)
			throws IOException {
		System.out.println("Yess");
		JSONObject obj = new JSONObject(json);
		String searchStr = obj.getString("searchStr");

		JSONArray list = adminDao.searchQuery(searchStr);
//		Gson gson  = new Gson();
//		String str = gson.toJson(list);

//		JSONArray arr = new JSONArray(str);

		JSONObject myobj = new JSONObject();
		myobj.put("msg", "SUCCESS");
		myobj.put("files", list);

		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}


	
	@RequestMapping("verifyOtp")
	public void verifyOtp(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) throws IOException {
		JSONObject obj = new JSONObject(json);
		
		String user_type = obj.getString("user_type");
		int otp = obj.getInt("otp");
		String username = obj.getString("username");
			
		int verifyOtp = adminDao.verifyOTP(user_type, otp, username);
		JSONObject myobj = new JSONObject();
		if(verifyOtp>0) {
			myobj.put("msg", "SUCCESS");
			myobj.put("status", 1);
		}else {
			myobj.put("msg", "FAILURE");
			myobj.put("status", 0);
		}
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}
	
	@RequestMapping("/showAllUsers")
	public void showAllUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<AllBean> list = adminDao.getAllUsers();
		JSONObject myobj = new JSONObject();
			myobj.put("msg", "SUCCESS");
			myobj.put("status", 1);
			myobj.put("data", list);
		
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}
	@RequestMapping("/addUser")
	public void addUser(HttpServletRequest request, HttpServletResponse response, @RequestBody String json)
			throws IOException {

		JSONObject obj = new JSONObject(json);
		AllBean bean = new AllBean();
		bean.setUsername(obj.getString("username"));
		bean.setPassword(obj.getString("password"));
		bean.setPhone_num(obj.getString("phone_num"));
		bean.setSegment(obj.getString("segment"));
		bean.setCategory(obj.getString("category"));
		bean.setSub_category(obj.getString("sub_category"));
		bean.setOperation_type(obj.getString("operation_type"));
		int result =0;
		if(bean.getOperation_type().equals("add")) {
		result = adminDao.addUser(bean);
		}
		else if(bean.getOperation_type().equals("edit")) {
			result = adminDao.editUser(bean);
		}
		JSONObject myobj = new JSONObject();
		if (result == 1) {
			myobj.put("msg", "SUCCESS");
			myobj.put("status", 1);
		} else if (result == 2) {
			myobj.put("msg", "Already Exists");
			myobj.put("status", 2);
		} else {
			myobj.put("msg", "FAILURE");
			myobj.put("status", 0);
		}
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}
	
	@RequestMapping("/showLoginInfo")
	public void showLoginInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<AllBean> list = adminDao.showLoginInfo();
		JSONObject myobj = new JSONObject();
			myobj.put("msg", "SUCCESS");
			myobj.put("status", 1);
			myobj.put("data", list);
		
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		pw.print(myobj);
		pw.flush();
		pw.close();
	}
}
