package com.devandroid.tmsearch;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.devandroid.tmsearch.Network.BroadcastReceiverInterface;
import com.devandroid.tmsearch.Network.MyBroadcastReceiver;
import com.devandroid.tmsearch.Util.Utils;

public class ParentActivity extends AppCompatActivity
        implements BroadcastReceiverInterface {

    private Snackbar mSnackbar;
    protected Toast mToast;
    private BroadcastReceiver mBrodcastReceiver;
    protected boolean mConnectionUp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Create broadcast receiver class and register callback
         */
        mBrodcastReceiver = new MyBroadcastReceiver();
        ((MyBroadcastReceiver) mBrodcastReceiver).registerCallback (this, getApplicationContext());
        /**
         * Register action and enable broadcast receiver
         */
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBrodcastReceiver, filter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mSnackbar = Snackbar.make(findViewById(R.id.parent_view), "", Snackbar.LENGTH_INDEFINITE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        mConnectionUp = Utils.isOnline(this);
        manageSnackbar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /**
         * unregister callback from broadcast receiver
         */
        ((MyBroadcastReceiver) mBrodcastReceiver).unregisterCallback();
        /**
         * unregister broadcast receiver
         */
        unregisterReceiver(mBrodcastReceiver);

    }

    @Override
    public void onChangeConnectivity(boolean state) {

        /**
         * update connection state
         */
        mConnectionUp = state;

        /**
         * update snackbar
         */
        manageSnackbar();
    }

    void manageSnackbar() {

        if(mConnectionUp) {
            mSnackbar.dismiss();
        } else {
            mSnackbar.setText(getString(R.string.no_internet_snackbar));
            mSnackbar.show();
        }
    }

    void showToast(String message) {

        mToast.setText(message);
        mToast.show();
    }
}
