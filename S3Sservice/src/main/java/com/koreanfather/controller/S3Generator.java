package com.koreanfather.controller;

import java.io.IOException;
import java.net.URL;	

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreanfather.service.impl.S3URLImpl;


//controller
@Controller
public class S3Generator {

	//service
	@Autowired
	private S3URLImpl s3i;
	
	//mapping
	@GetMapping("/s3/{imgName}")
	public ResponseEntity<URL> uploadS3(@PathVariable String imgName) throws IOException {
		System.out.println(imgName);
		return new ResponseEntity<URL>(s3i.getS3Link(imgName),HttpStatus.OK);
	}
}
