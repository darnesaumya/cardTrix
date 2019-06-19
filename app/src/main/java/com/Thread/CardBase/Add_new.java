package com.Thread.CardBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Add_new extends AppCompatActivity {
    EditText tf1,tf2,tf3,tf4,tf5,tf6,tf7;
    Button btn1,btn2;
    TextureView img;
    int id;
    SQLiteDatabase sqldb;
    Cursor resultCursor;
    String cameraID;
    HandlerThread cameraThread;
    Handler cameraThreadHandler;
    static SparseIntArray ORIENTATION = new SparseIntArray();
    static {
        ORIENTATION.append(Surface.ROTATION_0,0);
        ORIENTATION.append(Surface.ROTATION_90,90);
        ORIENTATION.append(Surface.ROTATION_180,180);
        ORIENTATION.append(Surface.ROTATION_270,270);
    }
    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            setupCamera(i,i1);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };
    CameraDevice cameraDevice;
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            camera.close();
            cameraDevice = null;
        }
    };
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraThread();
        if(img.isAvailable()){
            setupCamera(img.getWidth(),img.getHeight());
        }else{
            img.setSurfaceTextureListener(surfaceTextureListener);
        }
    }
    @Override
    protected void onPause(){
        closeCamera();
        stopCameraThread();
        super.onPause();
    }
    private void closeCamera(){
        if(cameraDevice != null){
            cameraDevice.close();
            cameraDevice = null;
        }
    }
    private void setupCamera(int width, int height){
        CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String tempCameraId : cameraManager.getCameraIdList()){
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(tempCameraId);
                if(cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){
                    continue;
                }
                int deviceOrientation = getWindowManager().getDefaultDisplay().getRotation();
                int sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                deviceOrientation = ORIENTATION.get(deviceOrientation);
                int totalRotation = rotation(cameraCharacteristics, deviceOrientation, sensorOrientation);
                int fwidth = width;
                int fheight = height;
                boolean swap = ((sensorOrientation == 90 || sensorOrientation == 270) && (totalRotation == 0 || totalRotation == 180) || (sensorOrientation == 0 || sensorOrientation == 180) && (totalRotation == 90 || totalRotation == 270));
                if (swap){
                    fwidth = height;
                    fheight = width;
                }
                cameraID = tempCameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void startCameraThread(){
        cameraThread = new HandlerThread("Camera Thread");
        cameraThread.start();
        cameraThreadHandler = new Handler(cameraThread.getLooper());
    }
    private void stopCameraThread(){
        cameraThread.quitSafely();
        try {
            cameraThread.join();
            cameraThread = null;
            cameraThreadHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private int rotation(CameraCharacteristics cameraCharacteristics, int deviceOrientation, int sensorOrientation){
        sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        int rot = deviceOrientation + sensorOrientation;
        if(rot >= 360) {
            rot -= 360;
        }
        return rot;
    }
}

