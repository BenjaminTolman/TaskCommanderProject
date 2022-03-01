package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.R;

public class JobDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "JobDetailsFragment";

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

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}