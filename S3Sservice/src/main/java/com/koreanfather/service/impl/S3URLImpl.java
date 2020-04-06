package com.koreanfather.service.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.koreanfather.service.S3URL;

@Service
public class S3URLImpl implements S3URL {

	@Override
	public URL getS3Link(String imgName) throws IOException {

		//environmental variables to user aws services
		
		String accKey = System.getenv("accKey");
		String secretKey = System.getenv("secretKey");
		
		
		//credentials
		BasicAWSCredentials credentials = new BasicAWSCredentials(accKey, secretKey); 
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsStaticCredentialsProvider)
                .withRegion(Regions.US_EAST_2)
                .build();

        
        //setting up the timelimit of the presigned url
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis +=  1000 * 60;
        expiration.setTime(expTimeMillis);
        
        //generating the presigned url
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest("facerektest1", imgName)
        		.withMethod(HttpMethod.PUT)
        		.withExpiration(expiration);
        
		URL presignedURL = s3.generatePresignedUrl(generatePresignedUrlRequest);
		
		//returning presigned url
		System.out.println(presignedURL);
		return presignedURL;
	}

}
