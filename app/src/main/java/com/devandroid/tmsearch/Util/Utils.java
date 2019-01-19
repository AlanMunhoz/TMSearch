package com.devandroid.tmsearch.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.devandroid.tmsearch.Firebase.ErrorCodes;

public class Utils {

    private static ProgressDialog progressDialog;
    private static AlertDialog alertDialog;

    /**
     * Check Email and Password in a EditText Field. Set error message in case of bad formatter.
     */
    public static boolean checkFormEmailPassword(String email, EditText editTextEmail, String password, EditText editTextPassword)
    {
        boolean ok = true;
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(ErrorCodes.EmailFieldEmptyError);
            ok=false;
        }else if(!email.contains("@")) {
            editTextEmail.setError(ErrorCodes.EmailFieldMalFormedError);
            ok=false;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(ErrorCodes.PasswordFieldEmptyError);
            ok=false;
        }
        return ok;
    }

    /**
     * Check Name, Email and Password in a EditText Field. Set error message in case of bad formatter.
     */
    public static boolean checkFormNameEmailPassword(String name, EditText editTextName, String email, EditText editTextEmail, String password, EditText editTextPassword)
    {
        boolean ok = true;
        if(TextUtils.isEmpty(name)) {
            editTextName.setError(ErrorCodes.NameFieldEmptyError);
            ok=false;
        }
        if (!checkFormEmailPassword(email, editTextEmail, password, editTextPassword)) {
            ok=false;
        }
        return ok;
    }

    /**
     * Start a Progress Dialog
     */
    public static void ProgressDialogStart(Activity activity, String message){
        //exibe progress bar
        progressDialog = new ProgressDialog( activity );
        progressDialog.setTitle(message);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    /**
     * Stop a Progress Dialog
     */
    public static void ProgressDialogStop(){
        if(progressDialog!=null) {
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    /**
     * Start a AlertDialog
     */
    public static void AlertDialogStart(Activity activity, String strTitle, String strMessage, String strPositive, DialogInterface.OnClickListener posListener, String strNegative, DialogInterface.OnClickListener negListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setPositiveButton(strPositive, posListener);
        if(!strNegative.isEmpty()) {
            builder.setNegativeButton(strNegative, negListener);
        }
        alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Hide the keyboard
     */
    public static void hidekeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Clear focus on any component
     */
    public static void clearAllFocus(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
