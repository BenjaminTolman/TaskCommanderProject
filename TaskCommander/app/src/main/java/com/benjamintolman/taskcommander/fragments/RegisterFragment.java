package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Objects.Job;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.BitmapUtility;
import com.benjamintolman.taskcommander.Utils.ValidationUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "RegisterFragment";

    private static final int CAMERA_REQUEST = 1888;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    ImageView profileImage;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    Bitmap profileImageBitmap;
    boolean imageUploaded = false;

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

        profileImage = view.findViewById(R.id.registration_profileimage);
        profileImage.setOnClickListener(this);

        profileImageBitmap = null;

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

        if(view.getId() == profileImage.getId()){
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

        if(view.getId() == registerButton.getId()){
            Log.d(TAG, "Register button tapped");

            email = emailInput.getText().toString();

            if(!email.isEmpty()){
                if(!ValidationUtility.isEmailValid(email)){
                    Toast.makeText(getContext(), "Email was not formatted correctly.", Toast.LENGTH_SHORT).show();
                    Log.d("FALSE ", email);
                    return;
                }
            }else{
                Toast.makeText(getContext(), "Email field was empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            name = nameInput.getText().toString();

            if(!name.isEmpty()){
               if(!ValidationUtility.validatename(name)){
                   Toast.makeText(getContext(), "Name is more than 30 characters.", Toast.LENGTH_SHORT).show();
                    Log.d("FALSE ", name);
                    return;
                }
            }else{
                Toast.makeText(getContext(), "Name field was empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            password = passwordInput.getText().toString();
        if(!password.isEmpty()){
            if(!ValidationUtility.validatePassword(password)){
                Toast.makeText(getContext(), "Password was not 10 characters.", Toast.LENGTH_SHORT).show();
                Log.d("FALSE ", password);
                return;
            }
        }else{
            Toast.makeText(getContext(), "Password field was empty.", Toast.LENGTH_SHORT).show();
            Log.d("FALSE ", "password empty");
            return;
        }

            phone = phoneInput.getText().toString();
            if(!ValidationUtility.validatePhone(phone)){
                Toast.makeText(getContext(), "Phone number was not formatted correctly.", Toast.LENGTH_SHORT).show();
                Log.d("FALSE ", phone);
                return;
            }

            TextView spinnerView = (TextView)roleSpinner.getSelectedView();
            role = spinnerView.getText().toString();

            companyCode = companyCodeInput.getText().toString();

        if (!companyCode.isEmpty()) {
            if (!ValidationUtility.validateCompanyCode(companyCode)) {
                Toast.makeText(getContext(), "Company code was too long.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(getContext(), "Company Code is required.", Toast.LENGTH_SHORT).show();
            return;
        }

            //set email to all lowercase
            email = email.toLowerCase(Locale.ROOT);

            //Send over our new fields to create a user in firebase.
            Employee thisEmployee = new Employee(email,name,password,phone,role,companyCode);
             MainActivity.currentUser = thisEmployee;

             if(imageUploaded){

                 // Create a Cloud Storage reference from the app
                 StorageReference storageRef = storage.getReference();

                 // Create a reference to "mountains.jpg"
                 StorageReference profileRef = storageRef.child(email + "profile.jpg");

                 // Create a reference to 'images/mountains.jpg'
                 StorageReference profileImagesRef = storageRef.child("images/" + email + "profile.jpg");

                 // While the file names are the same, the references point to different files
                 profileRef.getName().equals(profileImagesRef.getName());    // true
                 profileRef.getPath().equals(profileImagesRef.getPath());    // false

                 // Get the data from an ImageView as bytes
                 profileImage.setDrawingCacheEnabled(true);
                 profileImage.buildDrawingCache();
                 Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                 byte[] data = baos.toByteArray();

                 UploadTask uploadTask = profileRef.putBytes(data);
                 uploadTask.addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception exception) {
                         // Handle unsuccessful uploads
                     }
                 }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         imageUploaded = true;
                         Bitmap circleBitmap = BitmapUtility.getCircularBitmap(profileImageBitmap);
                         profileImage.setImageBitmap(circleBitmap);
                     }
                 });
             }else{
                 Toast.makeText(getContext(), "You must have a profile image.", Toast.LENGTH_SHORT).show();
                 return;
             }


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

                        getJobs();

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

                                    MainActivity.jobs.add(newJob);

                                } else {
                                    Toast.makeText(getContext(), "There was a problem with log in", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) imageData.getExtras().get("data");

            float aspectRatio = photo.getWidth() /
                    (float) photo.getHeight();
            int width = 200;
            int height = Math.round(width / aspectRatio);

            photo = Bitmap.createScaledBitmap(
                    photo, width, height, false);

            photo = BitmapUtility.getCircularBitmap(photo);

            profileImageBitmap = photo;
            profileImage.setImageBitmap(photo);
            imageUploaded = true;
        }
    }
}
