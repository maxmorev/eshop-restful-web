package ru.maxmorev.restful.eshop.controllers.response;

public class Message {

    public final static String SUCCES="success";
    public final static String ERROR="error";

    private final String status;
    private final String url;
    private final String message;

    public Message(String status, String url, Exception message) {
        this.status = status;
        this.url = url;
        this.message = message.getLocalizedMessage();
    }

    public Message(String status, String message){
        this.status = status;
        this.message = message;
        this.url = null;
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
