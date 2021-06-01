package com.example.kuufapp_project_lab.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kuufapp_project_lab.R;
import com.example.kuufapp_project_lab.model.Product;
import com.example.kuufapp_project_lab.model.Transaction;
import com.example.kuufapp_project_lab.model.User;
import com.example.kuufapp_project_lab.util.HelperData;
import com.google.gson.Gson;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ProductDetailActivity extends AppCompatActivity {
    Product product;

    Toolbar toolbar;
    TextView tvName, tvPrice, tvMinMax;
    Button btnBuy, btnOk;
    CardView cvShowLocation;
    Dialog dialog;

    String phoneNumber, message;
    SmsManager smsManager;
    int sendSMSPermission, receiveSMSPermission;
    int readPhoneStatePermission, readPhoneNumberPermission, readSMSPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getDataFromIntent();
        initializeToolbar();
        initializeComponents();
        setComponent();
        setDialog();
        setBuyProduct();
    }

    private void setBuyProduct() {

//        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");

        btnBuy.setOnClickListener(x -> {
            User curr = HelperData.getInstance().getCurrentUser();
            if(curr.getWallet() < product.getPrice()){
                dialog.show();
            }else{
                curr.setWallet(curr.getWallet() - product.getPrice());
                Transaction transaction = new Transaction(
                        0,
                        curr.getId(),
                        product.getId(),
                        getDateNow()
                );
                //update user and add transaction
                User.updateUserWallet(this, curr);
                Transaction.insertTransaction(this, transaction);

                //send sms to user phone number

                TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                smsManager = smsManager.getDefault();
                sendSMSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
                receiveSMSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
                readPhoneStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
                readSMSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
                readPhoneNumberPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS);

                if (sendSMSPermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
                }
                if (receiveSMSPermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
                }
                if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
                if (readSMSPermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
                }
                if (readPhoneNumberPermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_NUMBERS}, 1);
                }

                phoneNumber = tMgr.getLine1Number();

                message = "You have purchased " + product.getName() + " that costs Rp " + product.getPrice() +"\nThank you for purchasing at our store!";
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                goToActivity("home");
            }
        });

        cvShowLocation.setOnClickListener(x -> {
            //go to map form and show location
        });
    }

    private String getDateNow(){
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(formatter).toString();
    }

    private void setDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.buy_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(x -> {
            dialog.dismiss();
        });
    }

    private void setComponent() {
        tvName.setText(product.getName());
        tvPrice.setText(HelperData.getPriceString(product.getPrice()));
        String player = product.getMinPlayer() + " - " + product.getMaxPlayer() + " Players";
        tvMinMax.setText(player);
    }

    private void initializeComponents() {
        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvPrice = (TextView) findViewById(R.id.tv_detail_price);
        tvMinMax = (TextView) findViewById(R.id.tv_detail_min_max);
        btnBuy = (Button) findViewById(R.id.btn_buy);
        cvShowLocation = (CardView) findViewById(R.id.cv_show_location);
    }

    private void getDataFromIntent() {
        product =  (Product) getIntent().getSerializableExtra(HelperData.PRODUCT_DATA);
        if(product == null){
            onBackPressed();
        }
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void goToActivity(String act) {
        Intent intent = new Intent(this, ProfileActivity.class);
        if(act.equals("home")){
            intent.setClass(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }else if(act.equals("store")){
            intent.setClass(this, StoreActivity.class);
        }else if(act.equals("logout")){
            HelperData.getInstance().setCurrentUser(null);
            intent.setClass(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}