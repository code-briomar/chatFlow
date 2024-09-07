package com.briomar.chatflow;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NetworkMonitor {

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    public NetworkMonitor(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();

            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(Network network) {
                    // Internet connection lost
                    Toast.makeText(context, "No internet connection. You won't be able to get suggestions", Toast.LENGTH_SHORT).show();

                    // Disable the upload and uploadAgain buttons
//                    uploadButton.setVisibility(View.INVISIBLE);
//                    uploadAgainButton.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAvailable(Network network) {
                    // Internet connection regained
//                    Toast.makeText(context, "Internet connection available", Toast.LENGTH_LONG).show();
                    // Enable the upload and uploadAgain buttons
//                    uploadButton.setVisibility(View.VISIBLE);
//                    uploadAgainButton.setVisibility(View.VISIBLE);
                }
            };

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    // Call this method to stop monitoring when not needed
    public void unregisterCallback() {
        if (networkCallback != null && connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}
