package com.kredivation.switchland.model;

public class CheckHomeContent {
    private long responseCode;

    private int error;

    private String service;

    private Data data;

    private String msg;

    private boolean success;

    public long getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(long responseCode) {
        this.responseCode = responseCode;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ClassPojo [responseCode = " + responseCode + ", error = " + error + ", service = " + service + ", data = " + data + ", msg = " + msg + ", success = " + success + "]";
    }
}
