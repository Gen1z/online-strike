package com.gamedevers.onlinestrike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gamedevers.onlinestrike.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    static FirebaseDatabase database;
    EditText edMessage;
    TextView tvChat;
    static DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        edMessage = findViewById(R.id.edMessage);
        tvChat = findViewById(R.id.tvChat);
        database = FirebaseDatabase.getInstance("https://chat-d5f5b-default-rtdb.europe-west1.firebasedatabase.app");
        dr = database.getReference("ChatData");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvChat.setText(tvChat.getText().toString()+"\n");
                tvChat.setText(tvChat.getText().toString()+snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickSend(View view){
        if(edMessage.getText() != null){
            dr.setValue(edMessage.getText().toString());
        }
    }
}