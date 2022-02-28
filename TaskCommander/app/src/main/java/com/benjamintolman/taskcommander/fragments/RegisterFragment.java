package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.ValidationUtility;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "RegisterFragment";

    EditText emailInput;
    EditText nameInput;
    EditText passwordInput;
    EditText phoneInput;
    Spinner roleSpinner;
    EditText companyCodeInput;
    Button registerButton;


    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_layout, container, false);

        emailInput = view.findViewById(R.id.register_email_edittext);
        nameInput = view.findViewById(R.id.register_username_edittext);
        passwordInput = view.findViewById(R.id.register_password_edittext);
        phoneInput = view.findViewById(R.id.register_phone_edittext);
        roleSpinner = view.findViewById(R.id.register_role_spinner);
        companyCodeInput = view.findViewById(R.id.register_company_code_edittext);
        registerButton = view.findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(this);

        roleSpinner = view.findViewById(R.id.register_role_spinner);

        //Create and ArrayAdapter using the string array in strings and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.roles, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter and on select to the spinner
        roleSpinner.setAdapter(adapter);
        roleSpinner.setSelection(0,false);
        roleSpinner.setOnItemSelectedListener(this);

        Activity activity = getActivity();
        activity.setTitle(R.string.register);
        MainActivity.currentScreen = "Register";

        return view;
    }

    @Override
    public void onClick(View view) {

        String email = "";
        String name = "";
        String password = "";
        String phone = "";
        String role = "";
        String companyCode = "";

        if(view.getId() == registerButton.getId()){
            Log.d(TAG, "Register button tapped");

            //todo validate all fields before sending to firebaseUtil

            email = emailInput.getText().toString();

            if(!email.isEmpty()){
                if(!ValidationUtility.isEmailValid(email)){
                    //todo why
                    Log.d("FALSE ", email);
                    return;
                }
            }else{
                //email is empty
                Log.d("FALSE ", "email is empty");
                return;
            }


            name = nameInput.getText().toString();

            if(!name.isEmpty()){
               if(!ValidationUtility.validatename(name)){
                    //todo why
                    Log.d("FALSE ", name);
                    return;
                }
            }else{
                    //name is empty
                    return;
                }
            }

            password = passwordInput.getText().toString();
        if(!password.isEmpty()){
            if(!ValidationUtility.validatePassword(password)){
                //todo why
                Log.d("FALSE ", password);
                return;
            }
        }else{
            //name is empty
            Log.d("FALSE ", "password empty");
            return;
        }


            phone = phoneInput.getText().toString();
            if(!ValidationUtility.validatePhone(phone)){
               //todo why
                Log.d("FALSE ", phone);
                return;
            }

            TextView spinnerView = (TextView)roleSpinner.getSelectedView();
            role = spinnerView.getText().toString();

            companyCode = companyCodeInput.getText().toString();


            //set email to all lowercase
            email = email.toLowerCase(Locale.ROOT);

            //Send over our new fields to create a user in firebase.
            Employee thisEmployee = new Employee(email,name,password,phone,role,companyCode);
             MainActivity.currentUser = thisEmployee;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        user.put("password", password);
        user.put("phone", phone);
        user.put("role", role);
        user.put("companycode", companyCode);

        db.collection("users").document(email).set(user)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        //After register we go to dashboard
                        getParentFragmentManager().beginTransaction().replace(
                                R.id.fragment_holder,
                                DashboardFragment.newInstance()
                        ).commit();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        }

        //todo put a cancel button

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



}
