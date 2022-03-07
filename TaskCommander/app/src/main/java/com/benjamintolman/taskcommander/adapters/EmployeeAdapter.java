package com.benjamintolman.taskcommander.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class EmployeeAdapter extends BaseAdapter {

    private static final String TAG = "BookAdapter";
    public static final int ID_CONSTANT = 0x01000000;

    final ArrayList<Employee> mCollection;
    final Context mContext;

    int radius = 100;
    int margin = 5;
    Transformation transformation = new RoundedCornersTransformation(radius, margin);

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
            TextView busyText = convertView.findViewById(R.id.job_creation_employee_selector_busytext);

            //todo put in the how many jobs they have here.
            //todo for each job in jobs, if it's assigned to this user, then yes.

            ImageView image = convertView.findViewById(R.id.employee_item_profile_image);
            eName.setText(employee.getName());

            if(MainActivity.currentScreen.equals("Employees")){
                busyText.setVisibility(View.GONE);
            }else{
                //todo
                //If this is a jobs edit/create screen check jobs for this employee
                //If the job is assigned to them, and the current hour of the job
                //assignment screen is the same then user is busy
                //todo add in if =/- an hour for hour time.

                busyText.setText("");
                for (int i = 0; i < MainActivity.jobs.size(); i++) {
                    if(MainActivity.jobs.get(i).getEmployeeAssigned().equals(employee.getName())){

                        int day = MainActivity.jobs.get(i).getJobDay();
                        int month = MainActivity.jobs.get(i).getJobMonth();
                        int year = MainActivity.jobs.get(i).getJobYear();
                        int hour = MainActivity.jobs.get(i).getJobHour();
                        int hourPlus = hour += 1;
                        int hourMinus = hour -= 1;

                        if(hour <= MainActivity.selectorJobHour &&
                            day == MainActivity.selectorJobDay &&
                            month == MainActivity.selectorJobMonth &&
                            year == MainActivity.selectorJobYear ){
                                busyText.setText("Busy at this time.");
                        }
                    }
                }
            }

            String imageUri = "https://firebasestorage.googleapis.com/v0/b/taskcommander-3f0e3.appspot.com/o/" + employee.getEmail() + "profile.jpg?alt=media&token=fa379ac1-e777-4322-b4d1-8b9e11ece91e";
            Picasso.get().load(imageUri)
                    .transform(transformation).into((image));
        }

        return convertView;
    }
}