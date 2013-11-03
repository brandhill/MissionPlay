/*
 * Copyright 2013 Mitsw
 *
 */
package com.mitsw.yahoohackathon.missionplay.activity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.ion.Ion;
import com.mitsw.yahoohackathon.missionplay.Constants;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class LoginActivity extends Activity{
    private final String TAG = LoginActivity.class.getSimpleName();


    GoogleCloudMessaging gcm;
    String SENDER_ID = "718695621323";    
    
    private LoginButton loginButton;
    private ProfilePictureView profilePictureView;
    private GraphUser user;
    private List<GraphUser> tags;    
    
    private UiLifecycleHelper uiHelper;
    private PendingAction pendingAction = PendingAction.NONE;
    
    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }    

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d(TAG, String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d(TAG, "Success!");
        }
    };    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);        
        
        Session session = Session.getActiveSession();
        
        if (session.isOpened()){            
            Utils.goHome(LoginActivity.this);
            finish();
        }
        
        setContentView(R.layout.activity_login);       
        
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                LoginActivity.this.user = user;
                
                if (user !=null){
                    Log.d(TAG, "user : "+ user.toString());
                    
                    //Utils.sendUserInfoToServer(LoginActivity.this, user);
                    SharedPreferences.Editor mEditor =  getSharedPreferences("MissionPlay", MODE_PRIVATE).edit();
                    
                    if (!TextUtils.isEmpty(user.getId())) {

                        String emailString = "d";
                        try {

                            mEditor.putString(Constants.USER_ID, user.getId());
                            emailString = (String) user.getProperty("email");
                            mEditor.putString(Constants.USER_EMAIL, emailString);
                            mEditor.putString(Constants.USER_PHOTO_URL, Utils.getFbProfilePictuerUrl(user.getId()));
                            Log.d(TAG, "" + user.getId() + ", " + emailString + ", "+user.getName());
                        } catch (Exception e) {
                            Log.d(TAG, "Exception " + e.toString());
                        }

                    }
                    
                    if (!TextUtils.isEmpty(user.getName())){
                        mEditor.putString(Constants.USER_NAME, user.getName());
                    }
                    
    
                    mEditor.commit();
                    
                    
                    /*
                    final Intent intent = new Intent().setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    */
                    
                }
                
                //updateUI();
                // It's possible that we were waiting for this.user to be populated in order to post a
                // status update.
                //handlePendingAction();
            }
        });
        
        loginButton.setReadPermissions(Arrays.asList("email"));
        
        
        gcm = GoogleCloudMessaging.getInstance(this);
        
        // register with Google.
        new AsyncTask<Void,String,String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    String strRegId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration id=" + strRegId;
                 
                    // send id to our server
                    //sendRegIdToServer(strRegId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //tvRegisterMsg.append(msg + "\n");
                Log.d(TAG, "POST MSG : "+ msg);
            }

        }.execute(null, null, null);        
        //*/
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        

    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        //outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
        
        Log.d(TAG, "onActivityResult");
        
     
        
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        Log.d(TAG, "onSessionStateChange");
        
        if (session.isOpened()){
            Utils.goHome(LoginActivity.this);
            finish();
        }
        
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(LoginActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            //handlePendingAction();
        }
        //updateUI();
    }    
    
    

    
}
