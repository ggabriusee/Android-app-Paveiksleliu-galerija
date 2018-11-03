package com.example.gabrius.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // putting json data in String array that will hold image urls
        String[] urlArray = null;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray urls = obj.getJSONArray("urls");
            int arraySize = 10; // urls.length()
            urlArray = new String[arraySize];
            for(int i = 0; i < arraySize; i++) {
                urlArray[i] = urls.getString(i);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        populateListView(urlArray);
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView listView = findViewById(R.id.list_view_main);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                // getting clicked image
                ImageView imageView = (ImageView) viewClicked;
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                try{
                    //Write image to file
                    String filename = "bitmap.png";
                    FileOutputStream sream = openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, sream);
                    sream.close();

                    //Send image file name to second activity
                    Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                    intent.putExtra("image", filename);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateListView(String[] arr) {
        ListView listView = findViewById(R.id.list_view_main);
        listView.setAdapter(new ImageAdapter(this, R.layout.list_item_layout, arr));
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("dog_urls.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
