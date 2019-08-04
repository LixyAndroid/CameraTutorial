package com.example.cameratutorial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;

public class CameraViewActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private JavaCamera2View mCamera2View;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        //检查权限
        requestDangerousPermissions();


        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mCamera2View = findViewById(R.id.cv_camera);
        mCamera2View.setVisibility(SurfaceView.VISIBLE);
        mCamera2View.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
        mCamera2View.enableView();


    }


    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }


    /**
     * 请求权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestDangerousPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//版本判断
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] strings = new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, strings, 100);
            }
        }
    }


}
