package ru.maxmorev.restful.eshop.rest.response;

public class FileUploadResponse {

    public enum Status{
        OK, FAIL
    }

    private String status;
    private String uri;

    public FileUploadResponse(FileUploadResponse.Status status, String uri){
        super();
        this.status = status.name();
        this.uri = uri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
