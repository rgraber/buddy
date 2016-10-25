package com.example.rebeccagraber.buddy;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private ListView lvFriends;
    private ListView lvEvents;

    private ArrayList friends;
    private ArrayList events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main2);

        lvFriends = (ListView) findViewById(R.id.lvFriends);
        lvEvents = (ListView) findViewById(R.id.lvEvents);


        friends = new ArrayList();
        events = new ArrayList();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        final ArrayList<FacebookEvent> facebookEventArrayList = b.getParcelableArrayList("EVENTS");
        final ArrayList<FacebookFriend> facebookFriends = b.getParcelableArrayList("FRIENDS");
        for(FacebookEvent fe: facebookEventArrayList)
        {
            Log.d("BUDDY", fe.getName());
            events.add(fe.getName());
        }
        for(FacebookFriend fe: facebookFriends)
        {
            Log.d("BUDDY", fe.getName());
            friends.add(fe.getName());
        }

        ArrayAdapter<ArrayList> fAdapter = new ArrayAdapter<ArrayList>(this,android.R.layout.simple_list_item_1, friends);
        ArrayAdapter<ArrayList> eAdapter = new ArrayAdapter<ArrayList>(this,android.R.layout.simple_expandable_list_item_1, events);
        lvFriends.setAdapter(fAdapter);
        lvEvents.setAdapter(eAdapter);


        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int eid = facebookEventArrayList.get(position).getID();

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://facebook.com/events/"+ eid))
                        .build();

                ShareDialog.show(Main2Activity.this, content);

            }
        });
    }
}
