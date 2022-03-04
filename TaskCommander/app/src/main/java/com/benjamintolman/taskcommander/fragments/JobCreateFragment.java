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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.ValidationUtility;
import com.benjamintolman.taskcommander.adapters.EmployeeAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JobCreateFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemClickListener {

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

    ListView employeeList;

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

        //todo get info from selectors.
        jobNameInput = view.findViewById(R.id.job_creation_name);
        jobAddressInput = view.findViewById(R.id.job_creation_address);
        jobTimeInput = view.findViewById(R.id.job_creation_timepicker);
        jobDateInput = view.findViewById(R.id.job_creation_date);
        jobNotesInput = view.findViewById(R.id.job_creation_notes);
        clientNameInput = view.findViewById(R.id.job_creation_client_name);
        clientPhoneInput = view.findViewById(R.id.job_creation_phone);
        employeeAssignment = view.findViewById(R.id.job_create_layout_assigned_button);
        employeeAssignment.setOnClickListener(this);

        employeeList = view.findViewById(R.id.job_creation_employee_list);
        MainActivity.employees.add(new Employee("this@email","Bill Clay", "password11", "1234567890", "Employee", "companycode"));
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(MainActivity.employees,getContext());

        employeeList.setAdapter(employeeAdapter);
        employeeList.setOnItemClickListener(this);

        saveButton = view.findViewById(R.id.job_creation_save_button);
        saveButton.setOnClickListener(this);
        cancelButton = view.findViewById(R.id.job_creation_cancel_button);
        cancelButton.setOnClickListener(this);

        Activity activity = getActivity();
        activity.setTitle("Create Job");
        MainActivity.currentScreen = "Create Job";

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
            //todo validate these
            String jobName = jobNameInput.getText().toString();
            String jobAddress = jobAddressInput.getText().toString();

            int jobHour = jobTimeInput.getCurrentHour();
            int jobMin = jobTimeInput.getCurrentMinute();

            int jobMonth = jobDateInput.getMonth();
            int jobDay = jobDateInput.getDayOfMonth();
            int jobYear = jobDateInput.getYear();

            Log.d("TIME", String.valueOf(jobHour) + " " + String.valueOf(jobMin));

            String jobDate = (String.valueOf(jobDay) + "/" + String.valueOf(jobMonth) + "/" + String.valueOf(jobYear));

            Log.d("DATE", jobDate);

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

                //todo get the employee assigned from the actual fragment instead of Bill Clay.
            }else{
                Toast.makeText(getContext(), "Employee assigned is Empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            //todo employee assigned needs name and email.
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first and last name

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
            job.put("assigned", employeeAssigned); //todo is MA.selectedUser going to work here?
            job.put("status", "Posted");
            //this should be an ID not whatever we have

            //db.collection(companyCode).document("users").collection("list").document(email).set(user)
            db.collection("jobs").document(jobName).set(job)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");

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
                                    "Posted"
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
}
