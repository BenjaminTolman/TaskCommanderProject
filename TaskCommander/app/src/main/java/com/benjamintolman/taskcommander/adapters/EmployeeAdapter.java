package com.benjamintolman.taskcommander.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;

import java.util.ArrayList;

public class EmployeeAdapter extends BaseAdapter {

    private static final String TAG = "BookAdapter";
    public static final int ID_CONSTANT = 0x01000000;

    final ArrayList<Employee> mCollection;
    final Context mContext;

    public EmployeeAdapter(ArrayList<Employee> mCollection, Context mContext) {
        this.mCollection = mCollection;
        this.mContext = mContext;
    }

    @Override
    // Return the number of items in the collection.
    public int getCount() {
        if (mCollection != null) {
            return mCollection.size();
        } else {
            Log.i(TAG, "Could not get count of collection.");
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        // Return an item at a specific position.
        if (mCollection != null && position >= 0 && position <= mCollection.size()) {
            return mCollection.get(position);
        } else {
            Log.i(TAG, "getItem: There was a problem retrieving an item.");
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        // Return a unique item id for an item at a specified position.

        if (mCollection != null && position >= 0 && position <= mCollection.size()) {
            return ID_CONSTANT + position;
        } else {
            Log.i(TAG, "getItemId: There was a problem assigning the ID");
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Return a child view for an item at a specified position.
        if (convertView == null) {
            //Is the recycleable view we are using from our created context.
            convertView = LayoutInflater.from(mContext).inflate(R.layout.employee_item_layout, parent, false);
        }

        Employee employee = (Employee) getItem(position);

        if (position <= getCount()) {
            TextView eName = convertView.findViewById(R.id.employee_item_name);


            //ImageView image = convertView.findViewById(R.id.imageView);
            eName.setText(employee.getName());

            //bookName.setText(book.getmTitle());
            //String icon = book.getmImageURL();
            //Picasso.get().load(icon).into(image);
        }

        return convertView;
    }
}