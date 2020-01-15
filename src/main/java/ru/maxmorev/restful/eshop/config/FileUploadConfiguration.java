package ru.maxmorev.restful.eshop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * configuration takes values from environment variables
 */
@Getter
@Setter
@Configuration
public class FileUploadConfiguration {

    @Value("${file_upload_endpoint}")
    private String endpoint;
    @Value("${file_upload_accessKey}")
    private String accessKey;

}
