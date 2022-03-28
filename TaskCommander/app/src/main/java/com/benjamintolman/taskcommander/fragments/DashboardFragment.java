package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.R;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "DashboardFragment";


    Button jobsButton;
    Button employeesButton;
    Button profileButton;
    Button signOutButton;


    public static DashboardFragment newInstance() {

        Bundle args = new Bundle();

        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_layout, container, false);

        jobsButton = view.findViewById(R.id.dashboard_jobs_button);
        jobsButton.setOnClickListener(this);
        employeesButton = view.findViewById(R.id.dashboard_employees_button);
        employeesButton.setOnClickListener(this);
        profileButton = view.findViewById(R.id.dashboard_profile_button);
        profileButton.setOnClickListener(this);
        signOutButton = view.findViewById(R.id.dashboard_signout_button);
        signOutButton.setOnClickListener(this);

        if(MainActivity.currentUser.getRole().equals("Employee")){
            employeesButton.setVisibility(View.GONE);
        }

        for (int i = 0; i < MainActivity.jobs.size(); i++) {
            if(MainActivity.currentUser.isHasUpdates()){
                jobsButton.setText("Jobs - Updates");
                jobsButton.setBackgroundColor(getResources().getColor(R.color.forestgreen));
                MainActivity.currentUser.setHasUpdates(false);
            }
        }

        Activity activity = getActivity();
        activity.setTitle("Dashboard");

        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == jobsButton.getId()){

            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    JobsFragment.newInstance()
            ).commit();
        }
        if(view.getId() == employeesButton.getId()){

            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    EmployeesFragment.newInstance()
            ).commit();

        }
        if(view.getId() == profileButton.getId()){

            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    ProfileFragment.newInstance()
            ).commit();
        }

        if(view.getId() == signOutButton.getId()){

            MainActivity.currentJob = null;
            MainActivity.currentUser = null;
            MainActivity.currentScreen = "Sign In";
            MainActivity.employees.clear();
            MainActivity.jobs.clear();

            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    SignInFragment.newInstance()
            ).commit();
        }
    }
}