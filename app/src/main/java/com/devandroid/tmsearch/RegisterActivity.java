package com.devandroid.tmsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devandroid.tmsearch.Firebase.ErrorCodes;
import com.devandroid.tmsearch.Firebase.FirebaseManager;
import com.devandroid.tmsearch.Firebase.FirebaseCallback;
import com.devandroid.tmsearch.Util.Utils;

public class RegisterActivity extends AppCompatActivity implements FirebaseCallback {

    /**
     * Constants
     */
    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    /**
     * UI components
     */
    private ImageView mIvBack;
    private EditText mEtNameFieldInput;
    private EditText mEtEmailFieldInput;
    private EditText mEtPasswordFieldInput;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mIvBack = findViewById(R.id.ivBack);
        mEtNameFieldInput = findViewById(R.id.etNameFieldInput);
        mEtEmailFieldInput = findViewById(R.id.etEmailFieldInput);
        mEtPasswordFieldInput = findViewById(R.id.etPasswordFieldInput);
        mBtnRegister = findViewById(R.id.btnRegister);

        /**
         * subscribe firebase auth listener
         */
        FirebaseManager.addListener(RegisterActivity.this);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();
                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.CREATE_ACCOUNT_BUTTON);
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
        FirebaseManager.removeListener(RegisterActivity.this);

    }

    @Override
    public void mListenerSignInSuccessful() { }

    @Override
    public void mListenerSignInFail(String reason) { }

    @Override
    public void mListenerRegisterSuccessful() {
        Utils.ProgressDialogStop();
        Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mListenerRegisterFail(String reason) {
        Utils.ProgressDialogStop();
        Toast.makeText(this, "Oops! " + reason, Toast.LENGTH_LONG).show();
    }

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

    private void startRegister() {

        String strName = mEtNameFieldInput.getText().toString();
        String strEmail = mEtEmailFieldInput.getText().toString();
        String strPassword = mEtPasswordFieldInput.getText().toString();
        if(Utils.checkFormNameEmailPassword(strName, mEtNameFieldInput, strEmail, mEtEmailFieldInput, strPassword, mEtPasswordFieldInput))
        {
            Utils.ProgressDialogStart(RegisterActivity.this, ErrorCodes.ProgressBarAnimationCreatingUser);
            FirebaseManager.FirebaseAuthStartRegister(RegisterActivity.this, strName, strEmail, strPassword);
        }
    }
}
