package com.kredivation.switchland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Home_features;
import com.kredivation.switchland.model.Home_rules;
import com.kredivation.switchland.model.Homegallery;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.model.MychoiceArray;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;

import java.util.ArrayList;

public class SwitchDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Switch_DB";

    public SwitchDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_userInfo_TABLE = "CREATE TABLE userInfo(id TEXT,first_name TEXT, last_name TEXT,email TEXT,username TEXT,mobile_number TEXT,gender TEXT,status TEXT,is_verify TEXT,user_type TEXT,profile_image TEXT,is_home_available INTEGER,full_name TEXT,address_line_1 TEXT,address_line_2 TEXT,zipcode TEXT,country_id TEXT,city_id TEXT)";
        db.execSQL(CREATE_userInfo_TABLE);
        String CREATE_MasterData_TABLE = "CREATE TABLE MasterData(id INTEGER,masterData TEXT)";
        db.execSQL(CREATE_MasterData_TABLE);

        String CREATE_Myhomedata_TABLE = "CREATE TABLE Myhomedata(id TEXT,user_id TEXT,home_type TEXT,bedrooms TEXT,bathrooms TEXT,sleeps TEXT,property_type TEXT,pets TEXT,family_matters TEXT,title TEXT,sort_description TEXT,house_no TEXT,location TEXT,latitude TEXT,longitude TEXT,destinations TEXT,traveller_type TEXT,travelling_anywhere TEXT,profile_image TEXT,startdate TEXT,enddate TEXT,country_id TEXT,city_id TEXT,address1 TEXT,address2 TEXT,zipcode TEXT,gender TEXT,religion TEXT,landmarks TEXT,level_security TEXT,profile_completeness TEXT,status TEXT,added_date TEXT,updated_date TEXT)";
        db.execSQL(CREATE_Myhomedata_TABLE);
        String CREATE_MychoiceData_TABLE = "CREATE TABLE MychoiceData(home_id TEXT,title TEXT,sort_description TEXT,location TEXT,destinations TEXT,home_image TEXT,startdate TEXT,enddate TEXT,zipcode TEXT,user_id TEXT,full_name TEXT,country_name TEXT,city_name TEXT,profile_image TEXT)";
        db.execSQL(CREATE_MychoiceData_TABLE);

        String CREATE_LikedmychoiceData_TABLE = "CREATE TABLE LikedmychoiceData(home_id TEXT,title TEXT,sort_description TEXT,location TEXT,destinations TEXT,home_image TEXT,startdate TEXT,enddate TEXT,zipcode TEXT,user_id TEXT,full_name TEXT,country_name TEXT,city_name TEXT,profile_image TEXT)";
        db.execSQL(CREATE_LikedmychoiceData_TABLE);

        String CREATE_AddEditHomeData_TABLE = "CREATE TABLE AddEditHomeData(home_id TEXT,homestyle TEXT,security TEXT,gender TEXT,religion TEXT,family TEXT,pets TEXT,typeOfProperties TEXT,sleeps TEXT,bathrooms TEXT,bedrooms TEXT,title TEXT,about TEXT,savefeaturesList TEXT,saveRuleList TEXT,addressStr TEXT,hnoStr TEXT,landmarkStr TEXT,enterzipcodeStr TEXT,zipCodeStr TEXT,countryId TEXT,cityId TEXT,homeImageList TEXT,travleIdStr TEXT,profileImage TEXT,dreamStr TEXT,cardnoStr TEXT,nameStr TEXT,cvvStr TEXT,monthId TEXT,yearId EXT,latitude TEXT,longitude TEXT,startdate TEXT,enddate TEXT)";
        db.execSQL(CREATE_AddEditHomeData_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS userInfo");
        db.execSQL("DROP TABLE IF EXISTS MasterData");
        db.execSQL("DROP TABLE IF EXISTS Myhomedata");
        db.execSQL("DROP TABLE IF EXISTS MychoiceData");
        db.execSQL("DROP TABLE IF EXISTS LikedmychoiceData");
        db.execSQL("DROP TABLE IF EXISTS AddEditHomeData");
        onCreate(db);
    }

    // upsert user info
    public boolean upsertUserInfoData(Data ob, int is_home_available) {
        boolean done = false;
        Data data = null;
        if (ob.getId() != null && !ob.getId().equals("") && !ob.getId().equals("0")) {
            data = getUserInfoById(ob.getId());
            if (data == null) {
                done = insertUserInfoData(ob, is_home_available);
            } else {
                done = updateUserInfoData(ob, is_home_available);
            }
        }
        return done;
    }

    public Data getUserInfoById(String id) {
        String query = "Select * FROM userInfo WHERE id = '" + id + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data ob = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserInfoData(cursor, ob);
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    //populate user  list data
    private void populateUserInfoData(Cursor cursor, Data ob) {
        ob.setId(cursor.getString(0));
        ob.setFirst_name(cursor.getString(1));
        ob.setLast_name(cursor.getString(2));
        ob.setEmail(cursor.getString(3));
        ob.setUsername(cursor.getString(4));
        ob.setMobile_number(cursor.getString(5));
        ob.setGender(cursor.getString(6));
        ob.setStatus(cursor.getString(7));
        ob.setIs_verify(cursor.getString(8));
        ob.setUser_type(cursor.getString(9));
        ob.setProfile_image(cursor.getString(10));
        ob.setIs_home_available(cursor.getInt(11));
        ob.setFull_name(cursor.getString(12));
        ob.setAddress_line_1(cursor.getString(13));
        ob.setAddress_line_2(cursor.getString(14));
        ob.setZipcode(cursor.getString(15));
        ob.setCountry_id(cursor.getString(16));
        ob.setCity_id(cursor.getString(17));
    }

    public boolean insertUserInfoData(Data ob, int is_home_available) {
        ContentValues values = new ContentValues();
        populateUserInfoValueData(values, ob, is_home_available);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("userInfo", null, values);
        db.close();
        return i > 0;
    }

    public boolean updateUserInfoData(Data ob, int is_home_available) {
        ContentValues values = new ContentValues();
        populateUserInfoValueData(values, ob, is_home_available);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("userInfo", values, " id = '" + ob.getId() + "'", null);

        db.close();
        return i > 0;
    }

    public void populateUserInfoValueData(ContentValues values, Data ob, int is_home_available) {
        values.put("id", ob.getId());
        if (ob.getFirst_name() != null && !ob.getFirst_name().equals("")) {
            values.put("first_name", ob.getFirst_name());
        }
        if (ob.getLast_name() != null && !ob.getLast_name().equals("")) {
            values.put("last_name", ob.getLast_name());
        }
        if (ob.getEmail() != null && !ob.getEmail().equals("")) {
            values.put("email", ob.getEmail());
        }
        if (ob.getUsername() != null && !ob.getUsername().equals("")) {
            values.put("username", ob.getUsername());
        }
        if (ob.getMobile_number() != null && !ob.getMobile_number().equals("")) {
            values.put("mobile_number", ob.getMobile_number());
        }
        if (ob.getGender() != null && !ob.getGender().equals("")) {
            values.put("gender", ob.getGender());//
        }
        if (ob.getStatus() != null && !ob.getStatus().equals("")) {
            values.put("status", ob.getStatus());
        }
        if (ob.getIs_verify() != null && !ob.getIs_verify().equals("")) {
            values.put("is_verify", ob.getIs_verify());
        }
        if (ob.getUser_type() != null && !ob.getUser_type().equals("")) {
            values.put("user_type", ob.getUser_type());
        }
        if (ob.getProfile_image() != null && !ob.getProfile_image().equals("")) {
            values.put("profile_image", ob.getProfile_image());
        }
        values.put("is_home_available", is_home_available);
        if (ob.getFull_name() != null && !ob.getFull_name().equals("")) {
            values.put("full_name", ob.getFull_name());
        }
        if (ob.getAddress_line_1() != null && !ob.getAddress_line_1().equals("")) {
            values.put("address_line_1", ob.getAddress_line_1());
        }
        if (ob.getAddress_line_2() != null && !ob.getAddress_line_2().equals("")) {
            values.put("address_line_2", ob.getAddress_line_2());
        }
        if (ob.getZipcode() != null && !ob.getZipcode().equals("")) {
            values.put("zipcode", ob.getZipcode());
        }
        if (ob.getCountry_id() != null && !ob.getCountry_id().equals("")) {
            values.put("country_id", ob.getCountry_id());
        }
        if (ob.getCity_id() != null && !ob.getCity_id().equals("")) {
            values.put("city_id", ob.getCity_id());
        }
    }

    public ArrayList<Data> getAllUserInfoList() {
        String query = "Select *  FROM userInfo ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Data> list = new ArrayList<Data>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateUserInfoData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    public void deleteAllRows(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }

    public boolean insertMasterData(ServiceContentData ob) {
        ContentValues values = new ContentValues();
        populateMasterDataValue(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("MasterData", null, values);
        db.close();
        return i > 0;
    }

    //populate user  list data
    private void populateMasterData(Cursor cursor, ServiceContentData ob) {
        ob.setId(cursor.getInt(0));
        Data data = new Gson().fromJson(cursor.getString(1), new TypeToken<Data>() {
        }.getType());
        ob.setData(data);
    }

    public ServiceContentData getMasterData() {
        String query = "Select * FROM MasterData ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ServiceContentData ob = new ServiceContentData();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateMasterData(cursor, ob);
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    public void populateMasterDataValue(ContentValues values, ServiceContentData ob) {
        values.put("id", 1);
        String mData = new Gson().toJson(ob.getData());
        values.put("masterData", mData);
    }

    //---------Myhomedata data----
    public boolean insertMyhomedata(MyhomeArray ob) {
        ContentValues values = new ContentValues();
        populateMyhomedataValue(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("Myhomedata", null, values);
        db.close();
        return i > 0;
    }

    //populate user  list data
    private void populateMyhomedata(Cursor cursor, MyhomeArray ob) {
        ob.setId(cursor.getString(0));
        ob.setUser_id(cursor.getString(1));
        ob.setHome_type(cursor.getString(2));
        ob.setBedrooms(cursor.getString(3));
        ob.setBathrooms(cursor.getString(4));
        ob.setSleeps(cursor.getString(5));
        ob.setProperty_type(cursor.getString(6));
        ob.setPets(cursor.getString(7));
        ob.setFamily_matters(cursor.getString(8));
        ob.setTitle(cursor.getString(9));
        ob.setSort_description(cursor.getString(10));
        ob.setHouse_no(cursor.getString(11));
        ob.setLocation(cursor.getString(12));
        ob.setLatitude(cursor.getString(13));
        ob.setLongitude(cursor.getString(14));
        ob.setDestinations(cursor.getString(15));
        ob.setTraveller_type(cursor.getString(16));
        ob.setTravelling_anywhere(cursor.getString(17));
        ob.setProfile_image(cursor.getString(18));
        ob.setStartdate(cursor.getString(19));
        ob.setEnddate(cursor.getString(20));
        ob.setCountry_id(cursor.getString(21));
        ob.setCity_id(cursor.getString(22));
        ob.setAddress1(cursor.getString(23));
        ob.setAddress2(cursor.getString(24));
        ob.setZipcode(cursor.getString(25));
        ob.setGender(cursor.getString(26));
        ob.setReligion(cursor.getString(27));
        ob.setLandmarks(cursor.getString(28));
        ob.setLevel_security(cursor.getString(29));
        ob.setProfile_completeness(cursor.getString(30));
        ob.setStatus(cursor.getString(31));
        ob.setAdded_date(cursor.getString(32));
        ob.setUpdated_date(cursor.getString(33));
    }

    public MyhomeArray getMyhomedata() {
        String query = "Select * FROM Myhomedata ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MyhomeArray ob = new MyhomeArray();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateMyhomedata(cursor, ob);
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    public void populateMyhomedataValue(ContentValues values, MyhomeArray ob) {
        values.put("id", ob.getId());
        values.put("user_id", ob.getUser_id());
        values.put("home_type", ob.getHome_type());
        values.put("bedrooms", ob.getBedrooms());
        values.put("bathrooms", ob.getBathrooms());
        values.put("sleeps", ob.getSleeps());
        values.put("property_type", ob.getProperty_type());
        values.put("pets", ob.getPets());
        values.put("family_matters", ob.getFamily_matters());
        values.put("title", ob.getTitle());
        values.put("sort_description", ob.getSort_description());
        values.put("house_no", ob.getHouse_no());
        values.put("location", ob.getLocation());
        values.put("latitude", ob.getLatitude());
        values.put("longitude", ob.getLongitude());
        values.put("destinations", ob.getDestinations());
        values.put("traveller_type", ob.getTraveller_type());
        values.put("travelling_anywhere", ob.getTravelling_anywhere());
        values.put("profile_image", ob.getProfile_image());
        values.put("startdate", ob.getStartdate());
        values.put("enddate", ob.getEnddate());
        values.put("country_id", ob.getCountry_id());
        values.put("city_id", ob.getCity_id());
        values.put("address1", ob.getAddress1());
        values.put("address2", ob.getAddress2());
        values.put("zipcode", ob.getZipcode());
        values.put("gender", ob.getGender());
        values.put("religion", ob.getReligion());
        values.put("landmarks", ob.getLandmarks());
        values.put("level_security", ob.getLevel_security());
        values.put("profile_completeness", ob.getProfile_completeness());
        values.put("status", ob.getStatus());
        values.put("added_date", ob.getAdded_date());
        values.put("updated_date", ob.getUpdated_date());
    }

    public ArrayList<MyhomeArray> getAllMyhomedata() {
        String query = "Select *  FROM Myhomedata ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<MyhomeArray> list = new ArrayList<MyhomeArray>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                MyhomeArray ob = new MyhomeArray();
                populateMyhomedata(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //---------MychoiceData data----
    public boolean inserMychoiceData(MychoiceArray mychoice) {
        ContentValues values = new ContentValues();
        populateMychoiceDataValue(values, mychoice);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("MychoiceData", null, values);
        db.close();
        return i > 0;
    }

    //populate MychoiceData  list data
    private void populateMychoiceData(Cursor cursor, MychoiceArray ob) {
        ob.setHome_id(cursor.getString(0));
        ob.setTitle(cursor.getString(1));
        ob.setSort_description(cursor.getString(2));
        ob.setLocation(cursor.getString(3));
        ob.setDestinations(cursor.getString(4));
        ob.setHome_image(cursor.getString(5));
        ob.setStartdate(cursor.getString(6));
        ob.setEnddate(cursor.getString(7));
        ob.setZipcode(cursor.getString(8));
        ob.setUser_id(cursor.getString(9));
        ob.setFull_name(cursor.getString(10));
        ob.setCountry_name(cursor.getString(11));
        ob.setCity_name(cursor.getString(12));
        ob.setProfile_image(cursor.getString(13));
    }

    public MychoiceArray getMychoiceData() {
        String query = "Select * FROM MychoiceData ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MychoiceArray ob = new MychoiceArray();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateMychoiceData(cursor, ob);
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    public void populateMychoiceDataValue(ContentValues values, MychoiceArray ob) {
        values.put("home_id", ob.getHome_id());
        values.put("title", ob.getTitle());
        values.put("sort_description", ob.getSort_description());
        values.put("location", ob.getLocation());
        values.put("destinations", ob.getDestinations());
        values.put("home_image", ob.getHome_image());
        values.put("startdate", ob.getStartdate());
        values.put("enddate", ob.getEnddate());
        values.put("zipcode", ob.getZipcode());
        values.put("user_id", ob.getUser_id());
        values.put("full_name", ob.getFull_name());
        values.put("country_name", ob.getCountry_name());
        values.put("city_name", ob.getCity_name());
        values.put("profile_image", ob.getProfile_image());
    }

    public ArrayList<MychoiceArray> getAllMychoiceData() {
        String query = "Select *  FROM MychoiceData ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<MychoiceArray> list = new ArrayList<MychoiceArray>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                MychoiceArray ob = new MychoiceArray();
                populateMychoiceData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    //---------MychoiceData data----
    public boolean inserLikedmychoiceData(LikedmychoiceArray mychoice) {
        ContentValues values = new ContentValues();
        populateLikedmychoiceDataValue(values, mychoice);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("LikedmychoiceData", null, values);
        db.close();
        return i > 0;
    }

    //populate LikedmychoiceData  list data
    private void populateLikedmychoiceData(Cursor cursor, LikedmychoiceArray ob) {
        ob.setHome_id(cursor.getString(0));
        ob.setTitle(cursor.getString(1));
        ob.setSort_description(cursor.getString(2));
        ob.setLocation(cursor.getString(3));
        ob.setDestinations(cursor.getString(4));
        ob.setHome_image(cursor.getString(5));
        ob.setStartdate(cursor.getString(6));
        ob.setEnddate(cursor.getString(7));
        ob.setZipcode(cursor.getString(8));
        ob.setUser_id(cursor.getString(9));
        ob.setFull_name(cursor.getString(10));
        ob.setCountry_name(cursor.getString(11));
        ob.setCity_name(cursor.getString(12));
        ob.setProfile_image(cursor.getString(13));
    }

    public LikedmychoiceArray getLikedmychoiceData() {
        String query = "Select * FROM LikedmychoiceData ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        LikedmychoiceArray ob = new LikedmychoiceArray();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateLikedmychoiceData(cursor, ob);
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    public void populateLikedmychoiceDataValue(ContentValues values, LikedmychoiceArray ob) {
        values.put("home_id", ob.getHome_id());
        values.put("title", ob.getTitle());
        values.put("sort_description", ob.getSort_description());
        values.put("location", ob.getLocation());
        values.put("destinations", ob.getDestinations());
        values.put("home_image", ob.getHome_image());
        values.put("startdate", ob.getStartdate());
        values.put("enddate", ob.getEnddate());
        values.put("zipcode", ob.getZipcode());
        values.put("user_id", ob.getUser_id());
        values.put("full_name", ob.getFull_name());
        values.put("country_name", ob.getCountry_name());
        values.put("city_name", ob.getCity_name());
        values.put("profile_image", ob.getProfile_image());
    }

    public ArrayList<LikedmychoiceArray> getAllLikedmychoiceData() {
        String query = "Select *  FROM LikedmychoiceData ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<LikedmychoiceArray> list = new ArrayList<LikedmychoiceArray>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                LikedmychoiceArray ob = new LikedmychoiceArray();
                populateLikedmychoiceData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    public boolean upsertAddEditHomeData(HomeDetails ob) {
        boolean done = false;
        HomeDetails data = null;
        if (ob.getId() != null && !ob.getId().equals("") && !ob.getId().equals("0")) {
            data = getAddEditHomeDataById(ob.getId());
            if (data == null) {
                done = insertAddEditHomeData(ob);
            } else {
                done = updateAddEditHomeOverview(ob);
            }
        }
        return done;
    }

    public HomeDetails getAddEditHomeDataById(String id) {
        String query = "Select * FROM AddEditHomeData WHERE home_id = '" + id + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HomeDetails ob = new HomeDetails();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateAddEditHomeData(cursor, ob);
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    private void populateAddEditHomeData(Cursor cursor, HomeDetails ob) {
        ob.setId(cursor.getString(0));
        ob.setHomestyle(cursor.getString(1));
        ob.setLevel_security(cursor.getString(2));
        ob.setGender(cursor.getString(3));
        ob.setReligion(cursor.getString(4));
        ob.setFamily_matters(cursor.getString(5));
        ob.setPets(cursor.getString(6));
        ob.setProperty_type(cursor.getString(7));
        ob.setSleeps(cursor.getString(8));
        ob.setBathrooms(cursor.getString(9));
        ob.setBedrooms(cursor.getString(10));
        ob.setTitle(cursor.getString(11));
        ob.setSort_description(cursor.getString(12));
        ArrayList<Home_features> featureList = new Gson().fromJson(cursor.getString(13), new TypeToken<ArrayList<Home_features>>() {
        }.getType());
        ob.setFeatureList(featureList);
        ArrayList<Home_rules> houseRuleList = new Gson().fromJson(cursor.getString(14), new TypeToken<ArrayList<Home_rules>>() {
        }.getType());
        ob.setHouseRuleList(houseRuleList);
        ob.setAddress1(cursor.getString(15));
        ob.setHouse_no(cursor.getString(16));
        ob.setLandmarks(cursor.getString(17));
        ob.setZipcode(cursor.getString(18));
        ob.setLocation(cursor.getString(19));
        ob.setCountry_id(cursor.getString(20));
        ob.setCity_id(cursor.getString(21));
        ArrayList<Homegallery> homeImageList = new Gson().fromJson(cursor.getString(22), new TypeToken<ArrayList<Homegallery>>() {
        }.getType());
        ob.setHomeImageList(homeImageList);
        ob.setTraveller_type(cursor.getString(23));
        ob.setProfile_image(cursor.getString(24));
        ob.setDestinations(cursor.getString(25));
        ob.setCardnumber(cursor.getString(26));
        ob.setNameoncard(cursor.getString(27));
        ob.setCvv(cursor.getString(28));
        ob.setMonth(cursor.getString(29));
        ob.setYear(cursor.getString(30));

        ob.setLatitude(cursor.getString(31));
        ob.setLongitude(cursor.getString(32));
        ob.setStartdate(cursor.getString(33));
        ob.setEnddate(cursor.getString(34));
    }

    public boolean insertAddEditHomeData(HomeDetails ob) {
        ContentValues values = new ContentValues();
        populateAddEditHomeValueData(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("AddEditHomeData", null, values);
        db.close();
        return i > 0;
    }

    public void populateAddEditHomeValueData(ContentValues values, HomeDetails ob) {
        values.put("home_id", ob.getId());
        values.put("homestyle", ob.getHome_type());
        values.put("security", ob.getLevel_security());
        values.put("gender", ob.getGender());
        values.put("religion", ob.getReligion());
        values.put("family", ob.getFamily_matters());
        values.put("pets", ob.getPets());//
        values.put("typeOfProperties", ob.getProperty_type());
        values.put("sleeps", ob.getSleeps());
        values.put("bathrooms", ob.getBathrooms());
        values.put("bedrooms", ob.getBedrooms());
        values.put("title", ob.getTitle());
        values.put("about", ob.getSort_description());
        String featureStr = new Gson().toJson(ob.getFeatureList());
        values.put("savefeaturesList", featureStr);
        String ruleStr = new Gson().toJson(ob.getHouseRuleList());
        values.put("saveRuleList", ruleStr);
        values.put("addressStr", ob.getAddress1());
        values.put("hnoStr", ob.getHouse_no());
        values.put("landmarkStr", ob.getLandmarks());
        values.put("enterzipcodeStr", ob.getZipcode());
        values.put("zipCodeStr", ob.getLocation());
        values.put("countryId", ob.getCountry_id());
        values.put("cityId", ob.getCity_id());
        String image = new Gson().toJson(ob.getHomeImageList());
        values.put("homeImageList", image);
        values.put("travleIdStr", ob.getTraveller_type());
        values.put("profileImage", ob.getProfile_image());
        values.put("dreamStr", ob.getDestinations());
        values.put("cardnoStr", ob.getCardnumber());
        values.put("nameStr", ob.getNameoncard());
        values.put("cvvStr", ob.getCvv());
        values.put("monthId", ob.getMonth());
        values.put("yearId", ob.getYear());

        values.put("latitude", ob.getLatitude());
        values.put("longitude", ob.getLongitude());
        values.put("startdate", ob.getStartdate());
        values.put("enddate", ob.getEnddate());
    }

    public boolean updateAddEditHomeOverview(HomeDetails ob) {
        ContentValues values = new ContentValues();
        populateAddEditHomeValueDataOverview(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("AddEditHomeData", values, " home_id = '" + ob.getId() + "'", null);

        db.close();
        return i > 0;
    }

    public void populateAddEditHomeValueDataOverview(ContentValues values, HomeDetails ob) {
        values.put("home_id", ob.getId());
        values.put("homestyle", ob.getHome_type());
        values.put("security", ob.getLevel_security());
        values.put("gender", ob.getGender());
        values.put("religion", ob.getReligion());
        values.put("family", ob.getFamily_matters());
        values.put("pets", ob.getPets());//
        values.put("typeOfProperties", ob.getProperty_type());
        values.put("sleeps", ob.getSleeps());
        values.put("bathrooms", ob.getBathrooms());
        values.put("bedrooms", ob.getBedrooms());

    }

    public boolean updateAddEditHomeDetail(HomeDetails ob) {
        ContentValues values = new ContentValues();
        populateAddEditHomeValueDetail(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("AddEditHomeData", values, " home_id = '" + ob.getId() + "'", null);

        db.close();
        return i > 0;
    }

    public void populateAddEditHomeValueDetail(ContentValues values, HomeDetails ob) {
        values.put("title", ob.getTitle());
        values.put("about", ob.getSort_description());
        String featureStr = new Gson().toJson(ob.getFeatureList());
        values.put("savefeaturesList", featureStr);
        String ruleStr = new Gson().toJson(ob.getHouseRuleList());
        values.put("saveRuleList", ruleStr);
    }

    public boolean updateAddEditHomeLocation(HomeDetails ob) {
        ContentValues values = new ContentValues();
        populateAddEditHomeValueLocation(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("AddEditHomeData", values, " home_id = '" + ob.getId() + "'", null);

        db.close();
        return i > 0;
    }

    public void populateAddEditHomeValueLocation(ContentValues values, HomeDetails ob) {
        values.put("addressStr", ob.getAddress1());
        values.put("hnoStr", ob.getHouse_no());
        values.put("landmarkStr", ob.getLandmarks());
        values.put("enterzipcodeStr", ob.getZipcode());
        values.put("zipCodeStr", ob.getLocation());
        values.put("countryId", ob.getCountry_id());
        values.put("cityId", ob.getCity_id());
    }


    public boolean updateAddEditHomeImage(HomeDetails ob) {
        ContentValues values = new ContentValues();
        populateAddEditHomeValueImage(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("AddEditHomeData", values, " home_id = '" + ob.getId() + "'", null);

        db.close();
        return i > 0;
    }

    public void populateAddEditHomeValueImage(ContentValues values, HomeDetails ob) {
        String image = new Gson().toJson(ob.getHomeImageList());
        values.put("homeImageList", image);
    }

    public boolean updateAddEditHomeProfile(HomeDetails ob) {
        ContentValues values = new ContentValues();
        populateAddEditHomeValueProfile(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("AddEditHomeData", values, " home_id = '" + ob.getId() + "'", null);

        db.close();
        return i > 0;
    }

    public void populateAddEditHomeValueProfile(ContentValues values, HomeDetails ob) {
        values.put("travleIdStr", ob.getTraveller_type());
        values.put("profileImage", ob.getProfile_image());
        values.put("dreamStr", ob.getDestinations());
    }

    public boolean updateAddEditHomeCard(HomeDetails ob) {
        ContentValues values = new ContentValues();
        populateAddEditHomeValueCard(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("AddEditHomeData", values, " home_id = '" + ob.getId() + "'", null);

        db.close();
        return i > 0;
    }

    public void populateAddEditHomeValueCard(ContentValues values, HomeDetails ob) {
        values.put("cardnoStr", ob.getCardnumber());
        values.put("nameStr", ob.getNameoncard());
        values.put("cvvStr", ob.getCvv());
        values.put("monthId", ob.getMonth());
        values.put("yearId", ob.getYear());
    }

    public ArrayList<HomeDetails> getAllAddEditHomeDataList() {
        String query = "Select *  FROM AddEditHomeData ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<HomeDetails> list = new ArrayList<HomeDetails>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                HomeDetails ob = new HomeDetails();
                populateAddEditHomeData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }
}
