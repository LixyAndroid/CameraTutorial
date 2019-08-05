package com.example.cameratutorial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT;

public class CameraViewActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {

    private JavaCameraView mCameraView;

    private int cameraIndex = 0;

    int option = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.camrea_view_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // return super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id) {
            case R.id.invert:
                option = 1;
                break;
            case R.id.edge:
                option = 2;
                break;
            case R.id.sobel:
                option = 3;
                break;
            case R.id.boxblur:
                option = 4;
                break;
            default:
                option = 0;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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


        mCameraView = findViewById(R.id.cv_camera);
        //setup frame listener
        mCameraView.setCvCameraViewListener(this);

        mCameraView.setVisibility(SurfaceView.VISIBLE);

        mCameraView.enableView();


        RadioButton backOption = findViewById(R.id.backCameraOption);
        RadioButton frontOption = findViewById(R.id.frontCameraOption);


        backOption.setOnClickListener(this);
        frontOption.setOnClickListener(this);

        //默认选择后置摄像头
        backOption.setSelected(true);


    }


    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();



        process(frame);

        return frame;
    }

    private void process(Mat frame) {
        if (option == 1) {
            //反色
            Core.bitwise_not(frame, frame);
        } else if (option == 2) {
            Mat edges = new Mat();
            //边缘,Canny算法
            Imgproc.Canny(frame, edges, 100, 200, 3, false);
            Mat result = Mat.zeros(frame.size(), frame.type());
            frame.copyTo(result, edges);
            result.copyTo(frame);

            //释放资源
            edges.release();
            result.release();

        } else if (option == 3) {
            //梯度
            Mat gradx = new Mat();
            Imgproc.Sobel(frame, gradx, CvType.CV_32F, 1, 0);
            Core.convertScaleAbs(gradx, gradx);
            gradx.copyTo(frame);
            //释放资源
            gradx.release();

        } else if (option == 4) {

            //模糊
            Mat temp = new Mat();
            Imgproc.blur(frame, temp, new Size(15, 15));
            temp.copyTo(frame);
            //不要忘记释放资源
            temp.release();

        } else {
            //do nothing
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraView != null) {
            mCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraView != null) {
            mCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraView != null) {
            mCameraView.setCameraIndex(cameraIndex);
            mCameraView.enableView();
        }

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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frontCameraOption) {
            cameraIndex = CAMERA_ID_FRONT;
        } else if (id == R.id.backCameraOption) {
            cameraIndex = CameraBridgeViewBase.CAMERA_ID_BACK;
        }

        mCameraView.setCameraIndex(cameraIndex);
        if (mCameraView != null) {
            mCameraView.disableView();
        }

        mCameraView.enableView();
    }
}
