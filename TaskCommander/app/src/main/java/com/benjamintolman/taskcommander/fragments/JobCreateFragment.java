package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JobCreateFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "JobCreationFragment";

    public static String assignmentSelection;

    //todo is this a manager? Does it even matter here? Should be a manager if they get to this page.

    EditText jobNameInput;
    EditText jobAddressInput;
    EditText jobTimeInput;
    EditText jobDateInput;
    EditText jobNotesInput;
    EditText clientNameInput;
    EditText clientPhoneInput;
    EditText employeeAssignment;
    Button saveButton;
    Button cancelButton;


    public static JobCreateFragment newInstance() {

        Bundle args = new Bundle();

        JobCreateFragment fragment = new JobCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.job_create_layout, container, false);

        setHasOptionsMenu(true);

        //todo some of these need to open a selector on top of view.
        jobNameInput = view.findViewById(R.id.job_creation_name);
        jobAddressInput = view.findViewById(R.id.job_creation_address);
        jobTimeInput = view.findViewById(R.id.job_creation_time);
        jobDateInput = view.findViewById(R.id.job_creation_date);
        jobNotesInput = view.findViewById(R.id.job_creation_notes);
        clientNameInput = view.findViewById(R.id.job_creation_client_name);
        clientPhoneInput = view.findViewById(R.id.job_creation_phone);
        employeeAssignment = view.findViewById(R.id.job_creation_assignment);
        employeeAssignment.setOnClickListener(this);

        saveButton = view.findViewById(R.id.job_creation_save_button);
        saveButton.setOnClickListener(this);
        cancelButton = view.findViewById(R.id.job_creation_cancel_button);
        cancelButton.setOnClickListener(this);

        Activity activity = getActivity();
        activity.setTitle("Create Job");

        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == employeeAssignment.getId()) {
            Log.d("employee Assignment", "CLICKED");

            //todo add to backstack and deal with that.
            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    EmployeeChooserFragment.newInstance()
            ).commit();

            return;
        }

        if (view.getId() == saveButton.getId()) {
            saveJob();


            if (view.getId() == cancelButton.getId()) {
                Log.d(TAG, "Cancel was clicked");

                getParentFragmentManager().beginTransaction().replace(
                        R.id.fragment_holder,
                        DashboardFragment.newInstance()
                ).commit();
            }
        }
    }

        @Override
        public void onCreateOptionsMenu (@NonNull Menu menu, @NonNull MenuInflater inflater){
            //super.onCreateOptionsMenu(menu, inflater);
            inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.jobs_create_menu, menu);
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            String id = item.toString();
            Intent intent;

            switch (id) {
                case "save":
                    Log.d(TAG, "SAVE was clicked");
                    saveJob();

                    break;

                case "nav":
                    Log.d(TAG, "Nav was clicked");
                    break;

                default:
                    return false;
            }
            return false;
        }


        public void saveJob() {
            //todo validate these
            String jobName = jobNameInput.getText().toString();
            String jobAddress = jobAddressInput.getText().toString();
            String jobTime = jobTimeInput.getText().toString();
            String jobDate = jobDateInput.getText().toString();
            String jobNotes = jobNotesInput.getText().toString();
            String clientName = clientNameInput.getText().toString();
            String clientPhone = clientPhoneInput.getText().toString();
            String employeeAssigned = "Bill Clay";


            //todo this should be getting a company ID and an Employee ID from the selected employee and the current manager.
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name

            Map<String, Object> job = new HashMap<>();
            job.put("name", jobName);
            job.put("address", jobAddress);
            job.put("time", jobTime);
            job.put("date", jobDate);
            job.put("notes", jobNotes);
            job.put("cName", clientName);
            job.put("cPhone", clientPhone);
            job.put("companycode", MainActivity.currentUser.getCompanyCode());
            job.put("assigned", employeeAssigned);
            //this should be an ID not whatever we have

            //todo adjust this, better way to track job

            //db.collection(companyCode).document("users").collection("list").document(email).set(user)
            db.collection("jobs").document(jobName).set(job)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");

                            Job newJob = new Job(
                                    jobName,
                                    jobAddress,
                                    jobTime,
                                    jobDate,
                                    jobNotes,
                                    clientName,
                                    clientPhone,
                                    employeeAssigned
                            );

                            MainActivity.jobs.add(newJob);

                            getParentFragmentManager().beginTransaction().replace(
                                    R.id.fragment_holder,
                                    DashboardFragment.newInstance()
                            ).commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

        }
    }
