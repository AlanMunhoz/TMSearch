package com.devandroid.tmsearch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devandroid.tmsearch.Firebase.FirebaseManager;
import com.devandroid.tmsearch.Firebase.FirebaseCallback;
import com.devandroid.tmsearch.Util.Utils;

import java.lang.ref.WeakReference;


public class LoginActivity extends ParentActivity
        implements FirebaseCallback {

    /**
     * Constants
     */
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    /**
     * UI components
     */
    private EditText mEtEmailFieldInput;
    private EditText mEtPasswordFieldInput;
    private Button mBtnLogin;
    private TextView mTvRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtEmailFieldInput = findViewById(R.id.etEmailFieldInput);
        mEtPasswordFieldInput = findViewById(R.id.etPasswordFieldInput);
        mBtnLogin = findViewById(R.id.btnLogin);
        mTvRegister = findViewById(R.id.tvRegister);

        /**
         * Initialize the Firebase objects
         */
        FirebaseManager.FirebaseManagerInit(this);

        /**
         * subscribe firebase auth listener
         */
        FirebaseManager.addListener(LoginActivity.this);

        if(FirebaseManager.FirebaseAuthIsLoggedIn()) {

            FirebaseManager.FirebaseDatabaseRestoreApiKey();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        mBtnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startSignIn();
                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.LOGIN_BUTTON);
            }
        });

        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        Utils.clearAllFocus(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /**
         * unsubscribe firebase auth listener
         */
        FirebaseManager.removeListener(LoginActivity.this);

    }

    @Override
    public void mListenerSignInSuccessful() {
        Utils.ProgressDialogStop();
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void mListenerSignInFail(String reason) {
        Utils.ProgressDialogStop();
        Toast.makeText(this, "Oops! " + reason, Toast.LENGTH_LONG).show();
    }

    @Override
    public void mListenerRegisterSuccessful() { }

    @Override
    public void mListenerRegisterFail(String reason) { }

    @Override
    public void mListenerChangeCredentialsSuccessful() {}

    @Override
    public void mListenerChangeCredentialsFail(String reason) {}

    @Override
    public void mListenerDatabaseSetApiKeySuccessful() {}

    @Override
    public void mListenerDatabaseSetApiKeyFail(String reason) {}

    @Override
    public void mListenerDatabaseGetApiKey(String key) {}


    private void startSignIn() {

        if(!mConnectionUp) {

            showToast(getString(R.string.no_internet_toast));
            return;
        }

        String strEmail = mEtEmailFieldInput.getText().toString();
        String strPassword = mEtPasswordFieldInput.getText().toString();
        if(Utils.checkFormEmailPassword(strEmail, mEtEmailFieldInput, strPassword, mEtPasswordFieldInput)) {
            Utils.ProgressDialogStart(new WeakReference<Activity>(this),  getString(R.string.ProgressBarAnimationSignInUser));
            FirebaseManager.FirebaseAuthStartSignIn(LoginActivity.this, strEmail, strPassword);
        }
    }

}
