package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.adapters.EmployeeAdapter;

public class EmployeeChooserFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "EmployeeChooserFragment";

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

        employeeList = view.findViewById(R.id.employee_chooser_listview);

        EmployeeAdapter employeeAdapter = new EmployeeAdapter(MainActivity.employees,getContext());

        employeeList.setAdapter(employeeAdapter);
        employeeList.setOnItemClickListener(this);

        Activity activity = getActivity();
        activity.setTitle("Employee Chooser");

        MainActivity.currentScreen = ("Employee Chooser");

        return view;
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

        Employee clickedEmployee = MainActivity.employees.get(i);
        MainActivity.selectedEmployee = clickedEmployee;
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
