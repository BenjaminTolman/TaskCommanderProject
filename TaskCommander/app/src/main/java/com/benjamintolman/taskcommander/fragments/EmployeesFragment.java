package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.adapters.EmployeeAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class EmployeesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "EmployeesFragment";

    ListView employeeList;


    public static EmployeesFragment newInstance() {

        Bundle args = new Bundle();

        EmployeesFragment fragment = new EmployeesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.employees_page_layout, container, false);

        EmployeeAdapter employeeAdapter = new EmployeeAdapter(MainActivity.employees, getContext());

        employeeList = view.findViewById(R.id.employees_page_listview);
        employeeList.setAdapter(employeeAdapter);
        employeeList.setOnItemClickListener(this);


        Activity activity = getActivity();
        activity.setTitle("Employees");
        MainActivity.currentScreen = "Employees";

        MainActivity.selectedEmployee = null;

        return view;
    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Employee selectedEmployee = MainActivity.employees.get(i);
        Log.d("Employee Clicked ", selectedEmployee.getName());

        MainActivity.selectedEmployee = selectedEmployee;

        getParentFragmentManager().beginTransaction().replace(
                R.id.fragment_holder,
                EmployeeDetailsFragment.newInstance()
        ).commit();
    }


}