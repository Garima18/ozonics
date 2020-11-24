package com.ozonics.dao;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.Constants;
import com.ozonics.aws.s3.config.S3MultipartUpload;
//import com.google.common.io.Files;
import com.ozonics.aws.s3.serv.awsS3ServiceImpl;
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
	int count2 = 0;

	public AllBean verifyUser(String username, String password) {
		final AllBean resultBean = new AllBean();
		
		System.out.println("no part");
		String query = "select username, phone_num from ozonics.login where lower(username) = '" + username.toLowerCase() + "' and password = '"
				+ password + "'";
		String query1 = "Select username, phone_num from ozonics.users where lower(username) = '"+username.toLowerCase()+"' and password = '"+password+"'";
		
	
		List<String> list = template.query(query, new RowMapper() {
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				String str = rs.getString("username");
				resultBean.setUsername(str);
				resultBean.setPhone_num(rs.getString("phone_num"));
				return str;
			}
		});
		System.out.println("list:   "+list.size());
		List<String> list2 = template.query(query1, new RowMapper() {
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				String str = rs.getString("username");
				resultBean.setUsername(str);

				resultBean.setPhone_num(rs.getString("phone_num"));

				return str;
			}
		});
		
		
		System.out.println("count:" + count);
		if(list.size() >0) {
			resultBean.setUser_type("admin");
		}else if(list2.size()>0){
			resultBean.setUser_type("user");
		}
		if (list.size() > 0 || list2.size() >0) {
			resultBean.setCount(1);
			return resultBean;
		} else {
			resultBean.setCount(0);

			return resultBean;
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

			// another way
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
			byte[] bytes = file.getBytes();
			String content = new String(file.getBytes());
			File file_save = new File(dir + File.separator + "" + file.getOriginalFilename());
//			BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(new File(dir + File.separator + ""+file.getOriginalFilename())));
			try {
				BufferedWriter out = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file_save), StandardCharsets.UTF_8));

				out.write(content);
				out.flush();
				out.close();

				resultBean.setStatus(1);
				resultBean.setFile_name(dir + File.separator + file.getOriginalFilename());

			} catch (Exception e) {
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

	public File copyFile(MultipartFile multipart) throws IOException {
//		File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+"pp.jpeg");
//		multipart.transferTo(convFile);

//		String rootpath = System.getProperty("catalina.home");
//		System.out.println(rootpath);
//		File dir = new File("/home/garima/Documents/reactfiles/abc"+File.separator+"tmpfiles");
//
		File serverFile = new File(
				"/home/garima/Documents/reactfiles/abc" + File.separator + multipart.getOriginalFilename());
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		byte[] bytes = multipart.getBytes();
		stream.write(bytes);
		stream.close();

		Path root1 = Paths
				.get("/home/garima/Documents/reactfiles/abc" + File.separator + multipart.getOriginalFilename());
		System.out.println("file:" + Files.probeContentType(root1));

//		FileUtils.copyFile(convFile, dest);

//		File file = new File("/home/garima/Downloads/lido-logo-desktop.jpg");
		String fileBean = null;
		awsS3ServiceImpl service = new awsS3ServiceImpl();
//		fileBean = service.uploadFileImage(multipart, "event");
//		fileBean = service.uploadFileImage(file, "event");

//		System.out.println(fileBean.getFile_name());


        final int UPLOAD_PART_SIZE = 10 * Constants.MB;
		int bytesRead = 0, bytesAdded = 0;
		byte[] data = new byte[10*Constants.MB];
		ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();
		 URL url = new URL("_remote_url_of_uploading_file_");
	     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");

         InputStream inputStream = connection.getInputStream();
		while((bytesRead = inputStream.read(data, 0, bytesRead))!= -1) {
            bufferOutputStream.write(data, 0, bytesRead);
            if (bytesAdded < UPLOAD_PART_SIZE) {
                // continue writing to same output stream unless it's size gets more than UPLOAD_PART_SIZE
                bytesAdded += bytesRead;
                continue;
            }
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

            S3MultipartUpload multipartUpload = new S3MultipartUpload("lido/ozonics/files", "abc.jpeg", s3Client);
            multipartUpload.uploadPartAsync(new ByteArrayInputStream(bufferOutputStream.toByteArray()));
            bufferOutputStream.reset(); // flush the bufferOutputStream
            bytesAdded = 0; 
            
		}
		
		System.out.println("success");

		return null;
	}

	public int addUser(AllBean bean) {
		String check_user = "Select count(*) from ozonics.users where username = '" + bean.getUsername() + "'";
		final AllBean bean1 = new AllBean();
		template.query(check_user, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				bean1.setCount(rs.getInt("count"));
				return null;
			}

		});
		System.out.println("count of user:" + bean1.getCount());
		int status = 0;
		if (bean1.getCount() > 0) {
			// user already exiss
			status = 2;
		} else {
			String query = "insert into ozonics.users(username, password, phone_num, segment, category, sub_category) values ('"
					+ bean.getUsername() + "', '" + bean.getPassword() + "', " + "'" + bean.getPhone_num() + "', '"
					+ bean.getSegment() + "', '"+bean.getCategory()+"', '"+bean.getSub_category()+"')";
			try {
				template.update(query);
				status = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		System.out.println("status:"+status);
		return status;
	}
	
	public JSONArray searchQuery (String searchStr){
		String query = "select file_name from ozonics.files where file_folder like '%"+searchStr+"%'";
		System.out.println(query);
		final JSONArray arr = new JSONArray();
		
		final List<AllBean> list = new ArrayList<AllBean>();
		template.query(query, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				AllBean tempbean = new AllBean();
				tempbean.setFile_name(rs.getString("file_name"));
				arr.put(rs.getString("file_name"));
				list.add(tempbean);
				return null;
			}
			
		});
		System.out.println("File size:"+list.size());
		return arr;
	}
	
public int updateOTPindb(String user_type, String otp, String username) {
	String query = "";
	if(user_type.equals("admin")) {
	query = "update ozonics.login set otp="+otp+" where username = '"+username+"'";
	
	}else {
		query = "update ozonics.users set otp="+otp+" where username = '"+username+"'";
	}
	int status=0;
	try {
		System.out.println(query);
		template.update(query);
		System.out.println("OTP updated successfully");
		status =1;
	}catch(Exception e) {
		System.out.println("OTP update failed");
		e.printStackTrace();
		
	}
	return status;
}
	
	public int verifyOTP(String user_type, int otp, String username) {
		String query = "";
		if(user_type.equals("admin"))
		{
			query = "select count(*)  from ozonics.login where lower(username) = '"+username.toLowerCase()+"' and otp = '"+otp+"' ";
		}else {
			query = "select count(*)    from ozonics.users where lower(username) = '"+username.toLowerCase()+"' and otp = '"+otp+"' ";
		}
		
		List<Integer> list = template.query(query, new RowMapper() {

			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				int count = rs.getInt("count");
				return count;
			}
			
		});
		System.out.println("length of list:"+list.size());
		
		return list.size();
	}
}
