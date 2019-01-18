package com.devandroid.tmsearch.Firebase;

public interface mListener {

    void mListenerSignInSuccessful();
    void mListenerSignInFail(String reason);
    void mListenerRegisterSuccessful();
    void mListenerRegisterFail(String reason);
    void mListenerChangeCredentialsSuccessful();
    void mListenerChangeCredentialsFail(String reason);

}
