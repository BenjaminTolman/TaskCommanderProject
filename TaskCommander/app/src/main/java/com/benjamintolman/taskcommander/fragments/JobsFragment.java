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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.adapters.JobsAdapter;

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
        Intent intent;

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

        //todo this needs to open job details with
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}