package com.coeus.pdfreader.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.coeus.pdfreader.utilities.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(final Context context, final Intent intent) {
 
        String status = NetworkUtil.getConnectivityStatusString(context);
        Intent sendInternetMessage = new Intent("internetMessage");
        sendInternetMessage.putExtra("internetStatus", status);
        context.sendBroadcast(sendInternetMessage);
//        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}