package com.example.android.whatsappclone;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {
    EditText chatText;
    Button sendButton;
    ListView chatList;
    ArrayList<Chats> chats;
    ChatsAdapter adapter;
    String userToChatWith;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Intent intent = getIntent();
        userToChatWith = intent.getStringExtra("usernameToChatWith");
        setTitle(userToChatWith);

        chatText = (EditText) findViewById(R.id.chatInput);
        sendButton = (Button) findViewById(R.id.sendButton);
        chatList = (ListView) findViewById(R.id.chatList);
        chats = new ArrayList<>();
        adapter = new ChatsAdapter(this, R.layout.chat_list_item, chats, userToChatWith);
        chatList.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = chatText.getText().toString();
                final Chats c = new Chats(ParseUser.getCurrentUser().getUsername(), userToChatWith, content);
                ParseObject message = new ParseObject("Message");
                message.put("sender", c.getSender());
                message.put("content", c.getContent());
                message.put("receiver", c.getReceiver());

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            chats.add(c);
                            adapter.notifyDataSetChanged();
                            chatText.setText("");
                        } else
                            Toast.makeText(ConversationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        handler = new Handler();
        updateChatList();
    }

    private void updateChatList() {
        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("receiver", userToChatWith);

        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("sender", userToChatWith);
        query2.whereEqualTo("receiver", ParseUser.getCurrentUser().getUsername());

        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    chats.clear();

                    for (ParseObject object : objects) {
                        chats.add(new Chats(object.getString("sender"), object.getString("receiver"), object.getString("content")));
                    }
                    adapter.notifyDataSetChanged();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateChatList();
                    }
                }, 5000);
            }
        });
    }
}
