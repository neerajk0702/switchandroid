package com.kredivation.switchland.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyhomeArray {
    private String destinations;

    private String profile_completeness;

    private String location;

    private String bathrooms;

    private String address1;

    private String address2;

    private String travelling_anywhere;

    private String religion;

    private String id;

    private String city_id;

    private String sort_description;

    private String title;

    private String updated_date;

    private String pets;

    private String gender;

    private String user_id;

    private String longitude;

    private String added_date;

    private String status;

    private String profile_image;

    private String zipcode;

    private String country_id;

    private String sleeps;

    private String bedrooms;

    private String property_type;

    private String family_matters;

    private String startdate;

    private String landmarks;

    private String level_security;

    private String traveller_type;

    private String house_no;

    private String home_type;

    private String latitude;

    private String enddate;

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public String getProfile_completeness() {
        return profile_completeness;
    }

    public void setProfile_completeness(String profile_completeness) {
        this.profile_completeness = profile_completeness;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getTravelling_anywhere() {
        return travelling_anywhere;
    }

    public void setTravelling_anywhere(String travelling_anywhere) {
        this.travelling_anywhere = travelling_anywhere;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getSort_description() {
        return sort_description;
    }

    public void setSort_description(String sort_description) {
        this.sort_description = sort_description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getSleeps() {
        return sleeps;
    }

    public void setSleeps(String sleeps) {
        this.sleeps = sleeps;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getFamily_matters() {
        return family_matters;
    }

    public void setFamily_matters(String family_matters) {
        this.family_matters = family_matters;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(String landmarks) {
        this.landmarks = landmarks;
    }

    public String getLevel_security() {
        return level_security;
    }

    public void setLevel_security(String level_security) {
        this.level_security = level_security;
    }

    public String getTraveller_type() {
        return traveller_type;
    }

    public void setTraveller_type(String traveller_type) {
        this.traveller_type = traveller_type;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getHome_type() {
        return home_type;
    }

    public void setHome_type(String home_type) {
        this.home_type = home_type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }


    //----------------local variable use---------
    public List<Features> Featurelist;
    public List<House_rules> ruleList;

    public List<Features> getFeaturelist() {
        return Featurelist;
    }

    public void setFeaturelist(List<Features> featurelist) {
        Featurelist = featurelist;
    }

    public List<House_rules> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<House_rules> ruleList) {
        this.ruleList = ruleList;
    }

    public ArrayList<ChatData> homePhotoList;

    public ArrayList<ChatData> getHomePhotoList() {
        return homePhotoList;
    }

    public void setHomePhotoList(ArrayList<ChatData> homePhotoList) {
        this.homePhotoList = homePhotoList;
    }

    public File profileImfLocal;

    public File getProfileImfLocal() {
        return profileImfLocal;
    }

    public void setProfileImfLocal(File profileImfLocal) {
        this.profileImfLocal = profileImfLocal;
    }

    private String travel_country;
    private String travel_city;
    private String country_name;
    private String city_name;
    private String travel_country_name;
    private String travel_city_name;

    public String getTravel_country() {
        return travel_country;
    }

    public void setTravel_country(String travel_country) {
        this.travel_country = travel_country;
    }

    public String getTravel_city() {
        return travel_city;
    }

    public void setTravel_city(String travel_city) {
        this.travel_city = travel_city;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getTravel_country_name() {
        return travel_country_name;
    }

    public void setTravel_country_name(String travel_country_name) {
        this.travel_country_name = travel_country_name;
    }

    public String getTravel_city_name() {
        return travel_city_name;
    }

    public void setTravel_city_name(String travel_city_name) {
        this.travel_city_name = travel_city_name;
    }

    @Override
    public String toString() {
        return "ClassPojo [destinations = " + destinations + ", profile_completeness = " + profile_completeness + ", location = " + location + ", bathrooms = " + bathrooms + ", address1 = " + address1 + ", address2 = " + address2 + ", travelling_anywhere = " + travelling_anywhere + ", religion = " + religion + ", id = " + id + ", city_id = " + city_id + ", sort_description = " + sort_description + ", title = " + title + ", updated_date = " + updated_date + ", pets = " + pets + ", gender = " + gender + ", user_id = " + user_id + ", longitude = " + longitude + ", added_date = " + added_date + ", status = " + status + ", profile_image = " + profile_image + ", zipcode = " + zipcode + ", country_id = " + country_id + ", sleeps = " + sleeps + ", bedrooms = " + bedrooms + ", property_type = " + property_type + ", family_matters = " + family_matters + ", startdate = " + startdate + ", landmarks = " + landmarks + ", level_security = " + level_security + ", traveller_type = " + traveller_type + ", house_no = " + house_no + ", home_type = " + home_type + ", latitude = " + latitude + ", enddate = " + enddate + "]";
    }

}
