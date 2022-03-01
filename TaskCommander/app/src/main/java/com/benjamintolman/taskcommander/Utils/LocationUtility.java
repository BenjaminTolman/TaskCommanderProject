package com.benjamintolman.taskcommander.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationUtility extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private static final int REQUEST_LOCATION_PERMISSIONS = 0x01001;

    LocationManager mLocationManager;

    boolean mRequestingUpdates = false;

    //todo plug this in to actually get location data

    public void getLocationData(Context context){

            // Get our location manager.
            mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // Check if we have permissions.
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){

                // Get our last known location and check if it's a valid location.
                Location lastKnown = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                // Display the latitude and longitude.
                if(lastKnown != null){

                    //TextView latitude = (TextView) findViewById(R.id.text_latitude);
                    //latitude.setText(Double.toString(lastKnown.getLatitude()));

                    //TextView longitude = (TextView) findViewById(R.id.text_longitude);
                    //longitude.setText(Double.toString(lastKnown.getLongitude()));
                }
            } else{
                // Request permissions if we don't have them.
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);
            }
        }

    @Override
    public void onClick(View view) {
        // Check to see if we have permission and that we're not already requesting updates.
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && !mRequestingUpdates) {
            // Request location updates using 'this' as our LocationListener.
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000,
                    10.0f,
                    this);
            // Track that we're requesting updates so we don't request them twice.
            mRequestingUpdates = true;

            Log.d("asdfsadf", "asdfsaf");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update the UI with lat/long data from the newly acquired location.
       // TextView latitude = (TextView) findViewById(R.id.text_latitude);
        //latitude.setText(Double.toString(location.getLatitude()));

       // TextView longitude = (TextView) findViewById(R.id.text_longitude);
       // longitude.setText(Double.toString(location.getLongitude()));

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
