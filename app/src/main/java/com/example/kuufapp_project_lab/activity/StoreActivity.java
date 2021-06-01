package com.example.kuufapp_project_lab.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuufapp_project_lab.R;
import com.example.kuufapp_project_lab.adapter.ProductAdapter;
import com.example.kuufapp_project_lab.model.Product;
import com.example.kuufapp_project_lab.util.HelperData;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity implements ProductAdapter.ProductClickListener {
    Toolbar toolbar;
    private ArrayList<Product> products;
    private RecyclerView gameList;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        initializeToolbar();
        setGameList();
    }

    private void setGameList() {
        gameList = (RecyclerView) findViewById(R.id.rv_store_games);
        products = Product.getAllProduct(this);
        adapter = new ProductAdapter(products, this);
        gameList.setLayoutManager(new LinearLayoutManager(this));
        gameList.setItemAnimator(new DefaultItemAnimator());
        gameList.setAdapter(adapter);
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.store_toolbar);
        toolbar.setTitle("Game Store");
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
                return true;
            case R.id.menu_profile:
                goToActivity("profile");
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

    @Override
    public void onProductClicked(int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(HelperData.PRODUCT_DATA, products.get(position));
        startActivity(intent);
    }
}