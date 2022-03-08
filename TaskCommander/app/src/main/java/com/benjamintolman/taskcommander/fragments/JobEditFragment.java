package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.NetworkUtility;
import com.benjamintolman.taskcommander.Utils.ValidationUtility;
import com.benjamintolman.taskcommander.adapters.EmployeeAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobEditFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, TimePicker.OnTimeChangedListener, DatePicker.OnDateChangedListener {

    public static final String TAG = "JobCreationFragment";

    public static String assignmentSelection;

    EditText jobNameInput;
    EditText jobAddressInput;
    TimePicker jobTimeInput;
    DatePicker jobDateInput;
    EditText jobNotesInput;
    EditText clientNameInput;
    EditText clientPhoneInput;
    Button employeeAssignment;
    Button saveButton;
    Button cancelButton;
    Button deleteButton;

    ListView employeeList;
    EmployeeAdapter employeeAdapter;

    NetworkUtility nwu = new NetworkUtility(getContext());

    boolean jobStatusBool = false;
    Spinner jobStatus;

    public static JobEditFragment newInstance() {

        Bundle args = new Bundle();

        JobEditFragment fragment = new JobEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.job_edit_layout, container, false);

        setHasOptionsMenu(true);

        jobNameInput = view.findViewById(R.id.job_edit_name);
        jobAddressInput = view.findViewById(R.id.job_edit_address);
        jobTimeInput = view.findViewById(R.id.job_edit_timepicker);
        jobTimeInput.setOnTimeChangedListener(this);
        jobDateInput = view.findViewById(R.id.job_edit_date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            jobDateInput.setOnDateChangedListener(this);
        }

        jobNotesInput = view.findViewById(R.id.job_edit_notes);
        clientNameInput = view.findViewById(R.id.job_edit_client_name);
        clientPhoneInput = view.findViewById(R.id.job_edit_phone);
        employeeAssignment = view.findViewById(R.id.job_edit_layout_assigned_button);
        employeeAssignment.setOnClickListener(this);

        employeeList = view.findViewById(R.id.job_edit_employee_list);
        employeeAdapter = new EmployeeAdapter(MainActivity.employees,getContext());


        employeeList.setAdapter(employeeAdapter);
        employeeList.setOnItemClickListener(this);

        saveButton = view.findViewById(R.id.job_edit_save_button);
        saveButton.setOnClickListener(this);
        cancelButton = view.findViewById(R.id.job_edit_cancel_button);
        cancelButton.setOnClickListener(this);
        deleteButton = view.findViewById(R.id.job_edit_delete_button);
        deleteButton.setOnClickListener(this);

        jobNameInput.setText(MainActivity.currentJob.getJobTitle());
        jobAddressInput.setText(MainActivity.currentJob.getJobAddress());


        jobTimeInput.setCurrentMinute(MainActivity.currentJob.getJobMin());
        jobTimeInput.setCurrentHour(MainActivity.currentJob.getJobHour());

        jobDateInput.updateDate(
                MainActivity.currentJob.getJobYear(),
                MainActivity.currentJob.getJobMonth(),
                MainActivity.currentJob.getJobDay());

        jobNotesInput.setText(MainActivity.currentJob.getJobNotes());
        clientNameInput.setText(MainActivity.currentJob.getClientName());
        clientPhoneInput.setText(MainActivity.currentJob.getClientPhone());

        employeeAssignment.setText(MainActivity.currentJob.getEmployeeAssigned());

        jobStatus = view.findViewById(R.id.job_edit_status_spinner);
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
        activity.setTitle("Edit Job");
        MainActivity.currentScreen = "Edit Job";

        MainActivity.selectedEmployee = null;

        return view;
    }


    @Override
    public void onClick(View view) {


        if (view.getId() == saveButton.getId()) {
            saveJob();
        }

            if (view.getId() == cancelButton.getId()) {
                Log.d(TAG, "Cancel was clicked");

                getParentFragmentManager().beginTransaction().replace(
                        R.id.fragment_holder,
                        DashboardFragment.newInstance()
                ).commit();
            }

        if (view.getId() == deleteButton.getId()) {
            deleteJob();
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

            nwu = new NetworkUtility(getContext());
            if(!nwu.isConnected()){
                return;
            }

            String jobName = jobNameInput.getText().toString();
            String jobAddress = jobAddressInput.getText().toString();

            int jobHour = jobTimeInput.getCurrentHour();
            int jobMin = jobTimeInput.getCurrentMinute();

            int jobMonth = jobDateInput.getMonth();
            int jobDay = jobDateInput.getDayOfMonth();
            int jobYear = jobDateInput.getYear();

            String jobNotes = jobNotesInput.getText().toString();
            String clientName = clientNameInput.getText().toString();
            String clientPhone = clientPhoneInput.getText().toString();
            String employeeAssigned = employeeAssignment.getText().toString();

            if (!jobName.isEmpty()) {
                    if(!ValidationUtility.validateSize(jobName, 30)){
                        Toast.makeText(getContext(), "Job Name greater than 30 characters.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            else{
                Toast.makeText(getContext(), "Job Name is Empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!jobAddress.isEmpty()) {
                if(!ValidationUtility.validateSize(jobAddress, 60)){
                    Toast.makeText(getContext(), "Job Address greater than 60 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Geocoder coder = new Geocoder(getContext());
                List<Address> address;

                try {
                    address = coder.getFromLocationName(jobAddress,5);
                    if (address==null) {
                        return;
                    }
                    try{
                        Address location = address.get(0);

                    }catch(Exception e){
                        Toast.makeText(getContext(), "Not a valid address.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }



        }else{
                Toast.makeText(getContext(), "Job Address is Empty.", Toast.LENGTH_SHORT).show();
                return;
            }


            if (!jobNotes.isEmpty()) {
                if(!ValidationUtility.validateSize(jobNotes, 240)){
                    Toast.makeText(getContext(), "Job Notes are greater than 240 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }


            }else{
                Toast.makeText(getContext(), "Job Notes are Empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!clientName.isEmpty()) {
                if(!ValidationUtility.validateSize(clientName, 30)){
                    Toast.makeText(getContext(), "Client Name is greater than 240 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }else{
                Toast.makeText(getContext(), "Client Name is Empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!clientPhone.isEmpty()) {
                if(!ValidationUtility.validatePhone(clientPhone)){
                    Toast.makeText(getContext(), "Client Phone is not formatted correctly.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }else{
                Toast.makeText(getContext(), "Client Phone is Empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!employeeAssigned.isEmpty()) {


            }else{
                Toast.makeText(getContext(), "Employee assigned is Empty.", Toast.LENGTH_SHORT).show();
                return;
            }


                Job newJob = new Job(
                        jobName,
                        jobAddress,
                        jobHour,
                        jobMin,
                        jobDay,
                        jobMonth,
                        jobYear,
                        jobNotes,
                        clientName,
                        clientPhone,
                        employeeAssigned,
                        MainActivity.currentJob.getJobStatus(),
                        MainActivity.currentUser.getCompanyCode()
                );



                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference dr = db.collection("jobs").document(MainActivity.currentJob.getJobTitle());
                dr.delete()

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d(TAG, "DocumentSnapshot successfully Deleted!");

                                MainActivity.jobs.remove(MainActivity.currentJob);

                                MainActivity.jobs.add(newJob);
                                MainActivity.currentJob = newJob;

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });


            Map<String, Object> job = new HashMap<>();

                job.put("name", jobName);
                job.put("address", jobAddress);
                job.put("hour", jobHour);
                job.put("min", jobMin);
                job.put("day", jobDay);
                job.put("month", jobMonth);
                job.put("year", jobYear);
                job.put("notes", jobNotes);
                job.put("cName", clientName);
                job.put("cPhone", clientPhone);
                job.put("companycode", MainActivity.currentUser.getCompanyCode());
                job.put("assigned", employeeAssignment.getText().toString());
                job.put("status", MainActivity.currentJob.getJobStatus());


            db.collection("jobs").document(jobName).set(job)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");

                            getParentFragmentManager().beginTransaction().replace(
                                    R.id.fragment_holder,
                                    JobsFragment.newInstance()
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Employee clickedEmployee = MainActivity.employees.get(i);
        MainActivity.selectedEmployee = clickedEmployee;

        if(MainActivity.selectedEmployee != null){
            Log.d(MainActivity.selectedEmployee.getName().toString(), "ASSIGNED");
            employeeAssignment.setText(MainActivity.selectedEmployee.getName());
        }else{
            employeeAssignment.setText("Unassigned");
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void deleteJob(){

        nwu = new NetworkUtility(getContext());
        if(!nwu.isConnected()){
            //todo warn for offline
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference dr = db.collection("jobs").document(MainActivity.currentJob.getJobTitle());
        dr.delete()

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "DocumentSnapshot successfully Deleted!");

                        MainActivity.jobs.remove(MainActivity.currentJob);
                        MainActivity.currentJob = null;

                        getParentFragmentManager().beginTransaction().replace(
                                R.id.fragment_holder,
                                JobsFragment.newInstance()
                        ).commit();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
        MainActivity.selectorJobHour = i;
        MainActivity.selectorJobHour = jobTimeInput.getCurrentHour();
        MainActivity.selectorJobMonth = jobDateInput.getMonth();
        MainActivity.selectorJobDay = jobDateInput.getDayOfMonth();
        MainActivity.selectorJobYear = jobDateInput.getYear();
        employeeList.setAdapter(employeeAdapter);
        employeeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        //Year month day
        MainActivity.selectorJobDay = i2;
        MainActivity.selectorJobMonth = i1;
        MainActivity.selectorJobYear = i;
        MainActivity.selectorJobHour = jobTimeInput.getCurrentHour();
        employeeList.setAdapter(employeeAdapter);
        employeeAdapter.notifyDataSetChanged();
    }
}
