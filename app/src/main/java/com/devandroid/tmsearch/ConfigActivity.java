package com.devandroid.tmsearch;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devandroid.tmsearch.Firebase.FirebaseManager;
import com.devandroid.tmsearch.Firebase.FirebaseCallback;
import com.devandroid.tmsearch.Network.Network;
import com.devandroid.tmsearch.Util.Utils;

import java.lang.ref.WeakReference;

public class ConfigActivity extends ParentActivity
        implements FirebaseCallback {

    /**
     * Constants
     */
    private static final String LOG_TAG = ConfigActivity.class.getSimpleName();

    /**
     * UI components
     */
    private EditText mEtNameFieldInput;
    private EditText mEtEmailFieldInput;
    private TextInputLayout mTilOldPassword;
    private EditText mEtOldPasswordFieldInput;
    private TextInputLayout mTilNewPassword;
    private EditText mEtNewPasswordFieldInput;
    private EditText mEtTmdbApiKeyFieldInput;
    private Button mBtnChangeName;
    private Button mBtnChangeApiKey;
    private Button mBtnChangeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mEtNameFieldInput = findViewById(R.id.etNameFieldInput);
        mEtEmailFieldInput = findViewById(R.id.etEmailFieldInput);
        mTilOldPassword = findViewById(R.id.textInputOldPassword);
        mEtOldPasswordFieldInput = findViewById(R.id.etOldPasswordFieldInput);
        mTilNewPassword = findViewById(R.id.textInputNewPassword);
        mEtNewPasswordFieldInput = findViewById(R.id.etNewPasswordFieldInput);
        mEtTmdbApiKeyFieldInput = findViewById(R.id.etTmdbKeyFieldInput);
        mBtnChangeName = findViewById(R.id.btnChangeName);
        mBtnChangeApiKey = findViewById(R.id.btnRegister);
        mBtnChangeEmail = findViewById(R.id.btnChangeEmail);


        final Toolbar toolbar = findViewById(R.id.Toolbar);
        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            toolbar.setNavigationIcon(R.drawable.baseline_arrow_forward_white_24);
        } else {
            toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ActivityConfig_toolbarTitle));
        toolbar.setTitleTextColor(getColor(R.color.clLightTextColor));
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary));


        /**
         * subscribe firebase auth listener
         */
        FirebaseManager.addListener(ConfigActivity.this);

        mEtNameFieldInput.setText(FirebaseManager.FirebaseAuthGetUserName());
        mEtEmailFieldInput.setText(FirebaseManager.FirebaseAuthGetUserEmail());
        mEtTmdbApiKeyFieldInput.setText(Network.API_KEY);

        mBtnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeName();
            }
        });

        mBtnChangeApiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeApiKey();
            }
        });

        mBtnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail();
            }
        });

        Utils.clearAllFocus(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Utils.AlertDialogDismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /**
         * unsubscribe firebase auth listener
         */
        FirebaseManager.removeListener(ConfigActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void mListenerSignInSuccessful() { }

    @Override
    public void mListenerSignInFail(String reason) { }

    @Override
    public void mListenerRegisterSuccessful() { }

    @Override
    public void mListenerRegisterFail(String reason) { }

    @Override
    public void mListenerChangeCredentialsSuccessful() {
        Utils.ProgressDialogStop();
        mTilOldPassword.setVisibility(View.GONE);
        mTilNewPassword.setVisibility(View.GONE);
        Toast.makeText(this, "Config Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mListenerChangeCredentialsFail(String reason) {
        Utils.ProgressDialogStop();
        Toast.makeText(this, "Oops! " + reason, Toast.LENGTH_LONG).show();
    }

    @Override
    public void mListenerDatabaseSetApiKeySuccessful() {
        Utils.ProgressDialogStop();
        Toast.makeText(this, "Config Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mListenerDatabaseSetApiKeyFail(String reason) {
        Utils.ProgressDialogStop();
        Toast.makeText(this, "Oops! " + reason, Toast.LENGTH_LONG).show();
    }

    @Override
    public void mListenerDatabaseGetApiKey(String key) {}

    private void changeApiKey() {

        if(!mConnectionUp) {

            showToast(getString(R.string.no_internet_toast));
            return;
        }

        /**
         * store the ApiKey in Firebase database
         */
        String strApiKey = mEtTmdbApiKeyFieldInput.getText().toString();
        Utils.ProgressDialogStart(new WeakReference<Activity>(this),  getString(R.string.ProgressBarAnimationChangingConfigs));
        FirebaseManager.FirebaseDatabaseSetApiKey(ConfigActivity.this, strApiKey);

    }

    private void changeName() {

        if(!mConnectionUp) {

            showToast(getString(R.string.no_internet_toast));
            return;
        }

        String name = mEtNameFieldInput.getText().toString();
        FirebaseManager.FirebaseAuthChangeName(name);
        Toast.makeText(ConfigActivity.this, "Config Successful", Toast.LENGTH_SHORT).show();
    }

    private void changeEmail() {

        if(!mConnectionUp) {

            showToast(getString(R.string.no_internet_toast));
            return;
        }

        String email = mEtEmailFieldInput.getText().toString();
        String oldPass = mEtOldPasswordFieldInput.getText().toString();
        String newPass = mEtNewPasswordFieldInput.getText().toString();

        if(oldPass.isEmpty() && newPass.isEmpty() && mTilOldPassword.getVisibility()==View.GONE) {
            mTilOldPassword.setVisibility(View.VISIBLE);
            mTilNewPassword.setVisibility(View.VISIBLE);
        } else {
            if(Utils.checkFormEmailOldNewPassword(email, mEtEmailFieldInput, oldPass, mEtOldPasswordFieldInput, newPass, mEtNewPasswordFieldInput)) {
                Utils.ProgressDialogStart(new WeakReference<Activity>(this),  getString(R.string.ProgressBarAnimationChangingConfigs));
                FirebaseManager.FirebaseAuthChangeUser(ConfigActivity.this, email, oldPass, newPass);
            }
        }

    }
}
