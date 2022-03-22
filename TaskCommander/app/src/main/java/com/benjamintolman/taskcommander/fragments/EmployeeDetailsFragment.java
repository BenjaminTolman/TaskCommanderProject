package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.adapters.EmployeeAdapter;
import com.benjamintolman.taskcommander.adapters.JobsAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class EmployeeDetailsFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemClickListener, GoogleMap.InfoWindowAdapter {

    public static final String TAG = "EmployeeDetailsFragment";

    private GoogleMap mMap;

    private ImageView profileImage;
    private TextView nameText;
    private TextView phoneText;

    private ListView jobList;

    ArrayList<Job> employeeDetailsJobList = new ArrayList<>();

    boolean inProgress = false;

    SupportMapFragment mapFragmentV;

    public static EmployeeDetailsFragment newInstance() {

        Bundle args = new Bundle();

        EmployeeDetailsFragment fragment = new EmployeeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.employee_details_layout, container, false);

        profileImage = view.findViewById(R.id.employee_details_profileimage);
        nameText = view.findViewById(R.id.employee_details_name_text);
        phoneText = view.findViewById(R.id.employee_details_phone_text);

        String imageUri = "https://firebasestorage.googleapis.com/v0/b/taskcommander-3f0e3.appspot.com/o/" + MainActivity.selectedEmployee.getImageURL() + "?alt=media&token=fa379ac1-e777-4322-b4d1-8b9e11ece91e";

        int radius = 100;
        int margin = 5;
        Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(imageUri)
                .transform(transformation).into((profileImage));

        nameText.setText(MainActivity.selectedEmployee.getName());
        phoneText.setText(MainActivity.selectedEmployee.getPhoneFormatted());


        for (int i = 0; i < MainActivity.jobs.size(); i++) {
            employeeDetailsJobList.add(MainActivity.jobs.get(i));
        }



        for (int i = 0; i < employeeDetailsJobList.size(); i++) {
            if(!employeeDetailsJobList.get(i).getEmployeeAssigned().equals(MainActivity.selectedEmployee.getName())){
                employeeDetailsJobList.remove(i);

            }else{

            }
        }

        for (int i = 0; i < employeeDetailsJobList.size(); i++) {
            if(employeeDetailsJobList.get(i).getJobStatus().equals("In Progress")){
                inProgress = true;
                Log.d("TRUE", "IN PROGRESS WAS TRUE");
            }
            }




        JobsAdapter jobsAdapter = new JobsAdapter(employeeDetailsJobList, getContext());

        jobList = view.findViewById(R.id.employee_details_job_listview);
        jobList.setAdapter(jobsAdapter);
        jobList.setOnItemClickListener(this);



        if(inProgress) {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.google_map, mapFragment).commit();
            mapFragment.getMapAsync(this);
        }
        else {

            ConstraintLayout cl = view.findViewById(R.id.google_map_holder);
            cl.setVisibility(View.GONE);
        }


        Activity activity = getActivity();
        activity.setTitle("Employee Details");
        MainActivity.currentScreen = "Employee Details";

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


            mMap = googleMap;
            mMap.setInfoWindowAdapter(this);
            addMapMarker();
            zoomInCamera();

    }

    private void zoomInCamera(){
        if(mMap == null){
            return;
        }

        if(MainActivity.currentUser != null){
            if(MainActivity.currentUser.getLat() != 0 && MainActivity.currentUser.getLon() != 0){
                LatLng newLoc = new LatLng(MainActivity.currentUser.getLat(), MainActivity.currentUser.getLon());
                CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(newLoc, 10);
                mMap.moveCamera(cameraMovement);
                return;
            }
            Toast.makeText(getContext(), "This employee does not have Location Data.", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng newLoc = new LatLng(MainActivity.currentUser.getLat(), MainActivity.currentUser.getLon());
        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(newLoc, 5);

        mMap.moveCamera(cameraMovement);

    }

    private void addMapMarker() {
        if(mMap == null){
            return;
        }else{

            Log.d("CURRENT LAT LON", String.valueOf(MainActivity.currentUser.getLat()));

            MarkerOptions options = new MarkerOptions();
            options.title(MainActivity.currentUser.getName())
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            //options.snippet("MDV Offices");
            LatLng employeeLocation = new LatLng(MainActivity.currentUser.getLat(), MainActivity.currentUser.getLon());

            options.position(employeeLocation);
            mMap.addMarker(options);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        View contents = LayoutInflater.from(getActivity())
                .inflate(R.layout.employee_info_window, null);
        ((TextView) contents.findViewById(R.id.title)).setText(marker.getTitle());
        //((TextView) contents.findViewById(R.id.snippet)).setText(marker.getSnippet());

        return contents;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }
}