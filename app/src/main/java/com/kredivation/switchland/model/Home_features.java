package com.kredivation.switchland.model;

public class Home_features {
    private String created_date;

    private String id;

    private String updated_date;

    private String status;

    private String name;

    public String getCreated_date ()
    {
        return created_date;
    }

    public void setCreated_date (String created_date)
    {
        this.created_date = created_date;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_date ()
    {
        return updated_date;
    }

    public void setUpdated_date (String updated_date)
    {
        this.updated_date = updated_date;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }
    private boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [created_date = "+created_date+", id = "+id+", updated_date = "+updated_date+", status = "+status+", name = "+name+"]";
    }
}


