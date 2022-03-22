package com.benjamintolman.taskcommander.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.fragments.DashboardFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LocationUtility extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_LOCATION_PERMISSIONS = 0x01001;

    LocationManager mLocationManager;

    boolean mRequestingUpdates = false;
    Context mContext;
    Activity mActivity;

    public LocationUtility(Context context, Activity activity){
        this.mContext = context;
        this.mActivity = activity;
    }

    public boolean getLocationPermission(){
        // Check if we have permissions.
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

            return true;

        } else{
            // Request permissions if we don't have them.
            ActivityCompat.requestPermissions(mActivity, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);

            return false;
        }
    }

    public void getUserLocationData(Context context){


        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && !mRequestingUpdates) {
            // Request location updates using 'this' as our LocationListener.

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000,
                    10.0f,
                    this);
            // Track that we're requesting updates so we don't request them twice.
            mRequestingUpdates = true;

        }
    }

//    @Override
//    public void onClick(View view) {
//        // Check to see if we have permission and that we're not already requesting updates.
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED && !mRequestingUpdates) {
//            // Request location updates using 'this' as our LocationListener.
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    2000,
//                    10.0f,
//                    this);
//            // Track that we're requesting updates so we don't request them twice.
//            mRequestingUpdates = true;
//        }
//    }

    @Override
    public void onLocationChanged(Location location) {
        // Update the UI with lat/long data from the newly acquired location.
       // TextView latitude = (TextView) findViewById(R.id.text_latitude);
        //latitude.setText(Double.toString(location.getLatitude()));

       // TextView longitude = (TextView) findViewById(R.id.text_longitude);
       // longitude.setText(Double.toString(location.getLongitude()));

        MainActivity.currentUser.setLat(location.getLatitude());
        MainActivity.currentUser.setLon(location.getLongitude());

        Log.d("LAT LON ", String.valueOf(MainActivity.currentUser.getLat()) + String.valueOf(MainActivity.currentUser.getLon()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name

        Map<String, Object> user = new HashMap<>();
        user.put("email", MainActivity.currentUser.getEmail());
        user.put("name", MainActivity.currentUser.getName());
        user.put("password", MainActivity.currentUser.getPassword());
        user.put("phone", MainActivity.currentUser.getPhone());
        user.put("role", MainActivity.currentUser.getRole());
        user.put("companycode", MainActivity.currentUser.getCompanyCode());
        user.put("imageurl", MainActivity.currentUser.getImageURL());
        user.put("lat", MainActivity.currentUser.getLat());
        user.put("lon", MainActivity.currentUser.getLon());

        db.collection("users").document(MainActivity.currentUser.getEmail()).set(user)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    


        if(mRequestingUpdates){
            mRequestingUpdates = false;
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
