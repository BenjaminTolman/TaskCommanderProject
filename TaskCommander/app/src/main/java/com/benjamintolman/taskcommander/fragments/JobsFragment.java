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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class JobsFragment extends Fragment implements  AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "JobsFragment";

    //todo is this a manager? If this is, show all jobs, if not show the assigned jobs.

    ListView jobsList;
    Spinner filterSpinner;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //MainActivity.jobs.clear();

//        db.collection("jobs")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//
//                                String companyCode = document.get("companycode").toString();
//                                if (companyCode.equals("companycode")) {
//
//                                    Job newJob = new Job(
//                                            document.get("name").toString(),
//                                            document.get("address").toString(),
//                                            Integer.valueOf(document.get("hour").toString()),
//                                            Integer.valueOf(document.get("min").toString()),
//                                            Integer.valueOf(document.get("day").toString()),
//                                            Integer.valueOf(document.get("month").toString()),
//                                            Integer.valueOf(document.get("year").toString()),
//                                            document.get("notes").toString(),
//                                            document.get("cName").toString(),
//                                            document.get("cPhone").toString(),
//                                            document.get("assigned").toString(),
//                                            document.get("status").toString(),
//                                            document.get("companycode").toString()
//                                    );
//
//                                    MainActivity.jobs.add(newJob);
//
//                                } else {
//                                    Toast.makeText(getContext(), "There was a problem with log in", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                        else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//
//                });
//
//
//            for (int i = 0; i < MainActivity.jobs.size(); i++) {
//                if(MainActivity.jobs.get(i).getCompanyCode().equals(MainActivity.currentUser.getCompanyCode())){
//                    MainActivity.jobs.remove(i);
//                }
//            }

        //Remove jobs that are not for this employee if this is an employee.
        if(MainActivity.currentUser.getRole().equals("Employee")) {
            for (int i = 0; i < MainActivity.jobs.size(); i++) {
                if(!MainActivity.jobs.get(i).getEmployeeAssigned().equals(MainActivity.currentUser.getName())){
                    MainActivity.jobs.remove(i);
                }
            }
        }

        jobsList = view.findViewById(R.id.jobs_listview);

        JobsAdapter jobsAdapter = new JobsAdapter(MainActivity.jobs,getContext());

        jobsList.setAdapter(jobsAdapter);
        jobsList.setOnItemClickListener(this);

        filterSpinner = view.findViewById(R.id.jobs_list_filter_spinner);

        //Create and ArrayAdapter using the string array in strings and a default spinner layout.
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

        Job thisJob = MainActivity.jobs.get(i);

        MainActivity.currentJob = thisJob;

        getParentFragmentManager().beginTransaction().replace(
                R.id.fragment_holder,
                JobDetailsFragment.newInstance()
        ).commit();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}