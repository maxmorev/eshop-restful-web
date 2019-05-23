package ru.maxmorev.restful.eshop.controllers.response;

public class CrudResponse {

    public enum Status{
        OK, FAIL
    }

    public static CrudResponse OK = new CrudResponse(Status.OK);
    public static CrudResponse FAIL = new CrudResponse(Status.FAIL);

    private String status;

    public CrudResponse(Status status){
        this.status = status.name();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
