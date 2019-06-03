package ru.maxmorev.restful.eshop.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.maxmorev.restful.eshop.controllers.response.FileUploadResponse;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Date;

@RestController
public class FileUploadController {

    private final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private ApplicationContext applicationContext;

    @PostMapping("/upload/")
    public ResponseEntity<FileUploadResponse> uploadData(@RequestParam("file") MultipartFile file) throws Exception {

        if (file == null) {
            throw new IllegalArgumentException("You must select the a file for uploading");
        }

        InputStream inputStream = file.getInputStream();
        String originalName = file.getOriginalFilename();
        String name = file.getName();
        String contentType = file.getContentType();
        long size = file.getSize();

        logger.info("inputStream: " + inputStream);
        logger.info("originalName: " + originalName);
        logger.info("name: " + name);
        logger.info("contentType: " + contentType);
        logger.info("size: " + size);

        // Do processing with uploaded file data in Service layer
        String imgPath = applicationContext.getEnvironment().getProperty("eShop.image.path");
        logger.info("img path: " + imgPath);

        String fileVersion = Long.toString(new Date().getTime());
        String newName = fileVersion +"_"+ originalName;
        String saveFilePath = imgPath + newName;
        if( Files.exists( Paths.get(saveFilePath), new LinkOption[]{ LinkOption.NOFOLLOW_LINKS}) ){
            Files.delete(Paths.get(saveFilePath));
        }
        Files.copy(inputStream, Paths. get(saveFilePath));
        String serverURL = applicationContext.getEnvironment().getProperty("eShop.image.server");
        FileUploadResponse response = new FileUploadResponse(FileUploadResponse.Status.OK, serverURL+ newName);

        // processing file

        return new ResponseEntity<FileUploadResponse>(response, HttpStatus.OK);
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
