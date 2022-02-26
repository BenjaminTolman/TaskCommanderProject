package com.benjamintolman.taskcommander.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.FirestoreUtility;

public class SignInFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "SignInFragment";
    public static Employee currentEmployee;

    EditText emailInput;
    EditText passwordIntput;
    Button signInButton;
    TextView registerButton;

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
        passwordIntput = view.findViewById(R.id.login_password_edittext);
        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        registerButton = view.findViewById(R.id.register_link);
        registerButton.setOnClickListener(this);

        Activity activity = getActivity();
        activity.setTitle(R.string.sign_in);

        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == signInButton.getId()){
            Log.d(TAG, "Sign in button tapped");

            //Todo these need to be validated
            FirestoreUtility.signIn(emailInput.getText().toString(), passwordIntput.getText().toString());

            //todo actually check credentials
            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    DashboardFragment.newInstance()
            ).commit();
        }

        if(view.getId() == registerButton.getId()){
            Log.d(TAG, "Register");

            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    RegisterFragment.newInstance()
            ).commit();
        }
    }
}
