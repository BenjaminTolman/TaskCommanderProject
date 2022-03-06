package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.adapters.EmployeeAdapter;
import com.benjamintolman.taskcommander.adapters.JobsAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class EmployeeDetailsFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    public static final String TAG = "EmployeesFragment";

    private GoogleMap mMap;

    private ImageView profileImage;
    private TextView nameText;
    private TextView phoneText;

    private ListView jobList;

    //todo get actual last lat/lon from selectec employee
    private double mapLat = 37.3315;
    private double mapLon = -121.9700;

    public static EmployeeDetailsFragment newInstance() {

        Bundle args = new Bundle();

        EmployeeDetailsFragment fragment = new EmployeeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                //todo replace this with employee info.
        View view = inflater.inflate(R.layout.employee_details_layout, container, false);

        profileImage = view.findViewById(R.id.employee_details_profileimage);
        nameText = view.findViewById(R.id.employee_details_name_text);
        phoneText = view.findViewById(R.id.employee_details_phone_text);

        String imageUri = "https://firebasestorage.googleapis.com/v0/b/taskcommander-3f0e3.appspot.com/o/" + MainActivity.selectedEmployee.getEmail() + "profile.jpg?alt=media&token=fa379ac1-e777-4322-b4d1-8b9e11ece91e";

        int radius = 100;
        int margin = 5;
        Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(imageUri)
                .transform(transformation).into((profileImage));

        nameText.setText(MainActivity.selectedEmployee.getName());
        phoneText.setText(MainActivity.selectedEmployee.getPhoneFormatted());

        JobsAdapter jobsAdapter = new JobsAdapter(MainActivity.jobs, getContext());

        jobList = view.findViewById(R.id.employee_details_job_listview);
        jobList.setAdapter(jobsAdapter);
        jobList.setOnItemClickListener(this);


        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.google_map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        Activity activity = getActivity();
        activity.setTitle("Employee Details");
        MainActivity.currentScreen = "Employee Details";

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        zoomInCamera();
    }

    private void zoomInCamera(){
        if(mMap == null){
            return;
        }

        LatLng newLoc = new LatLng(mapLat, mapLon);
        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(newLoc, 10);
        mMap.moveCamera(cameraMovement);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}