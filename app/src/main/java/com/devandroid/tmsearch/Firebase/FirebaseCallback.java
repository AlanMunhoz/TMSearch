package com.devandroid.tmsearch.Firebase;

public interface FirebaseCallback {

    void mListenerSignInSuccessful();
    void mListenerSignInFail(String reason);
    void mListenerRegisterSuccessful();
    void mListenerRegisterFail(String reason);
    void mListenerChangeCredentialsSuccessful();
    void mListenerChangeCredentialsFail(String reason);
    void mListenerDatabaseSetApiKeySuccessful();
    void mListenerDatabaseSetApiKeyFail(String reason);
    void mListenerDatabaseGetApiKey(String key);

}
