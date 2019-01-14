package com.devandroid.tmsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devandroid.tmsearch.Network.Network;
import com.devandroid.tmsearch.Preferences.Preferences;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnLogin;
    private EditText mEtEmailFieldInput;
    private EditText mEtPasswordFieldInput;
    private EditText mEtTmdbApiKeyFieldInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnLogin = findViewById(R.id.btnLogin);
        mEtEmailFieldInput = findViewById(R.id.etEmailFieldInput);
        mEtPasswordFieldInput = findViewById(R.id.etPasswordFieldInput);
        mEtTmdbApiKeyFieldInput = findViewById(R.id.etTmdbKeyFieldInput);

        String strTmdbApiKey = Preferences.restoreStringTmdbApiKey(LoginActivity.this);
        mEtTmdbApiKeyFieldInput.setText(strTmdbApiKey);
        Network.setApiKey(strTmdbApiKey);

        mBtnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strApiKey = mEtTmdbApiKeyFieldInput.getText().toString();
                if(!strApiKey.isEmpty()) {
                    Preferences.saveStringTmdbApiKey(LoginActivity.this, strApiKey);
                    Network.setApiKey(strApiKey);
                }
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

    }
}
