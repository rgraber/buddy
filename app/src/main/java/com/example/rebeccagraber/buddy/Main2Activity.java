package com.example.rebeccagraber.buddy;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.SendButton;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private ListView lvFriends;
    private ListView lvEvents;
    private TextView tvFriends;
    private TextView tvEvents;
    private Button btnSend;
    private Button btnShare;

    // for ListView
    private ArrayList friends;
    private ArrayList events;


    // for click on events list
    private CallbackManager cm;


    private String chosenId;
    private LikeView likeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main2);
        // binding
        btnSend = (Button) findViewById(R.id.btnSend);
        btnShare = (Button) findViewById(R.id.btnShare);

        cm = CallbackManager.Factory.create();

        lvFriends = (ListView) findViewById(R.id.lvFriends);
        lvEvents = (ListView) findViewById(R.id.lvEvents);
        tvEvents = (TextView) findViewById(R.id.tvEvents);
        tvFriends = (TextView) findViewById(R.id.tvFriends);
        likeView = (LikeView) findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);

        // like link button
        likeView.setObjectIdAndType(
                "https://www.facebook.com/FacebookDevelopers",
                LikeView.ObjectType.PAGE);


        friends = new ArrayList();
        events = new ArrayList();
        Intent i = getIntent();
        Bundle b = i.getExtras();

        // retrieve events and friends from intent i with bundle
        final ArrayList<FacebookEvent> facebookEventArrayList = b.getParcelableArrayList("EVENTS");
        final ArrayList<FacebookFriend> facebookFriends = b.getParcelableArrayList("FRIENDS");
        for (FacebookEvent fe : facebookEventArrayList) {
            Log.d("BUDDY", fe.getName());
            events.add(fe.getName());
        }
        for (FacebookFriend fe : facebookFriends) {
            Log.d("BUDDY", fe.getName());
            friends.add(fe.getName());
        }


        // set ListView Adapters
        ArrayAdapter<ArrayList> fAdapter = new ArrayAdapter<ArrayList>(this, android.R.layout.simple_list_item_1, friends);
        ArrayAdapter<ArrayList> eAdapter = new ArrayAdapter<ArrayList>(this, android.R.layout.simple_expandable_list_item_1, events);
        lvFriends.setAdapter(fAdapter);
        lvEvents.setAdapter(eAdapter);


        // listen to click on events list
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eid = facebookEventArrayList.get(position).getID();
                chosenId = eid;

            }
        });


        // send link to friend button
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://facebook.com/events/" + chosenId))
                        .build();

                MessageDialog.show(Main2Activity.this, content);

            }
        });
        // share link to facebook page button
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://facebook.com/events/" + chosenId))
                        .build();

                ShareDialog.show(Main2Activity.this, content);

            }
        });





    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cm.onActivityResult(requestCode, resultCode, data);
    }

}
