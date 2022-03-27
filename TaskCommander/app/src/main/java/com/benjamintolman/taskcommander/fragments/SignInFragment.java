package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.LocationUtility;
import com.benjamintolman.taskcommander.Utils.NetworkUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SignInFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "SignInFragment";

    EditText emailInput;
    EditText passwordInput;
    Button signInButton;
    TextView registerButton;

    ImageView logo;

    boolean logInSuccess = false;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private StorageReference mStorageRef;

    private LocationUtility lu;
    private NetworkUtility nwu;

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout, container, false);

        emailInput = view.findViewById(R.id.login_email_edittext);
        passwordInput = view.findViewById(R.id.login_password_edittext);
        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        registerButton = view.findViewById(R.id.register_link);
        registerButton.setOnClickListener(this);

        logo = view.findViewById(R.id.registration_profileimage);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Activity activity = getActivity();
        activity.setTitle(R.string.sign_in);
        MainActivity.currentScreen = "SignIn";

        try {
            trimCache(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else {
            return false;
        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == signInButton.getId()) {

            nwu = new NetworkUtility(getContext());
            if(!nwu.isConnected()){

                return;
            }
            lu = new LocationUtility(getContext(), getActivity());
            lu.getLocationPermission();
            if(!lu.getLocationPermission()){
                Toast.makeText(getContext(), "Location is required", Toast.LENGTH_SHORT).show();
                return;
            }



            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    if (document.getId().equals(emailInput.getText().toString().toLowerCase(Locale.ROOT))) {
                                        String passString = document.get("password").toString();
                                        if (passString.equals(passwordInput.getText().toString())) {

                                            //This becomes the user in MainActivity.
                                            Employee thisEmployee = new Employee(
                                                    document.get("email").toString(),
                                                    document.get("name").toString(),
                                                    document.get("password").toString(),
                                                    document.get("phone").toString(),
                                                    document.get("role").toString(),
                                                    document.get("companycode").toString(),
                                                    document.get("imageurl").toString(),
                                                    Double.parseDouble(document.get("lat").toString()),
                                                    Double.parseDouble(document.get("lon").toString())
                                            );

                                            logInSuccess = true;

                                            MainActivity.currentUser = thisEmployee;
                                            getJobs();
                                            getUsers();

                                            //todo hook this up to firebase.
                                            lu.getUserLocationData(getContext());


                                        } else {
                                            Toast.makeText(getContext(), "Password or Email was incorrect.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

        if (view.getId() == registerButton.getId()) {
            Log.d(TAG, "Register");

            nwu = new NetworkUtility(getContext());
            if(!nwu.isConnected()){

                return;
            }
            lu = new LocationUtility(getContext(), getActivity());
            lu.getLocationPermission();
            if(!lu.getLocationPermission()){
                Toast.makeText(getContext(), "Location is required", Toast.LENGTH_SHORT).show();
                return;
            }

            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    RegisterFragment.newInstance()
            ).commit();
        }
    }

    public void getJobs(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        MainActivity.jobs.clear();

        db.collection("jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String companyCode = document.get("companycode").toString();
                                if (companyCode.equals(MainActivity.currentUser.getCompanyCode())) {



                                    Job newJob = new Job(
                                            document.get("name").toString(),
                                            document.get("address").toString(),
                                            Integer.valueOf(document.get("hour").toString()),
                                            Integer.valueOf(document.get("min").toString()),
                                            Integer.valueOf(document.get("day").toString()),
                                            Integer.valueOf(document.get("month").toString()),
                                            Integer.valueOf(document.get("year").toString()),
                                            document.get("notes").toString(),
                                            document.get("cName").toString(),
                                            document.get("cPhone").toString(),
                                            document.get("assigned").toString(),
                                            document.get("status").toString(),
                                            document.get("companycode").toString()
                                    );

                                    if(document.get("jobimageurls") != null){
                                        ArrayList<String> imageURLs = (ArrayList<String>) document.get("jobimageurls");
                                        newJob.jobImageURLs = imageURLs;
                                    }
                                    MainActivity.jobs.add(newJob);

                                } else {

                                }
                            }
                        }
                        else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                        getParentFragmentManager().beginTransaction().replace(
                                R.id.fragment_holder,
                                DashboardFragment.newInstance()
                        ).commit();
                    }

                });
    }


    public void getUsers(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        MainActivity.employees.clear();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String companyCode = document.get("companycode").toString();
                                if (companyCode.equals(MainActivity.currentUser.getCompanyCode())) {

                                    Log.d("USER FOUND ", companyCode);
                                    Employee newEmployee = new Employee(
                                            document.get("email").toString(),
                                            document.get("name").toString(),
                                            document.get("password").toString(),
                                            document.get("phone").toString(),
                                            document.get("role").toString(),
                                            document.get("companycode").toString(),
                                            document.get("imageurl").toString(),
                                            Double.parseDouble(document.get("lat").toString()),
                                            Double.parseDouble(document.get("lon").toString())
                                    );

                                    MainActivity.employees.add(newEmployee);

                                } else {
                                    
                                }
                            }
                        }
                        else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }
}

