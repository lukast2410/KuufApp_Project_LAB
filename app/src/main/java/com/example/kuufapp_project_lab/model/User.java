package com.example.kuufapp_project_lab.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kuufapp_project_lab.util.DBOpenHelper;

import java.util.ArrayList;

public class User {
    private int id;
    private String username;
    private String password;
    private String phone;
    private String gender;
    private int wallet;
    private String dob;

    public User(int id, String username, String password, String phone, String gender, int wallet, String dob) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.wallet = wallet;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public static void insertUser(Context context, User user){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.USERNAME, user.getUsername());
        cv.put(DBOpenHelper.PASSWORD, user.getPassword());
        cv.put(DBOpenHelper.PHONE, user.getPhone());
        cv.put(DBOpenHelper.GENDER, user.getGender());
        cv.put(DBOpenHelper.WALLET, user.getWallet());
        cv.put(DBOpenHelper.DOB, user.getDob());

        db.insert(DBOpenHelper.USERS, null, cv);
    }

    public static int updateUserWallet(Context context, User user){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.WALLET, user.getWallet());

        String selection = DBOpenHelper.ID + " LIKE ?";
        String[] selectionArgs = { user.getId() + "" };

        int count = db.update(
                DBOpenHelper.USERS,
                cv,
                selection,
                selectionArgs
        );

        return count;
    }

    public static boolean usernameIsUnique(Context context, String username){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USERNAME + " LIKE ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                DBOpenHelper.USERS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USERNAME,
                        DBOpenHelper.ID,
                        DBOpenHelper.USERNAME,
                        DBOpenHelper.PASSWORD,
                        DBOpenHelper.PHONE,
                        DBOpenHelper.GENDER,
                        DBOpenHelper.WALLET,
                        DBOpenHelper.DOB
                },
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0)
            return true;
        return false;
    }

    public static User userLogin(Context context, String username, String password){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USERNAME + " LIKE ? AND " + DBOpenHelper.PASSWORD + " LIKE ?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query(
                DBOpenHelper.USERS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USERNAME,
                        DBOpenHelper.PASSWORD,
                        DBOpenHelper.PHONE,
                        DBOpenHelper.GENDER,
                        DBOpenHelper.WALLET,
                        DBOpenHelper.DOB
                },
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0 || cursor.getCount() > 1){
            return null;
        }else{
            cursor.moveToFirst();
            return new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.GENDER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.WALLET)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.DOB))
            );
        }
    }
}
