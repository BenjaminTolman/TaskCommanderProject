package com.benjamintolman.taskcommander.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUtility {

    public static final String TAG = "FirestoreUtility";


    public static void uploadDate(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a new user with a first and last name
    Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

    //todo adjust this, add a Firestore authentication

    // Add a new document with a generated ID
        db.collection("users").document("Benjamin@benjamin.com").set(user)

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
