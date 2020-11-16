package com.ozonics.aws.s3.config;

import java.util.List;

import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.google.api.client.util.Value;

public class awsS3Config {


	   @Value("${aws.access_key_id}")
	    private String accessKeyId;
	    @Value("${aws.secret_access_key}")
	    private String secretAccessKey;
	    @Value("${aws.s3.region}")
	    private String region;
	    
	    public awsS3Config() {
	    	
	        this.accessKeyId = "AKIA2OA57QFWUBVCVKSW";
//	        this.accessKeyId = "AKIATJPYHMGV2SMRJPHN";   //for test
	     
	        this.secretAccessKey = "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";
//	        this.secretAccessKey = "lxBaGamnOPTzq6gpqp1CpmFFnGlPM9p1C0IyJ22f";     //for test

	        this.region = "Asia Pacific (Mumbai)";
	        
	        
	        //new key: AKIAJXQURPH2VYDTCPZQ
	        // secret: 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

	    }
	    
	    
	    @Bean
	    public AmazonS3 getAmazonS3Cient() {
	        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(this.accessKeyId, this.secretAccessKey);
	        final AmazonS3Client client = new AmazonS3Client((AWSCredentials)basicAWSCredentials);
	        final List<Bucket> buckets = (List<Bucket>)client.listBuckets();
	        System.out.println("Your Amazon S3 buckets are:");
	        for (final Bucket b : buckets) {
	            System.out.println("* " + b.getName());
	        }
	        return (AmazonS3)((AmazonS3ClientBuilder)((AmazonS3ClientBuilder)AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(this.region))).withCredentials((AWSCredentialsProvider)new AWSStaticCredentialsProvider((AWSCredentials)basicAWSCredentials))).build();
	    }

}
