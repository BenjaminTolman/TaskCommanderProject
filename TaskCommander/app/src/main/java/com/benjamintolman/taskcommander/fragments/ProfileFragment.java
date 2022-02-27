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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.FirestoreUtility;
import com.benjamintolman.taskcommander.Utils.ValidationUtility;

public class ProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "ProfileFragment";

    EditText emailInput;
    EditText nameInput;
    EditText passwordInput;
    EditText phoneInput;
    Spinner roleSpinner;
    EditText companyCodeInput;
    Button cancelButton;
    Button updateButton;
    Button deleteButton;


    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);

        //todo validate all of these in case we update or update.
        emailInput = view.findViewById(R.id.profile_email_edittext);
        nameInput = view.findViewById(R.id.profile_username_edittext);
        passwordInput = view.findViewById(R.id.profile_password_edittext);
        phoneInput = view.findViewById(R.id.profile_phone_edittext);
        roleSpinner = view.findViewById(R.id.profile_role_spinner);
        companyCodeInput = view.findViewById(R.id.profile_company_code_edittext);
        cancelButton = view.findViewById(R.id.profile_back_button);
        cancelButton.setOnClickListener(this);
        updateButton = view.findViewById(R.id.profile_update_button);
        updateButton.setOnClickListener(this);
        deleteButton = view.findViewById(R.id.profile_update_delete_button);
        deleteButton.setOnClickListener(this);

        //Set these blocks to the correct values from current employee
        emailInput.setText(MainActivity.currentUser.getEmail());
        nameInput.setText(MainActivity.currentUser.getName());
        passwordInput.setText(MainActivity.currentUser.getPassword());
        phoneInput.setText(MainActivity.currentUser.getPhone());

        roleSpinner = view.findViewById(R.id.profile_role_spinner);

        //Create and ArrayAdapter using the string array in strings and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.roles, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter and on select to the spinner
        roleSpinner.setAdapter(adapter);

        if(MainActivity.currentUser.getRole().equals("Employee")){
            roleSpinner.setSelection(0,false);
        }else{
            roleSpinner.setSelection(1,false);
        }

        roleSpinner.setOnItemSelectedListener(this);

        companyCodeInput.setText(MainActivity.currentUser.getCompanyCode());

        Activity activity = getActivity();
        activity.setTitle("Profile");
        MainActivity.currentScreen = "Profile";

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

        if (view.getId() == updateButton.getId()) {
            Log.d(TAG, "Update button tapped");

            email = emailInput.getText().toString();

            if (!email.isEmpty()) {
                if (!ValidationUtility.isEmailValid(email)) {
                    Toast.makeText(getContext(), "There was a problem with the email format.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                //email is empty
                Toast.makeText(getContext(), "Email is required.", Toast.LENGTH_SHORT).show();
                return;
            }

            name = nameInput.getText().toString();

            if (!name.isEmpty()) {
                if (!ValidationUtility.validatename(name)) {
                    Toast.makeText(getContext(), "Name must be less than 30 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(getContext(), "Name is required.", Toast.LENGTH_SHORT).show();
                return;
            }

            password = passwordInput.getText().toString();
            if (!password.isEmpty()) {
                if (!ValidationUtility.validatePassword(password)) {
                    Toast.makeText(getContext(), "Password must be 10 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                //name is empty
                Toast.makeText(getContext(), "Password is required.", Toast.LENGTH_SHORT).show();
                return;
            }

            phone = phoneInput.getText().toString();
            if (!password.isEmpty()) {
                if (!ValidationUtility.validatePhone(phone)) {
                    Toast.makeText(getContext(), "There was a problem with the Phone format.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }else
            {
                Toast.makeText(getContext(), "Phone number is required.", Toast.LENGTH_SHORT).show();
            }

            TextView spinnerView = (TextView) roleSpinner.getSelectedView();
            role = spinnerView.getText().toString();

            //todo validate company code.
            companyCode = companyCodeInput.getText().toString();

            //Send over our new fields to create a user in firebase.
            FirestoreUtility.updateUser(email, name, password, phone, role, companyCode);

            //After register we go to dashboard
            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    DashboardFragment.newInstance()
            ).commit();
        }

        if (view.getId() == deleteButton.getId()) {

            email = emailInput.getText().toString();
            FirestoreUtility.deleteUser(email);

            //After register we go to dashboard
            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    SignInFragment.newInstance()
            ).commit();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}