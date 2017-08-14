package com.example.android.whatsappclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    ArrayList<String> userList;
    ArrayAdapter adapter;
    ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("User List");

        userListView = (ListView) findViewById(R.id.userList);
        userList = new ArrayList<>();
        userList.add("Get user list...");
        getUserList();
        adapter = new ArrayAdapter(this, R.layout.user_list_item, userList);
        userListView.setAdapter(adapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!userList.get(i).equals("There is no user available")) {
                    Intent intent = new Intent(UserListActivity.this, ConversationActivity.class);
                    intent.putExtra("usernameToChatWith", userList.get(i));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserList() {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    userList.clear();
                    if (objects.size() > 0) {
                        for (ParseUser user : objects)
                            userList.add(user.getUsername());
                    } else
                        userList.add("There is no user available");
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
