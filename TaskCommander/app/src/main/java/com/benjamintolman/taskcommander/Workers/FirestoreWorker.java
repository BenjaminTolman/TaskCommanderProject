package com.benjamintolman.taskcommander.Workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.benjamintolman.taskcommander.MainActivity;
import com.benjamintolman.taskcommander.Objects.Employee;
import com.benjamintolman.taskcommander.Utils.FirestoreUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FirestoreWorker extends Worker {

    public FirestoreWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {

        //double lat = getInputData().getDouble("CLAT", 0);
        //double lon = getInputData().getDouble("CLON", 0);

        //String returnedData = NetworkUtility.getInstance(getApplicationContext()).getNetworkData("https://api.openweathermap.org/data/2.5/weather?lat="+ lat + "&lon=" + lon + "&appid=d048af63449a2f62fd513991d2e09a3f");

        try{

            //FirestoreUtility.signIn("ben@gmail.com", "1234567890");

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("WORKER", document.getId() + " => " + document.getData());
                                    //todo this gets ben and if its this email then we will log in
                                    if(document.getId().equals("ben@gmail.com") ){
                                        String passString =  document.get("password").toString();
                                        if(passString.equals("1234567890")){

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



                                            //todo maybe main activity static method?
                                        }
                                        else{

                                        }

                                        //todo if the name and pass are not in here then gracefully return.
                                        //todo empty the list and check for null on result?
                                    }
                                }
                            }
                        }

                    });
            Data outputData = new Data.Builder()
                    .putBoolean("SUCCESS", true)

                    .build();

            return Result.success(outputData);

        }

        catch(Exception e)
        {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
