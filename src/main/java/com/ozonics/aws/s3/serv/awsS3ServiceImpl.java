package com.ozonics.aws.s3.serv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
	        bucketName = "lidomall/images";
	    }
	    
	    @Async
	    public AllBean uploadFileImage(final File file, final String category, String bucket_name_ext) {
	        System.out.println("File upload in progress.");
	        String folderName = this.bucketName;
	        int status = 0;
	        try {
//	            final File file = this.convertMultiPartFileToFile(multipartFile);
	            if (category.equals("event")) {
	            	   bucketName = bucketName+bucket_name_ext;
	            	System.out.println(bucket_name_ext+" ______"+bucketName);
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
	        
	        return bean;
	    }
	    
	    @Async
	    public String uploadFile(final MultipartFile multipartFile, final String category) {
	        System.out.println("File upload in progress.");
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
	    
	    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
	        final File file = new File(multipartFile.getOriginalFilename());
	        try {
	            Throwable t = null;
	            try {
	                final FileOutputStream outputStream = new FileOutputStream(file);
	                try {
	                    outputStream.write(multipartFile.getBytes());
	                }
	                finally {
	                    if (outputStream != null) {
	                        outputStream.close();
	                    }
	                }
	            }
	            finally {
	                if (t == null) {
	                    final Throwable exception = null;
	                    t = exception;
	                }
	                else {
	                    final Throwable exception = null;
	                    if (t != exception) {
	                        t.addSuppressed(exception);
	                    }
	                }
	            }
	        }
	        catch (IOException ex) {
	            System.out.println("Error converting the multi-part file to file= " + ex.getMessage());
	        }
	        return file;
	    }
	    
	    private String uploadEventsToS3Bucket(String bucketName, final File file) {
	    	

			
	        final String uniqueFileName =  file.getName();
	        final AmazonS3 s3 = (AmazonS3)((AmazonS3ClientBuilder)AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)).build();
	        try {
	            System.out.println("yes");
//	            bucketName = String.valueOf(bucketName);
	            System.out.println(bucketName);
	            
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
