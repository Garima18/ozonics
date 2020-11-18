package com.ozonics.aws.s3.serv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ozonics.bean.AllBean;

public class awsS3ServiceImpl {

	  @Autowired
//	    private AmazonS3 amazonS3;
	    private String bucketName;
	    
	    public awsS3ServiceImpl() {
	        bucketName = "lido/ozonics/files";
	    }
	    
	    @Async
	    public String uploadFileImage(final MultipartFile multipartFile, final String category) throws IOException {
	        System.out.println("File upload in progress.");
	        String folderName = this.bucketName;
	        int status = 0;
	        try {
	            final File file = this.convertMultiPartFileToFile(multipartFile);
	            if (category.equals("event")) {
	            	System.out.println(bucketName);
	                folderName = this.uploadEventsToS3Bucket(bucketName, file);
	            }
	            System.out.println("File upload is completed.");
	            file.delete();
	            status =1;
	        }
	        catch (AmazonServiceException ex) {
	            System.out.println("File upload is failed.");
	            System.out.println("Error=  while uploading file." + ex.getMessage());
	        }
	        AllBean bean = new AllBean();
	        bean.setStatus(status);
	        bean.setFile_name(folderName.replace("lido/", ""));
	        
	        return null;
	    }
	    
	    @Async
	    public String uploadFile(final MultipartFile multipartFile, final String category) throws IOException {
	        System.out.println("multipart upload in progress.");
	        String folderName = this.bucketName;
	        try {
	            final File file = this.convertMultiPartFileToFile(multipartFile);
	                System.out.println("File upload is completed.");
	            file.delete();
	        }
	        catch (AmazonServiceException ex) {
	            System.out.println("File upload is failed.");
	            System.out.println("Error=  while uploading file." + ex.getMessage());
	        }
	        return folderName;
	    }
	    
	    private File convertMultiPartFileToFile(final MultipartFile multipartFile) throws IOException {
	    	 File convFile = new File(multipartFile.getOriginalFilename());
	    	    FileOutputStream fos = new FileOutputStream(convFile);
	    	
	    	    fos.write(multipartFile.getBytes());
	    	    fos.close();
	    	    return convFile;
	    }
	    
	    private String uploadEventsToS3Bucket(String bucketName, final File file) {
	    	

			
	        final String uniqueFileName =  file.getName();
	        final AmazonS3 s3 = (AmazonS3)((AmazonS3ClientBuilder)AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)).build();
	        try {
	            System.out.println("yes");
//	            bucketName = String.valueOf(bucketName);
	            
	            final PutObjectRequest por = new PutObjectRequest(bucketName, uniqueFileName, file);
	            por.setCannedAcl(CannedAccessControlList.PublicReadWrite);
	            s3.putObject(por);
//	        	Set<Permission> permissionList = new HashSet<>();
//
//	        	 AccessControlList acl = s3.getBucketAcl(bucketName);
//	        	    List<Grant> grants = acl.getGrantsAsList();
//	        	    for (Grant grant : grants) {
//	        	        System.out.format("  %s: %s\n", grant.getGrantee().getIdentifier(),
//	        	                grant.getPermission().toString());
//	        	    }
	        }
	        catch (Exception e) {
	            System.out.println("no");
	            e.printStackTrace();
	        }
	        return bucketName+"/"+uniqueFileName;
	    }
}
