package com.example.cameratutorial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        staticLoadsdk();
        Button processBtn = findViewById(R.id.button);
        Button goCameraDemoBtn = findViewById(R.id.camera_btn);
        processBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert2Gray();
            }
        });

        goCameraDemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,CameraViewActivity.class);
                startActivity(intent);
            }
        });

    }

    private void staticLoadsdk() {
        boolean load = OpenCVLoader.initDebug();
        if (load) {
            Log.i("testCV", "OpenCV loaded successfully!");
        }
    }


    private void convert2Gray() {

        Mat src = new Mat();
        Mat temp = new Mat();
        Mat dst = new Mat();


        Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.mipmap.lena);

        Utils.bitmapToMat(image, src);
        Imgproc.cvtColor(src, temp, Imgproc.COLOR_RGBA2BGR);
        Log.i("CV", "image type: " + (temp.type() == CvType.CV_8UC3));

        Imgproc.cvtColor(temp, dst, Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(dst, image);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(image);


        //release memory

        src.release();
        temp.release();
        dst.release();


    }






}
