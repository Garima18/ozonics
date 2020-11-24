package com.ozonics.bean;

import org.springframework.web.multipart.MultipartFile;

public class AllBean {
	private String image_b64;
	private String file_name;
	private String product_name;
	private String category;
	private String level1;
	private String level2;
	private String level3;
	private Integer status;
	private String username;
	private String password;
	private String phone_num;
	private String segment;
	private Integer count;
	private String sub_category;
	private MultipartFile file;
	private String user_type;
	

	
	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public MultipartFile getFile() {

		return file;

	}

	public void setFile(MultipartFile file) {

		this.file = file;

	}

	public String getSub_category() {
		return sub_category;
	}

	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLevel1() {
		return level1;
	}

	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	public String getLevel2() {
		return level2;
	}

	public void setLevel2(String level2) {
		this.level2 = level2;
	}

	public String getLevel3() {
		return level3;
	}

	public void setLevel3(String level3) {
		this.level3 = level3;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getImage_b64() {
		return image_b64;
	}

	public void setImage_b64(String image_b64) {
		this.image_b64 = image_b64;
	}

}
