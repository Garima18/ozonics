package com.ozonics.aws.s3.serv;

import org.springframework.web.multipart.MultipartFile;

public interface awsS3Service {
	 void uploadFile(final MultipartFile p0);
}
