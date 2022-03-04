package com.benjamintolman.taskcommander.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.R;
import com.benjamintolman.taskcommander.Utils.BitmapUtility;
import com.benjamintolman.taskcommander.Utils.ValidationUtility;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "ProfileFragment";

    private static final int CAMERA_REQUEST = 1889;

    EditText emailInput;
    EditText nameInput;
    EditText passwordInput;
    EditText phoneInput;
    Spinner roleSpinner;
    EditText companyCodeInput;
    Button cancelButton;
    Button updateButton;
    Button deleteButton;

    boolean imageUploaded = false;
    ImageView profileImage;
    Bitmap profileImageBitmap;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mStorageRef;


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
        profileImage = view.findViewById(R.id.profile_profileimage);
        profileImage.setOnClickListener(this);

        //Set these blocks to the correct values from current employee
        emailInput.setText(MainActivity.currentUser.getEmail());
        nameInput.setText(MainActivity.currentUser.getName());
        passwordInput.setText(MainActivity.currentUser.getPassword());
        phoneInput.setText(MainActivity.currentUser.getPhone());

        roleSpinner = view.findViewById(R.id.profile_role_spinner);

        //Firestore
        mStorageRef = FirebaseStorage.getInstance().getReference();

        String imageUri = "https://firebasestorage.googleapis.com/v0/b/taskcommander-3f0e3.appspot.com/o/" + MainActivity.currentUser.getEmail() + "profile.jpg?alt=media&token=fa379ac1-e777-4322-b4d1-8b9e11ece91e";


        //Picasso.with(getContext()).load(imageUri).into(profileImage);
        int radius = 100;
        int margin = 5;
        Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(imageUri)
                .transform(transformation).into((profileImage));


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

        if (view.getId() == profileImage.getId()) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

        if (view.getId() == cancelButton.getId()) {
            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_holder,
                    DashboardFragment.newInstance()
            ).commit();
        }

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
            } else {
                Toast.makeText(getContext(), "Phone number is required.", Toast.LENGTH_SHORT).show();
            }

            TextView spinnerView = (TextView) roleSpinner.getSelectedView();
            role = spinnerView.getText().toString();

            //todo validate company code.
            companyCode = companyCodeInput.getText().toString();

            //Send over our new fields to create a user in firebase.
            //Does email = the same user?


            Employee thisEmployee = new Employee(email, name, password, phone, role, companyCode);

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

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });



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

            //IF email matches we take this and put it as current user.
            if(email.equals(MainActivity.currentUser.getEmail())) {
                Log.d("EMAIL MATCHES ", "it does it does!");
                MainActivity.currentUser = thisEmployee;
            }else {

                DocumentReference dr = db.collection("users").document(MainActivity.currentUser.getEmail());
                dr.delete()

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d(TAG, "DocumentSnapshot successfully Deleted!");
                                //After register we go to dashboard

                                MainActivity.currentUser = thisEmployee;

                                getParentFragmentManager().beginTransaction().replace(
                                        R.id.fragment_holder,
                                        DashboardFragment.newInstance()
                                ).commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        }

        if (view.getId() == deleteButton.getId()) {

            email = MainActivity.currentUser.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference dr = db.collection("users").document(email);
            dr.delete()

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.d(TAG, "DocumentSnapshot successfully Deleted!");
                            //After register we go to dashboard
                            getParentFragmentManager().beginTransaction().replace(
                                    R.id.fragment_holder,
                                    SignInFragment.newInstance()
                            ).commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });

        }
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}