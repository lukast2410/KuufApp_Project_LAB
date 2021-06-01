package com.example.kuufapp_project_lab.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kuufapp_project_lab.R;
import com.example.kuufapp_project_lab.model.User;
import com.example.kuufapp_project_lab.util.HelperData;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    boolean expand;
    LinearLayout topUpContainer;
    RelativeLayout topUpExpand;
    EditText etPassword;
    TextView tvUsername, tvPhone, tvGender, tvDOB, tvWallet;
    TextView errTopUp, errPassword;
    RadioGroup rgTopUp;
    RadioButton selectedNominal;
    Button btnTopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeToolbar();
        initializeComponents();
        settingTopUp();
        settingData();
    }

    private void settingData() {
        User current = HelperData.getInstance().getCurrentUser();
        tvUsername.setText(current.getUsername());
        tvPhone.setText(current.getPhone());
        tvGender.setText(current.getGender());
        tvDOB.setText(current.getDob());
        String nominal = "Rp. " + HelperData.getPriceString(current.getWallet());
        tvWallet.setText(nominal);
    }

    private void settingTopUp() {
        topUpExpand.setOnClickListener(x -> {
            if(!expand){
                topUpContainer.setVisibility(View.VISIBLE);
                expand = true;
            }else{
                topUpContainer.setVisibility(View.GONE);
                expand = false;
            }
        });

        btnTopUp.setOnClickListener(x -> {
            User current = HelperData.getInstance().getCurrentUser();
            boolean success = true;

            int selectedID = rgTopUp.getCheckedRadioButtonId();
            selectedNominal = (RadioButton) findViewById(selectedID);
            int nominal = 0;
            if(selectedNominal == null){
                errTopUp.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errTopUp.setVisibility(View.GONE);
                nominal = getTopUpNominal(selectedNominal.getText().toString());
            }

            if(!etPassword.getText().toString().equals(current.getPassword())){
                errPassword.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errPassword.setVisibility(View.GONE);
            }

            if(success){
                current.setWallet(current.getWallet() + nominal);
                User.updateUserWallet(this, current);
                goToActivity("home");
            }
        });
    }

    private int getTopUpNominal(String nominal) {
        if(nominal.contains("250")){
            return 250000;
        }else if(nominal.contains("500")){
            return 500000;
        }else{
            return 1000000;
        }
    }

    private void initializeComponents() {
        expand = false;
        topUpContainer = (LinearLayout) findViewById(R.id.ll_top_up_container);
        topUpExpand = (RelativeLayout) findViewById(R.id.rl_top_up_expand);
        etPassword = (EditText) findViewById(R.id.et_top_up_password);
        tvUsername = (TextView) findViewById(R.id.tv_profile_username);
        tvPhone = (TextView) findViewById(R.id.tv_profile_phone);
        tvGender = (TextView) findViewById(R.id.tv_profile_gender);
        tvDOB = (TextView) findViewById(R.id.tv_profile_dob);
        tvWallet = (TextView) findViewById(R.id.tv_profile_wallet_nominal);
        errTopUp = (TextView) findViewById(R.id.tv_top_up_error);
        errPassword = (TextView) findViewById(R.id.tv_top_up_password_error);
        rgTopUp = (RadioGroup) findViewById(R.id.rg_top_up);
        btnTopUp = (Button) findViewById(R.id.btn_top_up);
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                goToActivity("home");
                return true;
            case R.id.menu_store:
                goToActivity("store");
                return true;
            case R.id.menu_profile:
                return true;
            case R.id.menu_logout:
                goToActivity("logout");
                return true;
        }
        return super.onOptionsItemSelected(item);
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
}