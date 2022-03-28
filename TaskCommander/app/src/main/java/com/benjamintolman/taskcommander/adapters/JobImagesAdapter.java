package com.benjamintolman.taskcommander.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JobImagesAdapter extends BaseAdapter {

    private static final String TAG = "Job Images Adapter";
    public static final int ID_CONSTANT = 0x01000000;

    final ArrayList<String> mCollection;
    final Context mContext;

    public JobImagesAdapter(ArrayList<String> mCollection, Context mContext) {
        this.mCollection = mCollection;
        this.mContext = mContext;
    }

    @Override
    // Return the number of items in the collection.
    public int getCount() {
        if(mCollection != null){
            return  mCollection.size();
        }
        else
        {
            Log.i(TAG, "Could not get count of collection.");
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        // Return an item at a specific position.
        if(mCollection != null && position >=0 && position <= mCollection.size()){
            return mCollection.get(position);
        }
        else {
            Log.i(TAG, "getItem: There was a problem retrieving an item.");
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        // Return a unique item id for an item at a specified position.

        if(mCollection != null && position >= 0 && position <= mCollection.size()){
            return ID_CONSTANT + position;
        }
        else
        {
            Log.i(TAG, "getItemId: There was a problem assigning the ID");
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Return a child view for an item at a specified position.
        if(convertView == null) {
            //Is the recycleable view we are using from our created context.
            convertView = LayoutInflater.from(mContext).inflate(R.layout.job_image_layout, parent, false);
        }
        String imageURL = (String) getItem(position);

        if(position <= getCount())
        {
            String fullURL = "https://firebasestorage.googleapis.com/v0/b/taskcommander-3f0e3.appspot.com/o/" + imageURL + "?alt=media&token=fa379ac1-e777-4322-b4d1-8b9e11ece91e";
            //Iterate through array list and get the image from the URL.
            ImageView image = convertView.findViewById(R.id.job_image_layout_imageview);
            Picasso.get().load(fullURL).into(image);

        }

        return convertView;
    }
}