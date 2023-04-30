package com.example.musicapplication.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.Adapter.HelperClass;
import com.example.musicapplication.BuilderPattern.Notification;
import com.example.musicapplication.BuilderPattern.NotificationBuilder;
import com.example.musicapplication.BuilderPattern.NotificationConcreteBuilder;
import com.example.musicapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText sigupName, signupUsername,  signupEmail,signupPass;
    TextView reLogin, title, content;
    Button signupButton, cancelButton, successButton, dangerButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sigupName = findViewById(R.id.etSigName);
        signupEmail = findViewById(R.id.etSigEmail);
        signupUsername = findViewById(R.id.etSigUser);
        signupPass = findViewById(R.id.etSigPass);
        signupButton = findViewById(R.id.register_button);
        cancelButton = findViewById(R.id.cancel_button);
        reLogin = findViewById(R.id.tvReLogin);

        linearLayout = findViewById(R.id.Dialog);
        title = findViewById(R.id.titleDialog);
        content = findViewById(R.id.contentDialog);
        dangerButton = findViewById(R.id.btn_danger);
        successButton = findViewById(R.id.btn_success);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("Alert Dialog");
                content.setText("Are you sure, you want to delete all information?");
                successButton.setText("YES");
                dangerButton.setText("NO");

                NotificationBuilder notificationBuilder = new NotificationConcreteBuilder()
                        .setTitle(title)
                        .setContent(content)
                        .setbtnSuccess(successButton)
                        .setbtnDanger(dangerButton);

                notificationBuilder.create();
                linearLayout.setVisibility(View.VISIBLE);
                notificationBuilder.getBtnSuccess().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("h","Successfully");
                        //Toast.makeText(this.getActivity(), "Item 1 clicked", Toast.LENGTH_SHORT).show();
                        sigupName.setText("");
                        signupEmail.setText("");
                        signupUsername.setText("");
                        signupPass.setText("");
                        linearLayout.setVisibility(View.GONE);
                    }
                });

                notificationBuilder.getBtnDanger().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("h","Danger");
                        //Toast.makeText(this.getActivity(), "Item 1 clicked", Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.GONE);
                    }
                });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
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

                title.setText("Successful!");
                content.setText("Your information has been saved.");
                successButton.setText("OK");

                NotificationBuilder notificationBuilder = new NotificationConcreteBuilder()
                        .setTitle(title)
                        .setContent(content)
                        .setbtnSuccess(successButton);

                notificationBuilder.create();
                linearLayout.setVisibility(View.VISIBLE);

                notificationBuilder.getBtnSuccess().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("h","Danger");
                        //Toast.makeText(this.getActivity(), "Item 1 clicked", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUpActivity.this,"You have sign up successfully!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        linearLayout.setVisibility(View.GONE);
                    }
                });
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