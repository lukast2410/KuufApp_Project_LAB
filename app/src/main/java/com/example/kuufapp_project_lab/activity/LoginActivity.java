package com.example.kuufapp_project_lab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kuufapp_project_lab.R;
import com.example.kuufapp_project_lab.model.Product;
import com.example.kuufapp_project_lab.model.Transaction;
import com.example.kuufapp_project_lab.model.User;
import com.example.kuufapp_project_lab.util.DBOpenHelper;
import com.example.kuufapp_project_lab.util.HelperData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout layout;
    EditText etUsername, etPassword;
    TextView errUsername, errPassword, errLogin;
    Button btnLogin, btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeBackground();
        initializeLoginForm();
        checkProduct();
        setInputForm();
        setButton();
    }

    private void initializeBackground() {
        layout = (RelativeLayout) findViewById(R.id.login_layout);
        AnimationDrawable animBg = (AnimationDrawable) layout.getBackground();
        animBg.setEnterFadeDuration(3000);
        animBg.setExitFadeDuration(4000);
        animBg.start();
    }

    private void initializeLoginForm() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        errUsername = (TextView) findViewById(R.id.tv_username_empty);
        errPassword = (TextView) findViewById(R.id.tv_password_empty);
        errLogin = (TextView) findViewById(R.id.tv_login_failed);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCreateAccount = (Button) findViewById(R.id.btn_create_account);
    }

    private void setButton() {
        btnLogin.setOnClickListener(x -> {
            boolean empty = false;
            if(etUsername.getText().toString().equals("")){
                errUsername.setVisibility(View.VISIBLE);
                empty = true;
            }else{
                errUsername.setVisibility(View.GONE);
            }

            if(etPassword.getText().toString().equals("")){
                errPassword.setVisibility(View.VISIBLE);
                empty = true;
            }else{
                errPassword.setVisibility(View.GONE);
            }

            if(empty)
                return;

            User user = User.userLogin(LoginActivity.this, etUsername.getText().toString(), etPassword.getText().toString());
            HelperData.getInstance().setCurrentUser(user);
            if(user != null){
                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();
                errLogin.setVisibility(View.GONE);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                errLogin.setVisibility(View.VISIBLE);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setInputForm() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(etUsername.getText().toString().equals("")){
                    errUsername.setVisibility(View.VISIBLE);
                }else{
                    errUsername.setVisibility(View.GONE);
                }

                if(etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    btnLogin.setEnabled(false);
                }else{
                    btnLogin.setEnabled(true);
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
                if(etPassword.getText().toString().equals("")){
                    errPassword.setVisibility(View.VISIBLE);
                }else{
                    errPassword.setVisibility(View.GONE);
                }

                if(etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    btnLogin.setEnabled(false);
                }else{
                    btnLogin.setEnabled(true);
                }
            }
        });
    }

    private void checkProduct() {
        if(Product.getAllProduct(this) == null){
            //get data from api and insert to db
            final String url = "https://api.jsonbin.io/b/5eb51c6947a2266b1474d701/7";
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonProducts = response.getJSONArray("items");
                        for(int i=0;i<jsonProducts.length();i++){
                            JSONObject jsonObj = jsonProducts.getJSONObject(i);
                            Product product = new Product(
                                    0,
                                    jsonObj.getString("name"),
                                    jsonObj.getInt("min_player"),
                                    jsonObj.getInt("max_player"),
                                    jsonObj.getInt("price"),
                                    jsonObj.getDouble("latitude"),
                                    jsonObj.getDouble("longitude")
                            );
                            Product.insertProduct(LoginActivity.this, product);
                            Log.d("testing "+ i, "inserted");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                }
            });

            requestQueue.add(jsonReq);
        }
    }
}