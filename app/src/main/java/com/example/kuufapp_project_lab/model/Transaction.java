package com.example.kuufapp_project_lab.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kuufapp_project_lab.util.DBOpenHelper;
import com.example.kuufapp_project_lab.util.HelperData;

import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    private int id;
    private int userId;
    private int productId;
    private String transactionDate;

    public Transaction(int id, int userId, int productId, String transactionDate) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.transactionDate = transactionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public static ArrayList<Transaction> getMyTransaction(Context context){
        if(HelperData.getInstance().getCurrentUser() == null)
            return null;
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USER_ID + " = ?";
        String[] selectionArgs = {HelperData.getInstance().getCurrentUser().getId() + ""};
        String sort = DBOpenHelper.ID + " DESC";

        Cursor cursor = db.query(
                DBOpenHelper.TRANSACTIONS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USER_ID,
                        DBOpenHelper.PRODUCT_ID,
                        DBOpenHelper.TRANSACTION_DATE
                },
                selection,
                selectionArgs,
                null,
                null,
                sort
        );

        if(cursor.getCount() == 0){
            return null;
        }else{
            ArrayList<Transaction> transactions = new ArrayList<>();
            cursor.moveToFirst();
            do{
                transactions.add(new Transaction(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TRANSACTION_DATE))
                ));
            }while(cursor.moveToNext());
            return transactions;
        }
    }

    public static void deleteTransaction(Context context, String ID){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.ID + " = ?";
        String[] selectionArgs = { ID };

        db.delete(
                DBOpenHelper.TRANSACTIONS,
                selection,
                selectionArgs
        );
    }

    public static void insertTransaction(Context context, Transaction transaction){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.USER_ID, transaction.getUserId());
        cv.put(DBOpenHelper.PRODUCT_ID, transaction.getProductId());
        cv.put(DBOpenHelper.TRANSACTION_DATE, transaction.getTransactionDate());

        db.insert(DBOpenHelper.TRANSACTIONS, null, cv);
    }
}
