package com.kredivation.switchland.model;

public class Homegallery {
    private String id;

    private String photo;

    private String home_id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPhoto ()
    {
        return photo;
    }

    public void setPhoto (String photo)
    {
        this.photo = photo;
    }

    public String getHome_id ()
    {
        return home_id;
    }

    public void setHome_id (String home_id)
    {
        this.home_id = home_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", photo = "+photo+", home_id = "+home_id+"]";
    }
}


