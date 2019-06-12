package com.example.cardtrix;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CardViewActivity extends AppCompatActivity {
    int id,num1,num2;
    String com,emp1,emp2,email,addr;
    EditText tf1,tf2,tf3,tf4,tf5,tf6,tf7;
    Button savebtn;
    SQLiteDatabase sqLiteDatabase;
    Cursor resultCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("Id");
        }
        Log.println(5,"ID", id+"");
        tf1 = findViewById(R.id.c_name);
        tf2 = findViewById(R.id.employee1);
        tf3 = findViewById(R.id.number1);
        tf4 = findViewById(R.id.employee2);
        tf5 = findViewById(R.id.number2);
        tf6 = findViewById(R.id.email_add);
        tf7 = findViewById(R.id.addr);
        savebtn = findViewById(R.id.saveBtn);
        sqLiteDatabase = openOrCreateDatabase("MainDB", MODE_PRIVATE, null);
        resultCursor = sqLiteDatabase.rawQuery("Select * from CardTable where C_ID = "+id, null);
        if(resultCursor.getCount() > 0){
            resultCursor.moveToFirst();
            com = resultCursor.getString(1);
            emp1 = resultCursor.getString(2);
            num1 = resultCursor.getInt(3);
            emp2 = resultCursor.getString(4);
            num2 = resultCursor.getInt(5);
            email = resultCursor.getString(6);
            addr = resultCursor.getString(7);
            tf1.setText(com);
            tf2.setText(emp1);
            tf3.setText(num1+"");
            tf4.setText(emp2);
            tf5.setText(num2+"");
            tf6.setText(email);
            tf7.setText(addr);
            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues cv = new ContentValues();
                    cv.put("C_Name",tf1.getText().toString());
                    cv.put("Emp1",tf2.getText().toString());
                    cv.put("Num1",tf3.getText().toString());
                    cv.put("Emp2",tf4.getText().toString());
                    cv.put("Num2",tf5.getText().toString());
                    cv.put("Email",tf6.getText().toString());
                    cv.put("Address",tf7.getText().toString());
                    sqLiteDatabase.update("CardTable",cv,"C_ID = ?",new String[]{id+""});
                }
            });
        }
    }
}
