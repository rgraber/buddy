package com.example.rebeccagraber.buddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        ArrayList<FacebookEvent> facebookEventArrayList = b.getParcelableArrayList("EVENTS");
        ArrayList<FacebookFriend> facebookFriends = b.getParcelableArrayList("FRIENDS");
        for(FacebookEvent fe: facebookEventArrayList)
        {
            Log.d("BUDDY", fe.getName());
        }
        for(FacebookFriend fe: facebookFriends)
        {
            Log.d("BUDDY", fe.getName());
        }
    }
}
