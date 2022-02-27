package com.benjamintolman.taskcommander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.fragments.DashboardFragment;
import com.benjamintolman.taskcommander.fragments.SignInFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main Activity";
    //list of Jobs
    public static ArrayList<Job> jobs = new ArrayList<Job>();
    public static ArrayList<Employee> employees = new ArrayList<Employee>();

    public static Employee currentUser;

    public static String currentScreen;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

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
    }
}