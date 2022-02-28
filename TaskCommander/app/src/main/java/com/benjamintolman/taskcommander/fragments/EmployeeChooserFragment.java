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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.adapters.EmployeeAdapter;
import com.benjamintolman.taskcommander.adapters.JobsAdapter;

public class EmployeeChooserFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "JobsFragment";

    //todo is this a manager? Does it even matter here? If he is, show all jobs, if not show the assigned jobs.

    ListView employeeList;

    public static EmployeeChooserFragment newInstance() {

        Bundle args = new Bundle();

        EmployeeChooserFragment fragment = new EmployeeChooserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.employee_chooser_layout, container, false);

        setHasOptionsMenu(true);

        employeeList = view.findViewById(R.id.employee_chooser_listview);

        //todo remove this for actual employees.
        MainActivity.employees.add(new Employee("this@email","Bill Clay", "password11", "1234567890", "Employee", "companycode"));

        EmployeeAdapter employeeAdapter = new EmployeeAdapter(MainActivity.employees,getContext());

        employeeList.setAdapter(employeeAdapter);
        employeeList.setOnItemClickListener(this);

        Activity activity = getActivity();
        activity.setTitle("Employee Chooser");

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        //inflater = getActivity().getMenuInflater();
        //inflater.inflate(R.menu.jobs_menu, menu);
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
    public void onClick(View view) {



    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //todo instead of name we want the email so we can go ahead and do that.
        Employee clickedEmployee = MainActivity.employees.get(i);
        Log.d("Employee CLICKED ", clickedEmployee.getName());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
