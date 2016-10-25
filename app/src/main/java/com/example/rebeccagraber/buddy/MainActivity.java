package com.example.rebeccagraber.buddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ArrayList<FacebookEvent> event_ids;
    private ArrayList<FacebookFriend> friend_ids;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.rebeccagraber.buddy",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        callbackManager = CallbackManager.Factory.create();
        event_ids = new ArrayList<FacebookEvent>();
        friend_ids = new ArrayList<FacebookFriend>();
        context = this.getBaseContext();

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
                    Log.d("BUDDY", "Victory! " + sId);
                    GraphRequestBatch batch = new GraphRequestBatch(
                            new GraphRequest(accessToken,
                                    "/" + sId + "/friends",
                                    null,
                                    HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        @Override
                                        public void onCompleted(GraphResponse response) {
                                            Log.d("BUDDY", "Friend request");
                                            try {
                                                JSONObject jo = response.getJSONObject();
                                                JSONArray ja = jo.getJSONArray("data");
                                                int size = ja.length();
                                                int max = Math.min(5, size);
                                                for (int i = 0; i < max; i++) {
                                                    JSONObject o = ja.getJSONObject(i);
                                                    int id = o.getInt("id");
                                                    String name = o.getString("name");
                                                    FacebookFriend ff = new FacebookFriend(id, name);
                                                    friend_ids.add(ff);
                                                }
                                            } catch (JSONException je) {
                                                Log.e("BUDDY", "Error parsing friend list!");
                                            }
                                        }
                                    }),
                            new GraphRequest(
                                    accessToken,
                                    "/" + sId + "/events",
                                    null,
                                    HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {
            /* handle the result */
                                            Log.d("BUDDY", "Event Request");
                                            try {
                                                JSONObject jo = response.getJSONObject();
                                                JSONArray ja = jo.getJSONArray("data");
                                                int size = ja.length();
                                                int max = Math.min(5, size);
                                                for (int i = 0; i < max; i++) {
                                                    JSONObject o = ja.getJSONObject(i);
                                                    String id = o.getString("id");
                                                    String name = o.getString("name");
                                                    FacebookEvent fe = new FacebookEvent(id, name);
                                                    event_ids.add(fe);
                                                }

                                            } catch (JSONException je) {
                                                Log.e("BUDDY", "Error in parsing JSON!");
                                            }
                                        }
                                    }

                            )
                    );
                    batch.addCallback(new GraphRequestBatch.Callback() {
                                          @Override
                                          public void onBatchCompleted(GraphRequestBatch graphRequests) {
                                              Intent i = new Intent(context, Main2Activity.class);
                                              i.putParcelableArrayListExtra("EVENTS", event_ids);
                                              i.putParcelableArrayListExtra("FRIENDS", friend_ids);
                                              i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                              context.startActivity(i);
                                          }
                                      }

                    );
                    batch.executeAsync();
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
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
