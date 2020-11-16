package com.ozonics.dao;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.ozonics.bean.AllBean;

public class AdminDao {
	JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public int saveDb() {
		String query = "insert into ozonics.login(username) values ('Garima')";
		System.out.println(query);
		template.update(query);
		return 1;
	}

	int count = 0;

	public int verifyUser(String username, String password) {
		System.out.println("no part");
		String query = "select count(*) from ozonics.login where username = '" + username + "' and password = '"
				+ password + "'";

		template.query(query, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				count = rs.getInt("count");
				return null;
			}
		});
		System.out.println("count:" + count);
		if (count > 0) {
			return 1;
		} else {
			return 0;
		}
	}

//	public int saveImage(AllBean bean) throws IOException {
//		String fileb64 = bean.getImage_b64();
//		String file_name = bean.getFile_name();
//		String[] file_arr= fileb64.split(";");
//		String type = file_arr[0].split("/")[1];
//		String b64_str = file_arr[1].split(",")[1];
//		
//		System.out.println("name of file:"+file_name+"       type:"+type);
//		String filename = file_name;
//		File file = new File("/home/garima/Documents/reactfiles/", filename);
//		  if (file.exists())
//		        file.delete();
//		  try {
//		FileOutputStream fos = new FileOutputStream(file);
//		fos.write(Base64.decodeBase64(b64_str));
//		fos.close();
//		  }catch(Exception e) {
//			  e.printStackTrace();
//		  }
//		return 1;
//	}
	final Path root = Paths.get("/home/garima/Documents/reactfiles/abc");

	public AllBean saveMultipartFile(MultipartFile file) throws IOException {
		String file_name = file.getOriginalFilename();
		AllBean resultBean = new AllBean();
//		String type = file_name.split(".")[1];
		
		

		System.out.println("name of file:" + file_name);
		String dir = "/tmp/reactfiles";

		File check_dir = new File(dir);
		if (!check_dir.exists()) {
			check_dir.mkdir();
		}


		StringBuilder fileName = new StringBuilder();
		try {

			// first way
//			Path path =Paths.get(dir, "abc.png");
//			try {
//				OutputStream io = Files.newOutputStream(path);
//				io.write(file.getBytes());
//			}catch(Exception e) {e.printStackTrace();}
//			
			
			//another way
//			fileName.append(file.getOriginalFilename());
//			Files.write(path, file.getBytes());
//			

			// second way
//			String dir1 = dir + File.separator + file_name;
//			InputStream in = file.getInputStream();
//			OutputStream out = new FileOutputStream(dir1);
//			System.out.println("size:" + file.getBytes());
//			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
			
			
			
//			third way
			byte[] bytes =file.getBytes();
			String content = new String(file.getBytes());
			File file_save = new File(dir+File.separator+""+file.getOriginalFilename());
//			BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(new File(dir + File.separator + ""+file.getOriginalFilename())));
			try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(file_save), StandardCharsets.UTF_8));
		
			out.write(content);
			out.flush();
			out.close();
			
			resultBean.setStatus(1);
			resultBean.setFile_name(dir+File.separator+file.getOriginalFilename());
			
			}catch(Exception e) {
				e.printStackTrace();
				resultBean.setStatus(0);
//				resultBean.setFile_name(dir+File.separator+file.getOriginalFilename());
			}
			
//			in.close();
//			out.close();
			// third way
//			String filePath = dir + file.getOriginalFilename();
//			File dest = new File(filePath);
//			file.transferTo(dest);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultBean.setStatus(0);
		}
		System.out.println("status:" + resultBean.getStatus());
		return resultBean;
	}

	public int saveFileInDb(AllBean bean) {

		String query = "insert into ozonics.files(product_name, category, level1, level2, level3, file_name, live_status ) values"
				+ " ('" + bean.getProduct_name() + "', '" + bean.getCategory() + "', '" + bean.getLevel1() + "', '"
				+ bean.getLevel2() + "', 'null', " + "'" + bean.getFile_name() + "', true)";
		int status = 0;
		System.out.println(query);
		try {
			template.update(query);
			status = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public JSONArray sendFilesList(AllBean bean) {
		String query = "Select * from ozonics.files where product_name = '" + bean.getProduct_name()
				+ "' and category = '" + bean.getCategory() + "' and " + "level1 = '" + bean.getLevel1()
				+ "'and level2 = '" + bean.getLevel2() + "'";
		System.out.println(query);
		final JSONArray arr = new JSONArray();
		template.query(query, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				String file_name = rs.getString("file_name");
				arr.put(file_name);
				return null;
			}

		});

		return arr;
	}
	public File copyFile (MultipartFile multipart) throws IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+"pp.jpeg");
//		multipart.transferTo(convFile);
		
		System.out.println("running new");
		
		byte[] bytes = multipart.getBytes();
		String rootpath = System.getProperty("catalina.home");
		System.out.println(rootpath);
		File dir = new File("/home/garima/Documents/reactfiles/abc"+File.separator+"tmpfiles");
//		if (!dir.exists())
//			dir.mkdirs();
		File serverFile = new File("/home/garima/Documents/reactfiles/abc"
				+ File.separator + multipart.getOriginalFilename());
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(serverFile));
		stream.write(bytes);
		stream.close();

//		FileUtils.copyFile(convFile, dest);
		System.out.println("success");
		
		return convFile;
	}
}
