package com.example.cardtrix;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    NavigationView navigationView;
    SQLiteDatabase sql;
    DrawerLayout drawerLayout;
    List<CardData> cardDataList;
    ListView listView;
    Cursor resultcursor;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardDataList = new ArrayList<>();
        listView = findViewById(R.id.card_list);
        CardListAdapter cardListAdapter = new CardListAdapter(this,R.layout.card_layout, cardDataList);
        listView.setAdapter(cardListAdapter);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.my_toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this , drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        setSupportActionBar(toolbar);
        toggle.syncState();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sql = openOrCreateDatabase("MainDB", MODE_PRIVATE,null);
        sql.execSQL("CREATE TABLE IF NOT EXISTS CardTable (C_Name VARCHAR , Emp1 VARCHAR, Num1 INTEGER, Emp2 VARCHAR, Num2 INTEGER, Email VARCHAR, Address VARCHAR)");
        navigationView.setNavigationItemSelectedListener(this);
        populator();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.add_new){
            Intent intent = new Intent(this, Add_new.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardDataList.clear();
        populator();
    }
    private void populator() {
        resultcursor = sql.rawQuery("Select C_Name from CardTable", null);
        if(resultcursor.getCount() > 0) {
            resultcursor.moveToFirst();
            do {
                cardDataList.add(new CardData(resultcursor.getString(0)));
            } while (resultcursor.moveToNext());
        }
        resultcursor.close();
    }
}
