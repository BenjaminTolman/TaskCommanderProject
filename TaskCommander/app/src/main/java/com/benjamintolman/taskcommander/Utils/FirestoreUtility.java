package com.benjamintolman.taskcommander.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUtility {

    public static final String TAG = "FirestoreUtility";


    public static void deleteUser(String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference dr = db.collection("users").document(email);
        dr.delete()

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "DocumentSnapshot successfully Deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public static void createUser(String email, String name, String password, String phone, String role, String companyCode){

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

        }
    })
            .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.w(TAG, "Error writing document", e);
        }
    });
    }

    public static void updateUser(String email, String name, String password, String phone, String role, String companyCode){

        //Does email = the same user?
        if(email.equals(MainActivity.currentUser.getEmail())){
            Log.d("EMAIL MATCHES ", "it does it does!");
        }
        //todo get current user email, if this email is different then create user and delete
        //todo the current one, if this is the same then update the user.

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

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }



    //todo jobs need a company code
    public static void createJob(String name, String address, String time, String date,String jobNotes, String cName, String cPhone, String assigned){

        //todo this should be getting a company ID and an Employee ID from the selected employee and the current manager.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name

        Map<String, Object> job = new HashMap<>();
        job.put("name", name);
        job.put("address", address);
        job.put("time", time);
        job.put("date", date);
        job.put("cName", cName);
        job.put("cPhone", cPhone);
        job.put("assigned", assigned); //this should be an ID not whatever we have

        //todo adjust this, better way to track job


        //db.collection(companyCode).document("users").collection("list").document(email).set(user)
        db.collection("jobs").document(name).set(job)

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
    }
}
