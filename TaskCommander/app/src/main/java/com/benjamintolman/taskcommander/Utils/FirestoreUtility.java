package com.benjamintolman.taskcommander.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUtility {

    public static final String TAG = "FirestoreUtility";


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

        //todo change this because it's not going to work out.
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

    public static void signIn(String sEmail, String sPass){
        //todo create new employee and stick it in the var

        FirebaseFirestore db = FirebaseFirestore.getInstance();



        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //todo this gets ben and if its this email then we will log in
                                if(document.getId().equals(sEmail) ){
                                    Log.d("KENWEW", "WEFWF");
                                    String passString =  document.get("password").toString();
                                    if(passString.equals(sPass)){
                                        Log.d("KENWEW", "CORRENC+");
                                        //todo this is the current employee so make it so in the employee var
                                        Employee thisEmployee = new Employee(
                                                document.get("email").toString(),
                                                document.get("name").toString(),
                                                document.get("password").toString(),
                                                document.get("phone").toString(),
                                                document.get("role").toString(),
                                                document.get("companycode").toString()
                                        );
                                        MainActivity.currentUser = thisEmployee;
                                        Log.d("Current Employee", MainActivity.currentUser.getName());
                                    }
                                    else{
                                        //todo give a reason why it's not correct (email or password incorrect)
                                    }

                                    //todo if the name and pass are not in here then gracefully return.
                                    //todo empty the list and check for null on result?
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
