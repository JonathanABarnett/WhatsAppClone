package com.alaythiaproductions.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderId;

    private TextView userName, userLastSeen;
    private CircleImageView userImage;
    private ImageButton sendMessageButton;
    private EditText message;

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        messageReceiverID = getIntent().getExtras().get("userIds").toString();
        messageReceiverName = getIntent().getExtras().get("userName").toString();
        messageReceiverImage = getIntent().getExtras().get("userImage").toString();

        initializeFields();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        userName.setText(messageReceiverName);
        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.blank_profile_image).into(userImage);

    };

    private void initializeFields() {

        mToolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        userImage = findViewById(R.id.custom_profile_image);
        userName = findViewById(R.id.custom_profile_name);
        userLastSeen = findViewById(R.id.custom_user_last_seen);
        message = findViewById(R.id.chat_input_message);
        sendMessageButton = findViewById(R.id.chat_send_message);

        messageAdapter = new MessageAdapter(messagesList);
        userMessagesList = findViewById(R.id.chat_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        rootRef.child("Messages").child(messageReceiverID).child(messageSenderId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages = dataSnapshot.getValue(Messages.class);

                messagesList.add(messages);

                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String messageText = message.getText().toString().trim();

        if (messageText.equals("")) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show();
        } else {
            String messageSenderRef = "Messages/" + messageSenderId + "/" + messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderId;

            DatabaseReference userMessageKeyRef = rootRef.child("Messages").child(messageSenderId)
                    .child(messageReceiverID).push();

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderId);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    message.setText("");
                }
            });
        }
    }


}
