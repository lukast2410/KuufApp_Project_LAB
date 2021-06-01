package com.example.kuufapp_project_lab.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.kuufapp_project_lab.model.User;

public class DBOpenHelper  extends SQLiteOpenHelper {
    public final static String DB_NAME = "KuufApp.db";
    public final static int DB_VERSION = 1;
    public final static String USERS = "Users";
    public final static String ID = "ID";
    public final static String USERNAME = "Username";
    public final static String PASSWORD = "Password";
    public final static String PHONE = "Phone";
    public final static String GENDER = "Gender";
    public final static String WALLET = "Wallet";
    public final static String DOB = "DateOfBirth";

    public final static String TRANSACTIONS = "Transactions";
    public final static String USER_ID = "UserId";
    public final static String PRODUCT_ID = "ProductId";
    public final static String TRANSACTION_DATE = "TransactionDate";

    public final static String PRODUCTS = "Products";
    public final static String NAME = "Name";
    public final static String MIN_PLAYER = "MinPlayer";
    public final static String MAX_PLAYER = "MaxPlayer";
    public final static String PRICE = "Price";
    public final static String LATITUDE = "Latitude";
    public final static String LONGITUDE = "Longitude";

    private static DBOpenHelper db = null;

    private DBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBOpenHelper getInstance(Context context){
        return db = (db == null)? new DBOpenHelper(context) : db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USERNAME + " TEXT NOT NULL," +
                PASSWORD + " TEXT NOT NULL," +
                PHONE + " TEXT NOT NULL," +
                GENDER + " TEXT NOT NULL," +
                WALLET + " INTEGER NOT NULL," +
                DOB + " TEXT NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE " + PRODUCTS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT NOT NULL," +
                MIN_PLAYER + " INTEGER NOT NULL," +
                MAX_PLAYER + " INTEGER NOT NULL," +
                PRICE + " INTEGER NOT NULL," +
                LATITUDE + " REAL NOT NULL," +
                LONGITUDE + " REAL NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE " + TRANSACTIONS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_ID + " INTEGER NOT NULL," +
                PRODUCT_ID + " INTEGER NOT NULL," +
                TRANSACTION_DATE + " TEXT NOT NULL," +
                " FOREIGN KEY("+ USER_ID +") REFERENCES "+ USERS +"("+ ID +")," +
                " FOREIGN KEY("+ PRODUCT_ID +") REFERENCES "+ PRODUCTS +"("+ ID +")" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
