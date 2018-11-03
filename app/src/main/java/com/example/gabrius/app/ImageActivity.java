package com.example.gabrius.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.FileInputStream;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //Geting image from sent file name
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try{
            FileInputStream is = openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //Displaying gotten image
        ImageView imageView = findViewById(R.id.bigger_image);
        imageView.setImageBitmap(bmp);
    }
}
