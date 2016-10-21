package com.example.rebeccagraber.buddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("user_events");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                String sId = accessToken.getUserId();
                Log.d("BUDDY","Victory! " + sId);
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + sId + "/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try
                                {
                                    JSONObject jo = response.getJSONObject();
                                    int nFriends = response.getJSONObject().getJSONObject("summary").getInt("total_count");
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "You have " + Integer.toString(nFriends) + "friends ", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                                catch(JSONException je)
                                {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Error getting friends", Toast.LENGTH_LONG);
                                    toast.show();
                                }


            /* handle the result */
                            }
                        }
                ).executeAsync();
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
