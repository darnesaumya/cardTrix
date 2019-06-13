package com.example.cardtrix;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Add_new extends AppCompatActivity {
    EditText tf1,tf2,tf3,tf4,tf5,tf6,tf7;
    Button btn1,btn2;
    ImageView img;
    int id;
    SQLiteDatabase sqldb;
    Cursor resultCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        id = 0;
        img = findViewById(R.id.imgview);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.photo);
        tf1 = findViewById(R.id.com_name);
        tf2 = findViewById(R.id.emp1);
        tf3 = findViewById(R.id.num1);
        tf4 = findViewById(R.id.emp2);
        tf5 = findViewById(R.id.num2);
        tf6 = findViewById(R.id.email);
        tf7 = findViewById(R.id.address);
        sqldb = openOrCreateDatabase("MainDB",MODE_PRIVATE, null);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultCursor = sqldb.rawQuery("Select MAX(C_ID) from CardTable", null);
                if(resultCursor.getCount() > 0) {
                    resultCursor.moveToFirst();
                    id = resultCursor.getInt(0) + 1;
                }
                sqldb.execSQL("INSERT INTO CardTable(C_ID, C_Name, Emp1, Num1, Emp2, Num2, Email, Address) VALUES (" + id + ",'"+tf1.getText()+"' , '"+tf2.getText()+"' , '"+tf3.getText()+"' , '"+tf4.getText()+"' , '"+tf5.getText()+"' , '"+tf6.getText()+"' , '"+tf7.getText()+"')");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(pictureIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(pictureIntent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int rotate = 0;
        if(requestCode == 1 && resultCode == RESULT_OK ){
            Bundle extras = data.getExtras();
            ExifInterface image = (ExifInterface) extras.get("data");
            int orientation = image.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = ExifInterface.ORIENTATION_ROTATE_270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = ExifInterface.ORIENTATION_ROTATE_180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = ExifInterface.ORIENTATION_ROTATE_90;
                    break;
            }
            image.setAttribute(ExifInterface.TAG_ORIENTATION, rotate+"");
        }
    }
}
