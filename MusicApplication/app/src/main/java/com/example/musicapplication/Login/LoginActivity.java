package com.example.musicapplication.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.MainActivity;
import com.example.musicapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText loginPass,loginUsername;
    TextInputLayout tlUsername, tlPass;
    TextView reRegis;
    Button loginButt;

    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPreferences;
    public static final String userID = "userID";
    public static final String fileName = "login";
    public static final String userName = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.etLogUsername);
        loginPass = findViewById(R.id.etLogPass);
        loginButt = findViewById(R.id.btnLogin);
        reRegis = findViewById(R.id.tvRegis);
        tlUsername = findViewById(R.id.tlUsername);
        tlPass = findViewById(R.id.tlPass);

        tlUsername.setOnClickListener(view -> tlUsername.setHintEnabled(false));
        loginPass.setOnClickListener(view -> tlPass.setHintEnabled(false));

        checkIsLogin();
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateUser()){
                    Toast.makeText(LoginActivity.this,"Please fill all required blank!",Toast.LENGTH_SHORT).show();
                }else{
                    checkDBUser();
                }
            }
        });

        reRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class );
                startActivity(intent);
            }
        });
    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        // put your code here...
//        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
//        if (sharedPreferences.contains(userName)){
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//
//        }
//
//    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //Exit the Music App
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);
    }


    public Boolean validateUser(){
        String user = loginUsername.getText().toString();
        String pass = loginPass.getText().toString();
        if(user.isEmpty()){
            loginUsername.setError("Username cannot be empty");
            return false;
        }else{
            if(pass.isEmpty()){
                loginPass.setError("Username cannot be empty");
                return false;
            }
            loginUsername.setError(null);
            loginPass.setError(null);
            return true;

        }
    }

    public void checkDBUser(){
        String username = loginUsername.getText().toString();
        String password = loginPass.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_account");
        Query checkUserDB = databaseReference.orderByChild("username").equalTo(username);

        checkUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    loginUsername.setError(null);
                    String pwFromDB = snapshot.child(username).child("password").getValue(String.class);
                    String getID = snapshot.child(username).child("userID").getValue(String.class);
                    String getUsername = snapshot.child(username).child("username").getValue(String.class);

                    if(pwFromDB.equals(password)){
                        loginUsername.setError(null);

                        //store in sharedReference
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(userID, getID);
                        editor.putString(userName, getUsername);
                        editor.commit();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        loginPass.setError("Invalid Credentials");
                        loginPass.requestFocus();
                    }
                }else{
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void checkIsLogin(){
        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(userName)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }
    }
}