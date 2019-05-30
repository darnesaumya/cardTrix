package com.example.cardtrix;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add_new extends AppCompatActivity {
    EditText tf1,tf2,tf3,tf4,tf5,tf6,tf7;
    Button btn1;
    SQLiteDatabase sqldb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqldb.execSQL("INSERT INTO CardTable(C_Name, Emp1, Num1, Emp2, Num2, Email, Address) VALUES ('"+tf1.getText()+"' , '"+tf2.getText()+"' , '"+tf3.getText()+"' , '"+tf4.getText()+"' , '"+tf5.getText()+"' , '"+tf6.getText()+"' , '"+tf7.getText()+"')");
            }
        });
        tf1 = findViewById(R.id.com_name);
        tf2 = findViewById(R.id.emp1);
        tf3 = findViewById(R.id.num1);
        tf4 = findViewById(R.id.emp2);
        tf5 = findViewById(R.id.num2);
        tf6 = findViewById(R.id.email);
        tf7 = findViewById(R.id.address);
        sqldb = openOrCreateDatabase("MainDB",MODE_PRIVATE, null);
    }
}
