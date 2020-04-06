package com.koreanfather.service.impl;

import org.springframework.stereotype.Service;

import com.koreanfather.service.CompareService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.util.IOUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Service
public class CompareServiceImpl implements CompareService{

	//helper function to round the numbers
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    
	    return bd.doubleValue();
	}
	
	@Override
	public double Compare(String myImgName, String compImgName) {

		//environmental variables
		String accKey = System.getenv("accKey");
		String secretKey = System.getenv("secretKey");

		//credentials
		BasicAWSCredentials credentials = new BasicAWSCredentials(accKey,secretKey);
		
		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

		//getting imgs from s3
        Image source=new Image().withS3Object(new S3Object().withName(myImgName).withBucket("facerektest1"));
        				
        Image target=new Image().withS3Object(new S3Object().withName(compImgName).withBucket("facerektest1"));
		
        //setting iup comparison
        CompareFacesRequest request = new CompareFacesRequest()
                .withSourceImage(source)
                .withTargetImage(target)
                .withSimilarityThreshold(0F);


        CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);
        double result = 0.0;
        
        List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
        
        //comparing the faces
        for (CompareFacesMatch match: faceDetails){
          ComparedFace face= match.getFace();
          BoundingBox position = face.getBoundingBox();
          System.out.println("Face at " + position.getLeft().toString()
                + " " + position.getTop()
                + " matches with " + match.getSimilarity().toString()
                + "% confidence.");
          result = match.getSimilarity();
        }
        
        
        List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();

        System.out.println("There was " + uncompared.size()
             + " face(s) that did not match ");
        
        
        //returning a rounded value
        return round(result,2);
	}

}
