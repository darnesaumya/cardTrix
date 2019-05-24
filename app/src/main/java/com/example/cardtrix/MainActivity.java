package com.example.cardtrix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    RelativeLayout rl1;
    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl1 = findViewById(R.id.rl1);
        t1 = findViewById(R.id.t1);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardactivityintent = new Intent(MainActivity.this, CardViewActivity.class);
                startActivity(cardactivityintent);
            }
        });
        try {
            FileOutputStream fOut = openFileOutput("Test", MODE_APPEND);
            String s = "Hello";
            fOut.write(s.getBytes());
            fOut.close();
            FileInputStream fin = openFileInput("Test");
            String temp = "";
            int c = fin.read();
            while(c != -1){
                temp = temp + Character.toString((char)c);
                c = fin.read();
            }
            t1.setText(temp);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
