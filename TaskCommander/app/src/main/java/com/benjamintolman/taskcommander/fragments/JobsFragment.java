package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.adapters.JobsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JobsFragment extends Fragment implements  AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "JobsFragment";

    ListView jobsList;
    boolean filterSpinnerBool;
    Spinner filterSpinner;
    JobsAdapter jobsAdapter;


    ArrayList<Job> jobsFragmentJobs = new ArrayList<>();

    public static JobsFragment newInstance() {

        Bundle args = new Bundle();

        JobsFragment fragment = new JobsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.jobs_layout, container, false);

        setHasOptionsMenu(true);

        for (int i = 0; i < MainActivity.jobs.size(); i++) {
            jobsFragmentJobs.add(MainActivity.jobs.get(i));
        }


            for (int i = 0; i < jobsFragmentJobs.size(); i++) {
                if(!jobsFragmentJobs.get(i).getCompanyCode().equals(MainActivity.currentUser.getCompanyCode())){
                    jobsFragmentJobs.remove(i);
                }
            }

            Log.d("ROLE ", MainActivity.currentUser.getRole());
        //Remove jobs that are not for this employee if this is an employee.
        if(MainActivity.currentUser.getRole().equals("Employee")) {
            for (int i = 0; i < jobsFragmentJobs.size(); i++) {
                if(!jobsFragmentJobs.get(i).getEmployeeAssigned().equals(MainActivity.currentUser.getName())){
                    jobsFragmentJobs.remove(i);
                }
            }
        }

        jobsList = view.findViewById(R.id.jobs_listview);

        jobsAdapter = new JobsAdapter(jobsFragmentJobs,getContext());

        jobsList.setAdapter(jobsAdapter);
        jobsList.setOnItemClickListener(this);


        filterSpinnerBool = false;
        filterSpinner = view.findViewById(R.id.jobs_list_filter_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.jobfilter, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter and on select to the spinner
        filterSpinner.setAdapter(adapter);
        filterSpinner.setSelection(0,false);
        filterSpinner.setOnItemSelectedListener(this);


        Activity activity = getActivity();
        activity.setTitle("Jobs");
        MainActivity.currentScreen = "Jobs";

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.jobs_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String id = item.toString();

        switch(id){
            case "add":
                Log.d(TAG, "Add was clicked");

                getParentFragmentManager().beginTransaction().replace(
                        R.id.fragment_holder,
                        JobCreateFragment.newInstance()
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Job thisJob = jobsFragmentJobs.get(i);

        MainActivity.currentJob = thisJob;

        getParentFragmentManager().beginTransaction().replace(
                R.id.fragment_holder,
                JobDetailsFragment.newInstance()
        ).commit();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String sortBy = filterSpinner.getSelectedItem().toString();

        if(sortBy.equals("Status Ascending")){
            Collections.sort(jobsFragmentJobs, Job.jobStatusCompare);
        }
        if(sortBy.equals("Status Descending")){
            Collections.sort(jobsFragmentJobs, Job.jobStatusCompareD);
        }
        if(sortBy.equals("Date Descending")){
            Collections.sort(jobsFragmentJobs, Job.compareDateDescending);
        }
        if(sortBy.equals("Date Ascending")){
            Collections.sort(jobsFragmentJobs, Job.compareDateAscending);
        }


        jobsList.setAdapter(jobsAdapter);
        jobsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
