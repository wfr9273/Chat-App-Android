package com.example.android.whatsappclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    Button signupLoginButton;
    TextView signupLoginTextView;
    boolean loginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText) findViewById(R.id.usernameText);
        passwordEditText = (EditText) findViewById(R.id.passwordText);
        signupLoginButton = (Button) findViewById(R.id.signupLoginButton);
        signupLoginTextView = (TextView) findViewById(R.id.signupLoginText);
        loginMode = true;

        if (ParseUser.getCurrentUser() != null)
            redirect();

        signupLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginMode) {
                    signupLoginButton.setText("Sign Up");
                    signupLoginTextView.setText("or, Login");
                } else {
                    signupLoginButton.setText("Login");
                    signupLoginTextView.setText("or, Sign Up");
                }
                loginMode = !loginMode;
            }
        });

        signupLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (loginMode) {
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                redirect();
                            } else
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                                redirect();
                            } else
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void redirect() {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }
}
