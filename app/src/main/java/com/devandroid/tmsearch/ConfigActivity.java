package com.devandroid.tmsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devandroid.tmsearch.Firebase.ErrorCodes;
import com.devandroid.tmsearch.Firebase.FirebaseManager;
import com.devandroid.tmsearch.Firebase.mListener;
import com.devandroid.tmsearch.Network.Network;
import com.devandroid.tmsearch.Util.Utils;

public class ConfigActivity extends AppCompatActivity implements mListener {

    /**
     * Constants
     */
    private static final String LOG_TAG = ConfigActivity.class.getSimpleName();

    /**
     * UI components
     */
    private EditText mEtNameFieldInput;
    private EditText mEtEmailFieldInput;
    private EditText mEtTmdbApiKeyFieldInput;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mEtNameFieldInput = findViewById(R.id.etNameFieldInput);
        mEtEmailFieldInput = findViewById(R.id.etEmailFieldInput);
        mEtTmdbApiKeyFieldInput = findViewById(R.id.etTmdbKeyFieldInput);
        mBtnRegister = findViewById(R.id.btnRegister);


        final Toolbar toolbar = findViewById(R.id.Toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ActivityConfig_toolbarTitle));
        toolbar.setTitleTextColor(getColor(R.color.clLightTextColor));
        toolbar.setBackgroundColor(getColor(R.color.clSelectedBackground));


        /**
         * subscribe firebase auth listener
         */
        FirebaseManager.addListener(ConfigActivity.this);

        mEtNameFieldInput.setText(FirebaseManager.FirebaseAuthGetUserName());
        mEtEmailFieldInput.setText(FirebaseManager.FirebaseAuthGetUserEmail());
        mEtTmdbApiKeyFieldInput.setText(Network.API_KEY);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();
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
    public void mListenerChangeCredentialsSuccessful() {}

    @Override
    public void mListenerChangeCredentialsFail(String reason) {}

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

    private void startRegister() {

        /**
         * store the ApiKey in Firebase database
         */
        String strApiKey = mEtTmdbApiKeyFieldInput.getText().toString();
        Utils.ProgressDialogStart(ConfigActivity.this, ErrorCodes.ProgressBarAnimationChangingConfigs);
        FirebaseManager.FirebaseDatabaseSetApiKey(ConfigActivity.this, strApiKey);
    }
}
