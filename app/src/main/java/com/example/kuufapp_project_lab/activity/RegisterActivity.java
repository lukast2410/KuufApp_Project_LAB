package com.example.kuufapp_project_lab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuufapp_project_lab.R;
import com.example.kuufapp_project_lab.model.User;
import com.example.kuufapp_project_lab.util.DBOpenHelper;
import com.example.kuufapp_project_lab.util.HelperData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    final String[] monthL = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    DBOpenHelper db;
    ScrollView layout;
    LinearLayout llDateContainer;
    DatePickerDialog datePickerDialog;
    String selectedDate;
    EditText etUsername, etPassword, etConfirm, etPhone;
    TextView tvDate;
    TextView errUsername, errPassword, errConfirm, errDate, errPhone, errAgree, errGender;
    RadioGroup rgGender;
    RadioButton rbSelectedGender;
    CheckBox cbAgree;
    Button btnCreateAccount, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DBOpenHelper.getInstance(this);
        setContentView(R.layout.activity_register);
        initializeBackground();
        initializeComponents();
        setDatePicker();
        watchInputError();
        setButtons();
    }

    private void initializeBackground() {
        layout = (ScrollView) findViewById(R.id.register_layout);
        AnimationDrawable animBg = (AnimationDrawable) layout.getBackground();
        animBg.setEnterFadeDuration(3000);
        animBg.setExitFadeDuration(4000);
        animBg.start();
    }

    private void initializeComponents() {
        llDateContainer = (LinearLayout) findViewById(R.id.ll_register_date);
        etUsername = (EditText) findViewById(R.id.et_register_username);
        etPassword = (EditText) findViewById(R.id.et_register_password);
        etConfirm = (EditText) findViewById(R.id.et_register_confirm);
        tvDate = (TextView) findViewById(R.id.et_register_date);
        etPhone = (EditText) findViewById(R.id.et_register_phone);
        errUsername = (TextView) findViewById(R.id.tv_register_username_error);
        errPassword = (TextView) findViewById(R.id.tv_register_password_error);
        errConfirm = (TextView) findViewById(R.id.tv_register_confirm_error);
        errDate = (TextView) findViewById(R.id.tv_register_date_error);
        errPhone = (TextView) findViewById(R.id.tv_register_phone_error);
        errAgree = (TextView) findViewById(R.id.tv_register_agree_error);
        errGender = (TextView) findViewById(R.id.tv_register_gender_error);
        rgGender = (RadioGroup) findViewById(R.id.rg_register_gender);
        cbAgree = (CheckBox) findViewById(R.id.cb_register_agree);
        btnCreateAccount = (Button) findViewById(R.id.btn_register_create_account);
        btnLogin = (Button) findViewById(R.id.btn_back_login);
    }

    private void setDatePicker() {
        selectedDate = "";
        datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = ((dayOfMonth < 10)? "0" : "") + dayOfMonth + " " + monthL[month-1] + " " + year;
                    tvDate.setText(selectedDate);
                    errDate.setVisibility(View.GONE);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        llDateContainer.setOnClickListener(v -> {
            datePickerDialog.show();
        });
    }

    private boolean isAlphanumeric(String words){
        boolean alpha = false;
        boolean numeric = false;
        boolean others = false;
        for(int i=0;i<words.length();i++){
            if(Character.isAlphabetic(words.charAt(i))){
                alpha = true;
            }else if(Character.isDigit(words.charAt(i))){
                numeric = true;
            }else{
                others = true;
            }
        }
        return alpha && numeric && !others;
    }

    private boolean isNumeric(String words){
        boolean numeric = false;
        boolean others = false;
        for(int i=0;i<words.length();i++){
            if(Character.isDigit(words.charAt(i))){
                numeric = true;
            }else{
                others = true;
            }
        }
        return numeric && !others;
    }

    private void watchInputError() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(etUsername.getText().length() < 6 || etUsername.getText().length() > 12){
                    errUsername.setText(R.string.username_612);
                    errUsername.setVisibility(View.VISIBLE);
                }else{
                    errUsername.setVisibility(View.GONE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(etPassword.getText().length() <= 8){
                    errPassword.setText(R.string.password_more8);
                    errPassword.setVisibility(View.VISIBLE);
                }else if(!isAlphanumeric(etPassword.getText().toString())){
                    errPassword.setText(R.string.password_alphanumeric);
                    errPassword.setVisibility(View.VISIBLE);
                }else{
                    errPassword.setVisibility(View.GONE);
                }
            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isNumeric(etPhone.getText().toString())){
                    errPhone.setText(R.string.phone_must_numbers);
                    errPhone.setVisibility(View.VISIBLE);
                }else if(etPhone.getText().length() < 10 || etPhone.getText().length() > 12) {
                    errPhone.setText(R.string.phone_1012);
                    errPhone.setVisibility(View.VISIBLE);
                }else{
                    errPhone.setVisibility(View.GONE);
                }
            }
        });

        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                errAgree.setVisibility(View.GONE);
            }
        });
    }

    private void setButtons() {
        btnLogin.setOnClickListener(v -> {
            finish();
        });

        btnCreateAccount.setOnClickListener(x -> {
            boolean success = true;

//            username
            if(etUsername.getText().length() < 6 || etUsername.getText().length() > 12){
                errUsername.setText(R.string.username_612);
                errUsername.setVisibility(View.VISIBLE);
                success = false;
            }else if(!User.usernameIsUnique(this, etUsername.getText().toString())){
                errUsername.setText(R.string.username_exists);
                errUsername.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errUsername.setVisibility(View.GONE);
            }

//            password
            if(etPassword.getText().length() <= 8){
                errPassword.setText(R.string.password_more8);
                errPassword.setVisibility(View.VISIBLE);
                success = false;
            }else if(!isAlphanumeric(etPassword.getText().toString())){
                errPassword.setText(R.string.password_alphanumeric);
                errPassword.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errPassword.setVisibility(View.GONE);
            }

//            confirm password
            if(!etConfirm.getText().toString().equals(etPassword.getText().toString())){
                errConfirm.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errConfirm.setVisibility(View.GONE);
            }

//            phone
            if(!isNumeric(etPhone.getText().toString())){
                errPhone.setText(R.string.phone_must_numbers);
                errPhone.setVisibility(View.VISIBLE);
                success = false;
            }else if(etPhone.getText().length() < 10 || etPhone.getText().length() > 12) {
                errPhone.setText(R.string.phone_1012);
                errPhone.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errPhone.setVisibility(View.GONE);
            }

//            Date of birth
            if(tvDate.getText().toString().isEmpty()){
                errDate.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errDate.setVisibility(View.GONE);
            }

//            gender
            int selectedGenderID = rgGender.getCheckedRadioButtonId();
            rbSelectedGender = (RadioButton) findViewById(selectedGenderID);
            String gender = "";
            if(rbSelectedGender == null){
                errGender.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errGender.setVisibility(View.GONE);
                gender = rbSelectedGender.getText().toString();
            }

//            agree
            if(!cbAgree.isChecked()){
                errAgree.setText(R.string.agree_must);
                errAgree.setVisibility(View.VISIBLE);
                success = false;
            }else{
                errAgree.setVisibility(View.GONE);
            }

            if(success){
                User user = new User(
                        0,
                        etUsername.getText().toString(),
                        etPassword.getText().toString(),
                        etPhone.getText().toString(),
                        gender,
                        0,
                        selectedDate);
                User.insertUser(this, user);
                Toast.makeText(this, "Create Account Success!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}