package com.devandroid.tmsearch.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.devandroid.tmsearch.R;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private BroadcastReceiverInterface mBroadcastReceiverInterface;
    private Context mContext;

    public void registerCallback(BroadcastReceiverInterface bcReceiver, Context context) {

        mBroadcastReceiverInterface = bcReceiver;
        mContext = context;
    }

    public void unregisterCallback() {

        mBroadcastReceiverInterface = null;
        mContext = null;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        StringBuilder sb = new StringBuilder();
        sb.append(intent.getAction()).append("\n").append(intent.toUri(Intent.URI_INTENT_SCHEME)).append("\n");

        String log = sb.toString();

        if(mContext!=null && mBroadcastReceiverInterface!=null){
            if (log.contains(mContext.getString(R.string.bc_receiver_no_connectivity))) {
                mBroadcastReceiverInterface.onChangeConnectivity(false);
            } else {
                mBroadcastReceiverInterface.onChangeConnectivity(true);
            }
        }
    }
}

