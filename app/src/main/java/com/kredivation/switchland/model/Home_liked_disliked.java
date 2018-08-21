package com.kredivation.switchland.model;

public class Home_liked_disliked {
    private String tender_status;

    private String user_id;

    private String home_id;

    public String getTender_status ()
    {
        return tender_status;
    }

    public void setTender_status (String tender_status)
    {
        this.tender_status = tender_status;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
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
        return "ClassPojo [tender_status = "+tender_status+", user_id = "+user_id+", home_id = "+home_id+"]";
    }
}


