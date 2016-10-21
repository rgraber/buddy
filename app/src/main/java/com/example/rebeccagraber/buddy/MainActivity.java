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
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ArrayList<FacebookEvent> event_ids;

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
                GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMyFriendsRequest(accessToken,
                                new GraphRequest.GraphJSONArrayCallback() {
                                    @Override
                                    public void onCompleted(JSONArray objects, GraphResponse response) {
                                        Log.d("BUDDY", "Friend request");
                                    }
                                }),
                        new GraphRequest(
                                accessToken,
                                "/"+sId+"/events",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
            /* handle the result */     Log.d("BUDDY","Event Request");
                                        event_ids = new ArrayList<FacebookEvent>();
                                        try {
                                            JSONObject jo = response.getJSONObject();
                                            JSONArray ja = jo.getJSONArray("data");
                                            for(int i=0; i < 5; i ++)
                                            {
                                                JSONObject o = ja.getJSONObject(i);
                                                int id = o.getInt("id");
                                                String name = o.getString("name");
                                                FacebookEvent fe = new FacebookEvent(id,name);
                                                event_ids.add(fe);
                                            }
                                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                            intent.putParcelableArrayListExtra("events", event_ids);
                                            startActivity(intent);

                                        }
                                        catch(JSONException je)
                                        {

                                        }

                                    }
                                }

                        )
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
