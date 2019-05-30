package com.example.cardtrix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RelativeLayout rl1;
    Toolbar toolbar;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.nav_view);
        rl1 = findViewById(R.id.rl1);
        t1 = findViewById(R.id.t1);
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this , drawerLayout, R.string.Open, R.string.Close);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardActivityIntent = new Intent(MainActivity.this, CardViewActivity.class);
                startActivity(cardActivityIntent);
            }
        });
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
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
        if(id == R.id.add_new)
            Toast.makeText(getApplicationContext(),"Add new", Toast.LENGTH_LONG).show();
        return true;
    }
}
