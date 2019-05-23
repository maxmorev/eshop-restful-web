package ru.maxmorev.restful.eshop.controllers.response;

public class ErrorInfo {

    private final String status = "error";
    private final String url;
    private final String message;

    public ErrorInfo(String url, Exception message) {
        this.url = url;
        this.message = message.getLocalizedMessage();
    }

    public String getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }
}
