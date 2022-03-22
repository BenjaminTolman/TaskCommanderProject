package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.opengl.Visibility;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnMapReadyCallback, AdapterView.OnItemClickListener, GoogleMap.InfoWindowAdapter {

    public static final String TAG = "JobDetailsFragment";

    private GoogleMap mMap;

    TextView jobNameInput;
    TextView jobAddressInput;
    TextView jobTimeInput;
    TextView jobDateInput;
    TextView jobNotesInput;
    TextView clientNameInput;
    TextView clientPhoneInput;
    TextView employeeAssignment;
    Button saveButton;
    Button cancelButton;
    Button editButton;

    boolean jobStatusBool = false;
    Spinner jobStatus;

    public static JobDetailsFragment newInstance() {

        Bundle args = new Bundle();

        JobDetailsFragment fragment = new JobDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_details_layout, container, false);


        jobNameInput = view.findViewById(R.id.job_details_name);
        jobAddressInput = view.findViewById(R.id.job_details_address);
        jobTimeInput = view.findViewById(R.id.job_details_time);
        jobDateInput = view.findViewById(R.id.job_details_date);
        jobNotesInput = view.findViewById(R.id.job_details_notes);
        clientNameInput = view.findViewById(R.id.job_details_client_name);
        clientPhoneInput = view.findViewById(R.id.job_details_phone);
        employeeAssignment = view.findViewById(R.id.job_details_assignment);

        jobNameInput.setText(MainActivity.currentJob.getJobTitle());
        jobAddressInput.setText(MainActivity.currentJob.getJobAddress());
        jobTimeInput.setText(MainActivity.currentJob.getJobTime());
        jobDateInput.setText(MainActivity.currentJob.getJobDate());
        jobNotesInput.setText(MainActivity.currentJob.getJobNotes());
        clientNameInput.setText(MainActivity.currentJob.getClientName());

        String fPhone = PhoneNumberUtils.formatNumber(MainActivity.currentJob.getClientPhone(), "US"); // output: (202) 555-0739
        clientPhoneInput.setText(fPhone);

        employeeAssignment.setText(MainActivity.currentJob.getEmployeeAssigned());

        cancelButton = view.findViewById(R.id.job_details_cancel_button);
        cancelButton.setOnClickListener(this);
        editButton = view.findViewById(R.id.job_details_edit_button);
        editButton.setOnClickListener(this);

        if(MainActivity.currentUser.getRole().equals("Employee")){
            editButton.setVisibility(View.GONE);
        }

        jobStatus = view.findViewById(R.id.job_details_job_status_spinner);
        //Create and ArrayAdapter using the string array in strings and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.jobstatus, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter and on select to the spinner
        jobStatus.setAdapter(adapter);

        String jobStatusString = MainActivity.currentJob.getJobStatus();
        if(jobStatusString.equals("Posted")){
            jobStatus.setSelection(0);
        }else if(jobStatusString.equals("In Progress")){
            jobStatus.setSelection(1);
        }else if(jobStatusString.equals("Complete")){
            jobStatus.setSelection(2);
        }

        jobStatus.setOnItemSelectedListener(this);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.google_map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        Activity activity = getActivity();
        activity.setTitle("Job Details");
        MainActivity.currentScreen = "Job Details";

        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == cancelButton.getId()){
            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    JobsFragment.newInstance()
            ).commit();
        }
        if(view.getId() == editButton.getId()){
            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    JobEditFragment.newInstance()
            ).commit();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(jobStatusBool == false){
            jobStatusBool = true;
            return;
        }

        String status = jobStatus.getSelectedItem().toString();

        MainActivity.currentJob.setJobStatus(status);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name

        Map<String, Object> job = new HashMap<>();
        job.put("status", status);
        //this should be an ID not whatever we have

        //db.collection(companyCode).document("users").collection("list").document(email).set(user)
        db.collection("jobs").document(MainActivity.currentJob.getJobTitle()).set(job, SetOptions.merge())

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
        //addMapMarker();
        zoomInCamera();
    }


    //todo get job location for this.
    private void zoomInCamera(){
        if(mMap == null){
            return;
        }

        if(MainActivity.currentUser != null){
            if(MainActivity.currentUser.getLat() != 0 && MainActivity.currentUser.getLon() != 0){
                //LatLng newLoc = new LatLng(MainActivity.currentUser.getLat(), MainActivity.currentUser.getLon());
                //CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(newLoc, 10);
                //mMap.moveCamera(cameraMovement);

                getLocationFromAddress(MainActivity.currentJob.getJobAddress());
                return;
            }
            Toast.makeText(getContext(), "This employee does not have Location Data.", Toast.LENGTH_SHORT).show();
            return;
        }

        //LatLng newLoc = new LatLng(MainActivity.currentUser.getLat(), MainActivity.currentUser.getLon());
        //CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(newLoc, 5);

        //mMap.moveCamera(cameraMovement);

    }

    private void addMapMarker(double lats, double lons) {
        if(mMap == null){
            return;
        }else{
            MarkerOptions options = new MarkerOptions();
            options.title(MainActivity.currentJob.getJobTitle())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //options.snippet("Job details");
            LatLng jobLocation = new LatLng(lats, lons);
            options.position(jobLocation);
            mMap.addMarker(options);
        }
    }

    public void getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return;
            }
            try{
                Address location = address.get(0);

                location.getLatitude();
                location.getLatitude();;

                LatLng newLoc = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(newLoc, 15);

                addMapMarker(location.getLatitude(), location.getLongitude());

                mMap.moveCamera(cameraMovement);

            }catch(Exception e){
                Toast.makeText(getContext(), "There is no valid address for this job.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

        } catch (IOException e) {
                e.printStackTrace();
            }
    }
}