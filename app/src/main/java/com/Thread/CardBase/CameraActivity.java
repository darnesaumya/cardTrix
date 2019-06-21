package com.Thread.CardBase;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CameraActivity extends AppCompatActivity {
    TextureView img;
    private static final int REQUEST_CAMERA_PERMISSION_RESULT = 0;
    String cameraID;
    HandlerThread cameraThread;
    Handler cameraThreadHandler;
    CameraDevice cameraDevice;
    Size previewSize;
    static SparseIntArray ORIENTATION = new SparseIntArray();

    static {
        ORIENTATION.append(Surface.ROTATION_0, 0);
        ORIENTATION.append(Surface.ROTATION_90, 90);
        ORIENTATION.append(Surface.ROTATION_180, 180);
        ORIENTATION.append(Surface.ROTATION_270, 270);
    }

    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            setupCamera(i, i1);
            connectCamera();
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

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            Toast.makeText(getApplicationContext(),"Camera connection made", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_camera);
        img = findViewById(R.id.textureView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraThread();
        if (img.isAvailable()) {
            setupCamera(img.getWidth(), img.getHeight());
            connectCamera();
        } else {
            img.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION_RESULT){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopCameraThread();
        super.onPause();
    }

    private void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String tempCameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(tempCameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                int deviceOrientation = getWindowManager().getDefaultDisplay().getRotation();
                int sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                deviceOrientation = ORIENTATION.get(deviceOrientation);
                int totalRotation = rotation(cameraCharacteristics, deviceOrientation, sensorOrientation);
                int fwidth = width;
                int fheight = height;
                boolean swap = ((sensorOrientation == 90 || sensorOrientation == 270) && (totalRotation == 0 || totalRotation == 180) || (sensorOrientation == 0 || sensorOrientation == 180) && (totalRotation == 90 || totalRotation == 270));
                if (swap) {
                    fwidth = height;
                    fheight = width;
                }
                previewSize = chooseSize(map.getOutputSizes(SurfaceTexture.class), fwidth, fheight);
                cameraID = tempCameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void connectCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraManager.openCamera(cameraID, stateCallback, cameraThreadHandler);
            }else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    Toast.makeText(this, "This app uses the camera functionality please consider granting permission", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_RESULT);
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
    private static class CompareSize implements Comparator<Size> {

        @Override
        public int compare(Size o1, Size o2) {
            return Long.signum((long) o1.getWidth() * o1.getHeight() - (long) o2.getWidth() * o2.getHeight());
        }
    }

    private static Size chooseSize(Size[] choices, int width, int height ){
        List<Size> sizeList = new ArrayList<>();
        for(Size i : choices){
            if(i.getHeight() == i.getWidth() * height/width){
                sizeList.add(i);
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new CompareSize());
        }else {
            return choices[0];
        }
    }
}
