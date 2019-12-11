package com.kredivation.switchland.model;

public class ServiceContentData {
    private int responseCode;
    private int id;
    private int error;

    private Data data;

    private String service;

    private String msg;
    private int is_home_available;
    private boolean success;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public int getIs_home_available() {
        return is_home_available;
    }

    public void setIs_home_available(int is_home_available) {
        this.is_home_available = is_home_available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

private int total_homes;

    public int getTotal_homes() {
        return total_homes;
    }

    public void setTotal_homes(int total_homes) {
        this.total_homes = total_homes;
    }

    @Override
    public String toString() {
        return "ClassPojo [responseCode = " + responseCode + ", error = " + error + ", data = " + data + ", service = " + service + ", msg = " + msg + ", success = " + success + "]";
    }
}
