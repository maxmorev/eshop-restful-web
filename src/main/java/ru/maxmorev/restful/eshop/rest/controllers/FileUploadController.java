package ru.maxmorev.restful.eshop.rest.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.maxmorev.restful.eshop.config.FileUploadConfiguration;
import ru.maxmorev.restful.eshop.rest.Constants;

import java.io.FileOutputStream;

@Slf4j
@RestController
public class FileUploadController {

    private final FileUploadConfiguration fileUploadConfiguration;

    public FileUploadController(@Autowired FileUploadConfiguration fuc){
        this.fileUploadConfiguration = fuc;
    }

    @PostMapping(value = Constants.REST_PRIVATE_URI+"upload/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> uploadData(@RequestParam("file") MultipartFile file) throws Exception {

        if (file == null) {
            throw new IllegalArgumentException("You must select the a file for uploading");
        }

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        String tempFileName = "/tmp/" + file.getOriginalFilename();
        FileOutputStream fo = new FileOutputStream(tempFileName);
        fo.write(file.getBytes());
        fo.close();
        map.add("file", new FileSystemResource(tempFileName));
        map.add("key", fileUploadConfiguration.getAccessKey());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        log.info("ENDPOINT:"  + fileUploadConfiguration.getEndpoint());
        ResponseEntity<Object> resp = restTemplate.exchange(fileUploadConfiguration.getEndpoint(), HttpMethod.POST, requestEntity, Object.class);
        if(resp.getStatusCode().value()==200){
            log.info("STATUS: OK");
        }else{
            log.info("STATUS: ERROR");
        }
        // processing file

        return new ResponseEntity<Object>(resp.getBody(), resp.getStatusCode());
    }

}
