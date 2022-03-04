package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class JobDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //todo get lat lon and pop in map.

    public static final String TAG = "JobDetailsFragment";

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


    //todo this needs to come from serialized Job after Alpha
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
        clientPhoneInput.setText(MainActivity.currentJob.getClientPhone());
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
}