package com.benjamintolman.taskcommander;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.fragments.DashboardFragment;
import com.benjamintolman.taskcommander.fragments.JobsFragment;
import com.benjamintolman.taskcommander.fragments.SignInFragment;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main Activity";
    //list of Jobs
    public static ArrayList<Job> jobs = new ArrayList<Job>();
    public static ArrayList<Employee> employees = new ArrayList<Employee>();

    public static Employee currentUser;
    public static Employee selectedEmployee;

    public static String currentScreen;

    public static Job currentJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MainActivity.currentScreen = "SignIn";
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_holder,
                SignInFragment.newInstance()
        ).commit();
    }

    @Override
    public void onBackPressed() {

        if(currentScreen.equals("Register")){
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    SignInFragment.newInstance()
            ).commit();
        }
        if(currentScreen.equals("Profile")){
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    DashboardFragment.newInstance()
            ).commit();
        }
        if(currentScreen.equals("SignIn")){
            //Ignore
        }
        if(currentScreen.equals("Jobs")){
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    DashboardFragment.newInstance()
            ).commit();
        }
        if(currentScreen.equals("Job Details")){
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    JobsFragment.newInstance()
            ).commit();
        }
        if(currentScreen.equals("Create Job")){
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    JobsFragment.newInstance()
            ).commit();
        }
    }
}