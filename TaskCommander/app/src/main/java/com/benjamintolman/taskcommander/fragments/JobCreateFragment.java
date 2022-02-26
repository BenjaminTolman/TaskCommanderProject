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

import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.FirestoreUtility;

public class JobCreateFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "JobCreationFragment";

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

        saveButton = view.findViewById(R.id.job_creation_save_button);
        saveButton.setOnClickListener(this);
        cancelButton = view.findViewById(R.id.job_creation_cancel_button);
        cancelButton.setOnClickListener(this);

        //todo should be a check to see if this is a manager as soon as possible and keep the user profile in main activity.
        Activity activity = getActivity();
        activity.setTitle("Create Job");

        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == saveButton.getId()) {
            Log.d(TAG, "SAVE was clicked");

            String jobName = jobNameInput.getText().toString();
            String jobAddress = jobAddressInput.getText().toString();
            String jobTime = jobTimeInput.getText().toString();
            String jobDate = jobDateInput.getText().toString();
            String jobNotes = jobNotesInput.getText().toString();
            String clientName = clientNameInput.getText().toString();
            String clientPhone = clientPhoneInput.getText().toString();
            String employeeAssigned = "Bill Clay";

            //Job newJob = new Job(jobName, jobAddress, jobTime,jobDate, jobNotes, clientName, clientPhone, employeeAssigned);

            //todo NOW we making the reference and doing this.
            FirestoreUtility.createJob(jobName, jobAddress, jobTime, jobDate, jobNotes, clientName, clientPhone, employeeAssigned);
            //todo validate this and then save to firebase
            //todo do we have a network connection?

            //todo finally we go back to dashboard.

            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    DashboardFragment.newInstance()
            ).commit();
        }

        if (view.getId() == cancelButton.getId()) {
            Log.d(TAG, "Cancel was clicked");

            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    DashboardFragment.newInstance()
            ).commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.jobs_create_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String id = item.toString();
        Intent intent;

        switch(id){
            case "save":
                Log.d(TAG, "SAVE was clicked");

                String jobName = jobNameInput.getText().toString();
                String jobAddress = jobAddressInput.getText().toString();
                String jobTime = jobTimeInput.getText().toString();
                String jobDate = jobDateInput.getText().toString();
                String jobNotes = jobNotesInput.getText().toString();
                String clientName = clientNameInput.getText().toString();
                String clientPhone = clientPhoneInput.getText().toString();
                String employeeAssigned = "Bill Clay";

                //Job newJob = new Job(jobName, jobAddress, jobTime,jobDate, jobNotes, clientName, clientPhone, employeeAssigned);

                //todo NOW we making the reference and doing this.
                FirestoreUtility.createJob(jobName, jobAddress, jobTime,jobDate, jobNotes, clientName, clientPhone, employeeAssigned);
                //todo validate this and then save to firebase
                //todo do we have a network connection?

                //todo finally we go back to dashboard.

                getParentFragmentManager().beginTransaction().replace(
                        R.id.fragment_holder,
                        DashboardFragment.newInstance()
                ).commit();

                break;

            case "nav":
                Log.d(TAG, "Nav was clicked");
                break;

            default:
                return false;
        }
        return false;
    }

}