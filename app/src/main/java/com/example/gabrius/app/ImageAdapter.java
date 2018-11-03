package com.example.gabrius.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class ImageAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater; //for converting xml layout-file to view object
    private String[] imageUrlArray;

    public ImageAdapter(Context context, int textViewResourseId, String[] imageUrlArray){
        super(context, textViewResourseId, imageUrlArray);
        this.inflater = ((Activity) context).getLayoutInflater();
        this.imageUrlArray = imageUrlArray;
    }

    // image data holder class
    private static class ViewHolder {
        ImageView imageView;
        String imageUrl;
        Bitmap bitmap;
    }

    //creating a view that will appear in the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        //if recycled view avaible (e.g. no longer visible) create a new one
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView;
            convertView.setTag(viewHolder);
        }

        // put data in viewHolder
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.imageUrl = imageUrlArray[position];

        //use that data to download image
        new DownloadAsyncTask().execute(viewHolder);
        return convertView;
    }

    //class to handle image downloads in background
    private static class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            ViewHolder viewHolder = params[0];
            try{
                URL imageUrl = new URL(viewHolder.imageUrl);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
            }catch (IOException e){
                Log.e("error", "downloading image failed");
                viewHolder.bitmap = null;
            }
            return viewHolder;
        }

        //set downloaded image to view object ((ViewHolder class).imageView)
        @Override
        protected void onPostExecute(ViewHolder result){
            if(result.bitmap == null){
                //if no image display default icon
                result.imageView.setImageResource(R.drawable.ic_launcher_background);
            }else {
                result.imageView.setImageBitmap(result.bitmap);
            }
        }
    }
}
