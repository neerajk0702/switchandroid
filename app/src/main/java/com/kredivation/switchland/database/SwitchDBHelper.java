package com.kredivation.switchland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kredivation.switchland.model.Data;

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
        String CREATE_userInfo_TABLE = "CREATE TABLE userInfo(id TEXT,first_name TEXT, last_name TEXT,email TEXT,username TEXT,mobile_number TEXT,gender TEXT,status TEXT,is_verify TEXT,user_type TEXT,profile_image TEXT)";
        db.execSQL(CREATE_userInfo_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS userInfo");
        onCreate(db);
    }

    // upsert user info
    public boolean upsertUserInfoData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getId() != null && !ob.getId().equals("") && !ob.getId().equals("0")) {
            data = getUserInfoById(ob.getId());
            if (data == null) {
                done = insertUserInfoData(ob);
            } else {
                done = updateUserInfoData(ob);
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
    }

    public boolean insertUserInfoData(Data ob) {
        ContentValues values = new ContentValues();
        populateUserInfoValueData(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("userInfo", null, values);
        db.close();
        return i > 0;
    }

    public boolean updateUserInfoData(Data ob) {
        ContentValues values = new ContentValues();
        populateUserInfoValueData(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("userInfo", values, " id = '" + ob.getId() + "'", null);

        db.close();
        return i > 0;
    }

    public void populateUserInfoValueData(ContentValues values, Data ob) {
        values.put("id", ob.getId());
        values.put("first_name", ob.getFirst_name());
        values.put("last_name", ob.getLast_name());
        values.put("email", ob.getEmail());
        values.put("username", ob.getUsername());
        values.put("mobile_number", ob.getMobile_number());
        values.put("gender", ob.getGender());
        values.put("status", ob.getStatus());
        values.put("is_verify", ob.getIs_verify());
        values.put("user_type", ob.getUser_type());
        values.put("profile_image", ob.getProfile_image());
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
}
