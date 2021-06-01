package com.example.kuufapp_project_lab.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kuufapp_project_lab.util.DBOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private int id;
    private String name;
    private int minPlayer;
    private int maxPlayer;
    private int price;
    private double latitude;
    private double longitude;

    public Product(int id, String name, int minPlayer, int maxPlayer, int price, double longitude, double latitude) {
        this.id = id;
        this.name = name;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public void setMinPlayer(int minPlayer) {
        this.minPlayer = minPlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public static ArrayList<Product> getAllProduct(Context context){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        ArrayList<Product> products = new ArrayList<>();

        Cursor cursor = db.query(
                DBOpenHelper.PRODUCTS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.NAME,
                        DBOpenHelper.MIN_PLAYER,
                        DBOpenHelper.MAX_PLAYER,
                        DBOpenHelper.PRICE,
                        DBOpenHelper.LATITUDE,
                        DBOpenHelper.LONGITUDE
                },
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0){
            return null;
        }else{
            cursor.moveToFirst();
            do{
                products.add(new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MIN_PLAYER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MAX_PLAYER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DBOpenHelper.LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DBOpenHelper.LONGITUDE))
                ));
            }while(cursor.moveToNext());
            return products;
        }
    }

    public static void insertProduct(Context context, Product product){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.NAME, product.getName());
        cv.put(DBOpenHelper.MIN_PLAYER, product.getMinPlayer());
        cv.put(DBOpenHelper.MAX_PLAYER, product.getMaxPlayer());
        cv.put(DBOpenHelper.PRICE, product.getPrice());
        cv.put(DBOpenHelper.LATITUDE, product.getLatitude());
        cv.put(DBOpenHelper.LONGITUDE, product.getLongitude());

        db.insert(DBOpenHelper.PRODUCTS, null, cv);
    }
}
