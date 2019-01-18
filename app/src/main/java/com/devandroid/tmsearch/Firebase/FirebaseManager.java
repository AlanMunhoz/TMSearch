package com.devandroid.tmsearch.Firebase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;


public final class FirebaseManager {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static List<mListener> listeners = new ArrayList<mListener>();

    /**
     * Add listener to object pass by parameter
     */
    public static void addListener(mListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * Add listener to object pass by parameter
     */
    public static void removeListener(mListener toAdd) {
        listeners.remove(toAdd);
    }

    /**
     * Return true if there is some user logged in, otherwise return false
     */
    public static boolean FirebaseAuthIsLoggedIn(){
        if(mAuth.getCurrentUser() != null) return true;
        return false;
    }

    /**
     * Return a user logged in if exists, otherwise return null
     */
    public static String FirebaseAuthGetUserId()
    {
        if(mAuth.getCurrentUser() != null) { return mAuth.getCurrentUser().getUid(); }
        else { return null; }
    }

    /**
     * Return the name of user logged in if exists, otherwise return ""
     */
    public static String FirebaseAuthGetUserName() {

        FirebaseUser fbUser = mAuth.getCurrentUser();
        String strUserName = "";

        if(fbUser!=null) {
            strUserName = fbUser.getDisplayName();
        }
        return strUserName;
    }

    /**
     * Return the email of user logged in if exists, otherwise return ""
     */
    public static String FirebaseAuthGetUserEmail() {

        FirebaseUser fbUser = mAuth.getCurrentUser();
        String strUserEmail = "";

        if(fbUser!=null) {
            strUserEmail = fbUser.getEmail();
        }
        return strUserEmail;
    }

    /**
     * Make a sign in of a user
     */
    public static void FirebaseAuthStartSignIn(final Activity activity, String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        for (mListener events : listeners) events.mListenerSignInSuccessful();
                    } else {
                        String erroExcecao = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erroExcecao = ErrorCodes.FirebaseAuthInvalidCredentialsException;
                        } catch (FirebaseAuthInvalidUserException e) {
                            erroExcecao = ErrorCodes.FirebaseAuthInvalidUserException;
                        } catch (FirebaseNetworkException e) {
                            erroExcecao = ErrorCodes.FirebaseNetworkException;
                        } catch (Exception e) {
                            erroExcecao = ErrorCodes.FirebaseAuthGenericError /*+ e.getMessage()*/;
                            e.printStackTrace();
                        }
                        for (mListener events : listeners) events.mListenerSignInFail(erroExcecao);
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(activity, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Make sign out of a user
     */
    public static void FirebaseAuthStartSignOut()
    {
        mAuth.signOut();
    }

    /**
     * Make register of a user
     */
    public static void FirebaseAuthStartRegister(final Activity activity, final String name, String email, String password) {
        try{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();
                        user.updateProfile(profileUpdates);

                        for (mListener events : listeners) events.mListenerRegisterSuccessful();
                    }
                    else {
                        String erroExcecao="";
                        try{
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erroExcecao = ErrorCodes.FirebaseAuthWeakPasswordException;
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erroExcecao = ErrorCodes.FirebaseAuthInvalidCredentialsExceptionCreateUsers;
                        } catch ( FirebaseAuthUserCollisionException e){
                            erroExcecao = ErrorCodes.FirebaseAuthUserCollisionException;
                        } catch (FirebaseNetworkException e) {
                            erroExcecao = ErrorCodes.FirebaseNetworkException;
                        } catch ( Exception e) {
                            erroExcecao = ErrorCodes.FirebaseAuthGenericErrorCreateUser /*+ e.getMessage()*/;
                            e.printStackTrace();
                        }
                        for (mListener events : listeners) events.mListenerRegisterFail(erroExcecao);
                    }
                }
            });
        }catch (Exception e) {
            Toast.makeText(activity, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Make unregister of a user
     */
    public static void FirebaseAuthDelete() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {}
                else {}
            }
        });
    }

    /**
     * Change credentials of a user
     */
    public static void FirebaseAuthChangeUser(final Activity activity, final String newEmail, String oldPassword, final String newPassword)
    {
        final FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Alterando email
                    user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Alterando senha
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            for (mListener events : listeners) events.mListenerChangeCredentialsSuccessful();
                                        }else {
                                            for (mListener events : listeners) events.mListenerChangeCredentialsFail(ErrorCodes.FirebaseAuthChangePasswordError);
                                        }
                                    }
                                });
                            }else {
                                for (mListener events : listeners) events.mListenerChangeCredentialsFail(ErrorCodes.FirebaseAuthChangeEmailError);
                            }
                        }
                    });
                } else {
                    String erroExcecao = ErrorCodes.FirebaseAuthReauthenticateGenericError;
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        erroExcecao = ErrorCodes.FirebaseAuthReauthenticateGenericError + e.getMessage();
                        e.printStackTrace();
                    }
                    for (mListener events : listeners) events.mListenerSignInFail(erroExcecao);
                }
            }
        });
    }

}



