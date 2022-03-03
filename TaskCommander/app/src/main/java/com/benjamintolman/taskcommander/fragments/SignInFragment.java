package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URL;
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

        //todo can we use picasso go get the profile images?
        // or get them via https?

        Activity activity = getActivity();
        activity.setTitle(R.string.sign_in);
        MainActivity.currentScreen = "SignIn";

        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == signInButton.getId()) {

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
                                                    document.get("companycode").toString()
                                            );

                                            logInSuccess = true;

                                            MainActivity.currentUser = thisEmployee;
                                            getJobs();

                                        } else {

                                        }
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

            //todo get employees too if company code is this company.
            //todo switch to having a company collection for beta.
        }

        if (view.getId() == registerButton.getId()) {
            Log.d(TAG, "Register");

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
                                if (companyCode.equals("companycode")) {

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
                                            document.get("assigned").toString()
                                    );

                                    MainActivity.jobs.add(newJob);

                                } else {
                                    Toast.makeText(getContext(), "There was a problem with log in", Toast.LENGTH_SHORT).show();
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

    private void getProfileImage() {

        //StorageReference storageRef = storage.getReference();

// Create a reference to a file from a Cloud Storage URI
        //StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/profile.jpg");

        //todo figure this out.
        //gsReference.getDownloadUrl();



    }
}

