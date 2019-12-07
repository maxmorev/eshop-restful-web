package ru.maxmorev.restful.eshop.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponse {

    public enum Status{
        OK, FAIL
    }

    private String status;
    private String uri;



}
