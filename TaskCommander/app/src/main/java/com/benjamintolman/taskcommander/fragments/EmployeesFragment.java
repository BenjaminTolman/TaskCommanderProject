package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.R;

public class EmployeesFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "EmployeesFragment";

    //todo is this a manager? Does it even matter here? Should be a manager if they get to this page.


    public static EmployeesFragment newInstance() {

        Bundle args = new Bundle();

        EmployeesFragment fragment = new EmployeesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //todo this should be the EMPLOYEES layout
        View view = inflater.inflate(R.layout.register_layout, container, false);

        //todo should be a check to see if this is a manager as soon as possible and keep the user profile in main activity.
        Activity activity = getActivity();
        activity.setTitle("Employees");

        return view;
    }

    @Override
    public void onClick(View view) {

        //todo this should be a listview item click

    }

}