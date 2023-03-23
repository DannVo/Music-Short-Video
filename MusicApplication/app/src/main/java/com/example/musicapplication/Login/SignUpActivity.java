package com.example.musicapplication.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.Adapter.HelperClass;
import com.example.musicapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText sigupName, signupUsername,  signupEmail,signupPass;
    TextView reLogin;
    Button signupButt;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sigupName = findViewById(R.id.etSigName);
        signupEmail = findViewById(R.id.etSigEmail);
        signupUsername = findViewById(R.id.etSigUser);
        signupPass = findViewById(R.id.etSigPass);
        signupButt = findViewById(R.id.btnSignup);
        reLogin = findViewById(R.id.tvReLogin);

        signupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("user_account");

                String name = sigupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String pass = signupPass.getText().toString();

                String user_id = generateID(name,email);
                HelperClass helperClass = new HelperClass(user_id, name, email, username, pass);
                reference.child(username).setValue(helperClass);

                Toast.makeText(SignUpActivity.this,"You have sign up successfully!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        reLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class );
                startActivity(intent);
            }
        });
    }

    private String generateID(String username, String email){
        String name = username.toUpperCase().substring(0,1);
        String user_email = email.split("@")[0].toUpperCase().substring(0,3);
        String mix_user = name+user_email;
        String codeID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        StringBuilder sb = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            int index = (int)(codeID.length() * Math.random());

            sb.append(codeID.charAt(index));
        }
        String new_generateID = mix_user + sb.toString();
        return new_generateID;

    }
}