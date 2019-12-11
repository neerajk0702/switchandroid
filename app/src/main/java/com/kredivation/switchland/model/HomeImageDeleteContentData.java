package com.kredivation.switchland.model;

public class HomeImageDeleteContentData {
    int responseCode;
    int error;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    boolean success;
    String msg;
    String service;

}
