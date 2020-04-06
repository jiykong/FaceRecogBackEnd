package com.koreanfather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.koreanfather.service.impl.CompareServiceImpl;

@Controller
public class CompareInfo {
	
	@Autowired
	private CompareServiceImpl csi;
	
	@PostMapping("/comp")
	public ResponseEntity<Double> compare2Img(@RequestParam("file") String file, @RequestParam("file2") String file2) {
		double a = csi.Compare(file,file2);
		return new ResponseEntity<Double>(a,HttpStatus.OK);
	}
}
