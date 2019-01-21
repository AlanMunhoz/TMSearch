package com.devandroid.tmsearch.Firebase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.analytics.FirebaseAnalytics;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public final class FirebaseManager {

    private static FirebaseAuth mAuth;

    private static DatabaseReference mDb;

    private static FirebaseAnalytics mAnalytics;
    private static List<FirebaseCallback> firebaseCallbacks;

    public enum EventKeys {

        LOGIN_BUTTON("1", "login_button", "action"),
        CREATE_ACCOUNT_BUTTON("2", "create_account_button", "action"),
        FAVORITE_BUTTON("3", "favorite_button", "action"),
        MOST_POPULAR_MENU("4", "most_popular_menu", "action"),
        TOP_RATED_MENU("5", "top_rated_menu", "action"),
        NOW_LAYING_MENU("6", "now_playing_menu", "action"),
        UPCOMING_MENU("7", "upcoming_menu", "action"),
        FAVORITES_MENU("8", "favorites_menu", "action"),
        CONFIG_MENU("9", "config_menu", "action"),
        EXIT_MENU("10", "exit_menu", "action"),
        MOVIE_CLICK_LIST("11", "movie_click_list", "action"),
        TRAILER_CLICK_LIST("12", "trailer_click_list", "action");

        public String mId;
        public String mName;
        public String mContentType;

        EventKeys(String id, String name, String contentType) {
            mId = id;
            mName = name;
            mContentType = contentType;
        }
    }

    /**
     * Initialize the objects in Firebase
     */
    public static void FirebaseManagerInit(Context context) {

        /**
         * listener reference
         */
        firebaseCallbacks = new ArrayList<>();
        /**
         * Firebase authentication reference
         */
        mAuth = FirebaseAuth.getInstance();
        /**
         * Firebase analytics reference
         */
        mAnalytics = FirebaseAnalytics.getInstance(context);
        /**
         * Firebase database reference
         */
        mDb = FirebaseDatabase.getInstance().getReference();

    }

    /**
     * Add listener to object pass by parameter
     */
    public static void addListener(FirebaseCallback toAdd) {
        firebaseCallbacks.add(toAdd);
    }

    /**
     * Add listener to object pass by parameter
     */
    public static void removeListener(FirebaseCallback toAdd) {
        firebaseCallbacks.remove(toAdd);
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

    public static void FirebaseAuthChangeName(String name) {

        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        user.updateProfile(profileUpdates);
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

                        /**
                         * once the login was done, retrieve ApiKey from firebase database
                         */
                        FirebaseDatabaseRestoreApiKey();

                        for (FirebaseCallback events : firebaseCallbacks) events.mListenerSignInSuccessful();
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
                        for (FirebaseCallback events : firebaseCallbacks) events.mListenerSignInFail(erroExcecao);
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

                        FirebaseAuthChangeName(name);

                        for (FirebaseCallback events : firebaseCallbacks) events.mListenerRegisterSuccessful();
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
                        for (FirebaseCallback events : firebaseCallbacks) events.mListenerRegisterFail(erroExcecao);
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
                                            for (FirebaseCallback events : firebaseCallbacks) events.mListenerChangeCredentialsSuccessful();
                                        }else {
                                            for (FirebaseCallback events : firebaseCallbacks) events.mListenerChangeCredentialsFail(ErrorCodes.FirebaseAuthChangePasswordError);
                                        }
                                    }
                                });
                            }else {
                                for (FirebaseCallback events : firebaseCallbacks) events.mListenerChangeCredentialsFail(ErrorCodes.FirebaseAuthChangeEmailError);
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
                    for (FirebaseCallback events : firebaseCallbacks) events.mListenerSignInFail(erroExcecao);
                }
            }
        });
    }

    /**
     * Make log event on Firebase Analytics
     */
    public static void FirebaseAnalyticsLogEvent(EventKeys eventKey) {

        /**
         * Enable DebugView to visualize sent events on terminal through avd
         * (1) In Android SDK Manager/Android SDK/SDK Tools, enable Android SDK Platform-Tools
         * (2) Verify if there is the adb.exe in folder like "C:\Users\Alan\AppData\Local\Android\sdk\platform-tools"
         * (3) Execute the command "adb shell setprop debug.firebase.analytics.app <package_name>"
         */

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, eventKey.mId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventKey.mName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, eventKey.mContentType);
        mAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * Retrieve data from Firebase database
     */
    public static void FirebaseDatabaseRestoreApiKey() {

        mDb.child("users").child(FirebaseAuthGetUserId()).child("ApiKey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String strApiKey = dataSnapshot.getValue() != null ? (String)dataSnapshot.getValue() : "";

                for (FirebaseCallback events : firebaseCallbacks) events.mListenerDatabaseGetApiKey(strApiKey);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                for (FirebaseCallback events : firebaseCallbacks) events.mListenerDatabaseGetApiKey("");
            }
        });

    }

    /**
     * Store data in Firebase database
     */
    public static void FirebaseDatabaseSetApiKey(Activity activity, String strApiKey) {
        try{
            mDb.child("users").child(FirebaseAuthGetUserId()).child("ApiKey").setValue(strApiKey).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (FirebaseCallback events : firebaseCallbacks) events.mListenerDatabaseSetApiKeySuccessful();
                    }
                    else {
                        String erroExcecao = ErrorCodes.FirebaseDbSetApiKeyError;
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            erroExcecao = ErrorCodes.FirebaseDbSetApiKeyError + e.getMessage();
                            e.printStackTrace();
                        }
                        for (FirebaseCallback events : firebaseCallbacks) events.mListenerDatabaseSetApiKeyFail(erroExcecao);
                    }
                }
            });
        }catch (Exception e) {
            for (FirebaseCallback events : firebaseCallbacks) events.mListenerDatabaseSetApiKeyFail(e.getMessage());
            e.printStackTrace();
        }
    }

}



