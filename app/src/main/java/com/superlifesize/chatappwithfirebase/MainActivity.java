package com.superlifesize.chatappwithfirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String FIREBASE_URL = "https://chatappwithfirebase.firebaseio.com/";

    private Firebase firebaseRootRef;
    private FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder> firebaseRecyclerAdapter;
    private Query chatRef;
    private RecyclerView chatMessageRecyclerView;
    private EditText chatMessageEditText;
    private Button chatMessageSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Reference to the Firebase database for this app
        firebaseRootRef = new Firebase(FIREBASE_URL);
        chatRef = firebaseRootRef.limitToFirst(20);

        chatMessageEditText = (EditText) findViewById(R.id.chatMessageEditText);
        chatMessageSendButton = (Button) findViewById(R.id.chatMessageSendButton);

        chatMessageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Update User to be user's login identifier
                ChatMessage chat = new ChatMessage("User", chatMessageEditText.getText().toString());
                firebaseRootRef.push().setValue(chat, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if(firebaseError != null) {
                            Log.e(TAG, "Firebase Error: " + firebaseError.toString());
                        }
                    }
                });
                chatMessageEditText.setText("");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //TODO: Scroll to newest message

        chatMessageRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        chatMessageRecyclerView.setHasFixedSize(true);
        chatMessageRecyclerView.setLayoutManager(layoutManager);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(
                ChatMessage.class,
                android.R.layout.simple_expandable_list_item_2,
                ChatMessageViewHolder.class,
                chatRef) {

            @Override
            protected void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatMessage chatMessage, int position) {
                chatMessageViewHolder.userText.setText(chatMessage.getUser());
                chatMessageViewHolder.messageText.setText(chatMessage.getMessage());
            }
        };

        chatMessageRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView userText;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(android.R.id.text1);
            userText = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
