package com.benjamintolman.taskcommander.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtility {

    private Context mContext;

    public NetworkUtility(Context mContext) {

        this.mContext = mContext;
    }

    public boolean isConnected() {
        //Get connectivity manager to test connectivity first.
        ConnectivityManager mgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mgr != null) //Could be any connection so test network inside here.
        {
            //Check for wifi and the like.
            NetworkInfo info = mgr.getActiveNetworkInfo();

            if (info != null) {

                boolean isConnected = info.isConnected();

                if (isConnected) {
                    return true;
                }
            }

            //If not connected then give a warning box and also build list from data.
            else {


            }
        }
        Toast.makeText(mContext, "Not connected to network.", Toast.LENGTH_SHORT).show();
        return false;
    }
}
