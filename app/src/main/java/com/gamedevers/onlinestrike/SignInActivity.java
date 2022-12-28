package com.gamedevers.onlinestrike;

import static android.content.ContentValues.TAG;
import static com.gamedevers.onlinestrike.MainActivity.database;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignInActivity extends Activity {
    private static FirebaseAuth mAuth;
    EditText edPass,edEmail;
    public static FirebaseUser currentUser;
    public static DatabaseReference data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        database = FirebaseDatabase.getInstance("https://chat-d5f5b-default-rtdb.europe-west1.firebasedatabase.app");
        edPass = findViewById(R.id.edPass);
        edEmail = findViewById(R.id.edEmail);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String a = String.valueOf(currentUser);
            String result = a.replaceAll("[^\\p{N}]+", "");
            data=database.getReference(result);
            Intent l = new Intent(this, MainActivity.class);
            startActivity(l);
        }
    }

    public void onClickSignUp(View view){
        if(!TextUtils.isEmpty(edEmail.getText().toString()) && !TextUtils.isEmpty(edPass.getText().toString())){
            mAuth.createUserWithEmailAndPassword(edEmail.getText().toString(),edPass.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            i();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
            String a = String.valueOf(currentUser);
            String result = a.replaceAll("[^\\p{N}]+", "");
            data = database.getReference(result);
            data.setValue(currentUser);
        }
    }
    public void onClickSignIn(View view){
        if(!TextUtils.isEmpty(edEmail.getText().toString()) && !TextUtils.isEmpty(edPass.getText().toString())){
            mAuth.signInWithEmailAndPassword(edEmail.getText().toString(),edPass.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            i();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
            String a = String.valueOf(currentUser);
            String result = a.replaceAll("[^\\p{N}]+", "");
            data = database.getReference(result);
            data.setValue(currentUser);
        }
    }

    public void i(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
