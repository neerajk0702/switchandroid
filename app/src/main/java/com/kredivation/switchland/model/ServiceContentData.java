package com.kredivation.switchland.model;

public class ServiceContentData {
    private int responseCode;

    private int error;

    private Data data;

    private String service;

    private String msg;

    private boolean success;

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public String getService ()
    {
        return service;
    }

    public void setService (String service)
    {
        this.service = service;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
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

    @Override
    public String toString()
    {
        return "ClassPojo [responseCode = "+responseCode+", error = "+error+", data = "+data+", service = "+service+", msg = "+msg+", success = "+success+"]";
    }
}
