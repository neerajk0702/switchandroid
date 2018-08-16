package com.kredivation.switchland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kredivation.switchland.model.Data;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS userInfo");
        db.execSQL("DROP TABLE IF EXISTS MasterData");
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

}
