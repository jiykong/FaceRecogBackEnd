package com.koreanfather.service;

import java.io.IOException;
import java.net.URL;

public interface S3URL {
	public URL getS3Link(String imgName) throws IOException;
}
