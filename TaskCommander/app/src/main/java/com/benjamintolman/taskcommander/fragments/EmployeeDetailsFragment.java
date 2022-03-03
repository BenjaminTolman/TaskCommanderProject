package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.adapters.EmployeeAdapter;

public class EmployeeDetailsFragment extends Fragment {

    public static final String TAG = "EmployeesFragment";

    public static EmployeeDetailsFragment newInstance() {

        Bundle args = new Bundle();

        EmployeeDetailsFragment fragment = new EmployeeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                //todo replace this with employee info.
        View view = inflater.inflate(R.layout.employee_details_layout, container, false);


        Activity activity = getActivity();
        activity.setTitle("Employees");
        MainActivity.currentScreen = "Employees";

        MainActivity.selectedEmployee = null;

        return view;
    }

}