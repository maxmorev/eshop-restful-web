package ru.maxmorev.restful.eshop.domain;

public class Mail {
    private String to;
    private String subject;
    private String text;

    public Mail to(String to){
        this.to = to;
        return this;
    }

    public Mail subject(String sb){
        this.subject = sb;
        return this;
    }

    public Mail text(String tx){
        this.text = tx;
        return this;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }
}
