package com.kredivation.switchland.model;

public class Data {
    private String created_date;

    private String role_id;

    private String status;

    private String user_type;

    private String profile_image;

    private String is_verify;

    private String last_login;

    private String id;

    private String first_name;

    private String username;

    private String updated_date;

    private String mobile_number;

    private String added_by;

    private String email;

    private String last_name;

    private String otp;

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getIs_verify() {
        return is_verify;
    }

    public void setIs_verify(String is_verify) {
        this.is_verify = is_verify;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    private House_rules[] house_rules;

    private Pets_allowed[] pets_allowed;

    private Bathrooms[] bathrooms;

    private Sleeps[] sleeps;

    private Security[] security;

    private Bedrooms[] bedrooms;

    private Type_of_property[] type_of_property;

    private Country[] country;
    private City[] city;

    private Religion[] religion;

    private Type_of_traveller[] type_of_traveller;

    private Family[] family;

    private Month[] month;

    private Home_style[] home_style;

    private Features[] features;

    private Year[] year;

    public House_rules[] getHouse_rules() {
        return house_rules;
    }

    public void setHouse_rules(House_rules[] house_rules) {
        this.house_rules = house_rules;
    }

    public Pets_allowed[] getPets_allowed() {
        return pets_allowed;
    }

    public void setPets_allowed(Pets_allowed[] pets_allowed) {
        this.pets_allowed = pets_allowed;
    }

    public Bathrooms[] getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Bathrooms[] bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Sleeps[] getSleeps() {
        return sleeps;
    }

    public void setSleeps(Sleeps[] sleeps) {
        this.sleeps = sleeps;
    }

    public Security[] getSecurity() {
        return security;
    }

    public void setSecurity(Security[] security) {
        this.security = security;
    }

    public Bedrooms[] getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Bedrooms[] bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Type_of_property[] getType_of_property() {
        return type_of_property;
    }

    public void setType_of_property(Type_of_property[] type_of_property) {
        this.type_of_property = type_of_property;
    }

    public Country[] getCountry() {
        return country;
    }

    public void setCountry(Country[] country) {
        this.country = country;
    }

    public Religion[] getReligion() {
        return religion;
    }

    public void setReligion(Religion[] religion) {
        this.religion = religion;
    }

    public Type_of_traveller[] getType_of_traveller() {
        return type_of_traveller;
    }

    public void setType_of_traveller(Type_of_traveller[] type_of_traveller) {
        this.type_of_traveller = type_of_traveller;
    }

    public Family[] getFamily() {
        return family;
    }

    public void setFamily(Family[] family) {
        this.family = family;
    }

    public Month[] getMonth() {
        return month;
    }

    public void setMonth(Month[] month) {
        this.month = month;
    }

    public Home_style[] getHome_style() {
        return home_style;
    }

    public void setHome_style(Home_style[] home_style) {
        this.home_style = home_style;
    }

    public Features[] getFeatures() {
        return features;
    }

    public void setFeatures(Features[] features) {
        this.features = features;
    }

    public Year[] getYear() {
        return year;
    }

    public void setYear(Year[] year) {
        this.year = year;
    }

    private int is_home_available;

    public int getIs_home_available() {
        return is_home_available;
    }

    public void setIs_home_available(int is_home_available) {
        this.is_home_available = is_home_available;
    }

    public City[] getCity() {
        return city;
    }

    public void setCity(City[] city) {
        this.city = city;
    }

    private String full_name;
    private String address_line_1;
    private String address_line_2;
    private String zipcode;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    private String country_id;
    private String city_id;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    private Genderarray[] genderarray;
    private String gender;

    public Genderarray[] getGenderarray() {
        return genderarray;
    }

    public void setGenderarray(Genderarray[] genderarray) {
        this.genderarray = genderarray;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private LikedmychoiceArray[] likedmychoiceArray;

    private MyhomeArray[] myhomeArray;

    private MychoiceArray[] mychoiceArray;

    public LikedmychoiceArray[] getLikedmychoiceArray() {
        return likedmychoiceArray;
    }

    public void setLikedmychoiceArray(LikedmychoiceArray[] likedmychoiceArray) {
        this.likedmychoiceArray = likedmychoiceArray;
    }

    public MyhomeArray[] getMyhomeArray() {
        return myhomeArray;
    }

    public void setMyhomeArray(MyhomeArray[] myhomeArray) {
        this.myhomeArray = myhomeArray;
    }

    public MychoiceArray[] getMychoiceArray() {
        return mychoiceArray;
    }

    public void setMychoiceArray(MychoiceArray[] mychoiceArray) {
        this.mychoiceArray = mychoiceArray;
    }

    private Home_data[] home_data;

    private Home_liked_disliked[] home_liked_disliked;

    public Home_data[] getHome_data() {
        return home_data;
    }

    public void setHome_data(Home_data[] home_data) {
        this.home_data = home_data;
    }

    public Home_liked_disliked[] getHome_liked_disliked() {
        return home_liked_disliked;
    }

    public void setHome_liked_disliked(Home_liked_disliked[] home_liked_disliked) {
        this.home_liked_disliked = home_liked_disliked;
    }

//--------------how its work--------------

    private String title_two;

    private String description_main_one;

    private String image_main_two;

    private String image_main_three;

    private String title_main_one;

    private String title_main_two;

    private String title_one;

    private String description_three;

    private String title_main_three;

    private String image_main_one;

    private String description_main_two;

    private String title_three;

    private String main_title;

    private String description_main_three;

    private String description_one;

    private String description_two;

    public String getTitle_two() {
        return title_two;
    }

    public void setTitle_two(String title_two) {
        this.title_two = title_two;
    }

    public String getDescription_main_one() {
        return description_main_one;
    }

    public void setDescription_main_one(String description_main_one) {
        this.description_main_one = description_main_one;
    }

    public String getImage_main_two() {
        return image_main_two;
    }

    public void setImage_main_two(String image_main_two) {
        this.image_main_two = image_main_two;
    }

    public String getImage_main_three() {
        return image_main_three;
    }

    public void setImage_main_three(String image_main_three) {
        this.image_main_three = image_main_three;
    }

    public String getTitle_main_one() {
        return title_main_one;
    }

    public void setTitle_main_one(String title_main_one) {
        this.title_main_one = title_main_one;
    }

    public String getTitle_main_two() {
        return title_main_two;
    }

    public void setTitle_main_two(String title_main_two) {
        this.title_main_two = title_main_two;
    }

    public String getTitle_one() {
        return title_one;
    }

    public void setTitle_one(String title_one) {
        this.title_one = title_one;
    }

    public String getDescription_three() {
        return description_three;
    }

    public void setDescription_three(String description_three) {
        this.description_three = description_three;
    }

    public String getTitle_main_three() {
        return title_main_three;
    }

    public void setTitle_main_three(String title_main_three) {
        this.title_main_three = title_main_three;
    }

    public String getImage_main_one() {
        return image_main_one;
    }

    public void setImage_main_one(String image_main_one) {
        this.image_main_one = image_main_one;
    }

    public String getDescription_main_two() {
        return description_main_two;
    }

    public void setDescription_main_two(String description_main_two) {
        this.description_main_two = description_main_two;
    }

    public String getTitle_three() {
        return title_three;
    }

    public void setTitle_three(String title_three) {
        this.title_three = title_three;
    }

    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public String getDescription_main_three() {
        return description_main_three;
    }

    public void setDescription_main_three(String description_main_three) {
        this.description_main_three = description_main_three;
    }

    public String getDescription_one() {
        return description_one;
    }

    public void setDescription_one(String description_one) {
        this.description_one = description_one;
    }

    public String getDescription_two() {
        return description_two;
    }

    public void setDescription_two(String description_two) {
        this.description_two = description_two;
    }

    private String city_name;
    private String user_id;
    private String country_name;
    private String home_id;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getHome_id() {
        return home_id;
    }

    public void setHome_id(String home_id) {
        this.home_id = home_id;
    }

    //---------------chat--------------
    private String from_user_id;
    private String password;
    private String to_status;
    private String to_user_id;
    private String from_status;
    private String message_image;
    private String form_user_delete_id;
    private String msg_add_date;
    private String message_id;
    private String message;
    private String to_user_delete_id;

    public String getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(String from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTo_status() {
        return to_status;
    }

    public void setTo_status(String to_status) {
        this.to_status = to_status;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getFrom_status() {
        return from_status;
    }

    public void setFrom_status(String from_status) {
        this.from_status = from_status;
    }

    public String getMessage_image() {
        return message_image;
    }

    public void setMessage_image(String message_image) {
        this.message_image = message_image;
    }

    public String getForm_user_delete_id() {
        return form_user_delete_id;
    }

    public void setForm_user_delete_id(String form_user_delete_id) {
        this.form_user_delete_id = form_user_delete_id;
    }

    public String getMsg_add_date() {
        return msg_add_date;
    }

    public void setMsg_add_date(String msg_add_date) {
        this.msg_add_date = msg_add_date;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo_user_delete_id() {
        return to_user_delete_id;
    }

    public void setTo_user_delete_id(String to_user_delete_id) {
        this.to_user_delete_id = to_user_delete_id;
    }
    private String added_date;

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    @Override
    public String toString() {
        return "ClassPojo [created_date = " + created_date + ", role_id = " + role_id + ", status = " + status + ", user_type = " + user_type + ", profile_image = " + profile_image + ", is_verify = " + is_verify + ", last_login = " + last_login + ", id = " + id + ", first_name = " + first_name + ", username = " + username + ", updated_date = " + updated_date + ", mobile_number = " + mobile_number + ", added_by = " + added_by + ", email = " + email + ", last_name = " + last_name + ", otp = " + otp + "]";
    }
}


