package com.appex.edhelp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appex.edhelp.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import chipset.potato.Potato;

public class LoginActivity extends AppCompatActivity {

    public static Firebase ref;
    String userID;
    RelativeLayout relativeLayout;
    Button signupButton, loginButton, createAccountButton;
    EditText emailEditText;
    EditText passwordEditText, retypeEditText;
    ProgressDialog mProgressDialog;
    TextView passwordResetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://edhelp.firebaseio.com");
        AuthData authData = ref.getAuth();
        if (authData != null)
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        relativeLayout = (RelativeLayout) findViewById(R.id.login_layout);
        loginButton = (Button) findViewById(R.id.login_button);
        signupButton = (Button) findViewById(R.id.signup_button);
        emailEditText = (EditText) findViewById(R.id.email_textview);
        passwordEditText = (EditText) findViewById(R.id.password_textview);
        retypeEditText = (EditText) findViewById(R.id.retype_textview);
        createAccountButton = (Button) findViewById(R.id.create_button);
        passwordResetTextView = (TextView) findViewById(R.id.password_reset_textview);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(true);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mProgressDialog.isShowing())
                    mProgressDialog.show();
                signupButton.setVisibility(View.GONE);
                ref.authWithPassword(emailEditText.getText().toString(), passwordEditText.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Snackbar.make(relativeLayout, "Logged In Successfully", Snackbar.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        userID = authData.getUid();
                        Log.d("USERID", userID);
                        Potato.potate(getApplicationContext()).Preferences().putSharedPreference("uid", userID);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        mProgressDialog.dismiss();
                        Snackbar.make(relativeLayout, "Wrong Credentials", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retypeEditText.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                createAccountButton.setVisibility(View.GONE);
                signupButton.setVisibility(View.VISIBLE);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mProgressDialog.isShowing())
                    mProgressDialog.show();
                if (passwordEditText.getText().toString().equals(retypeEditText.getText().toString())) {
                    ref.createUser(emailEditText.getText().toString(), passwordEditText.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> stringObjectMap) {
                            Snackbar.make(relativeLayout, "Your account has been created", Snackbar.LENGTH_SHORT).show();
                            signupButton.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                            retypeEditText.setVisibility(View.GONE);
                            mProgressDialog.dismiss();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            mProgressDialog.dismiss();
                            Snackbar.make(relativeLayout, "Something went wrong!", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    mProgressDialog.dismiss();
                    Snackbar.make(relativeLayout, "Passwords don't match!", Snackbar.LENGTH_SHORT).show();

                }

            }
        });

        passwordResetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginButton.getVisibility() == View.VISIBLE)
                    loginButton.setVisibility(View.GONE);
                if (passwordEditText.getVisibility() == View.VISIBLE)
                    passwordEditText.setVisibility(View.GONE);
                if (retypeEditText.getVisibility() == View.VISIBLE)
                    retypeEditText.setVisibility(View.GONE);
                if (createAccountButton.getVisibility() == View.VISIBLE)
                    createAccountButton.setVisibility(View.GONE);
                if (signupButton.getVisibility() == View.VISIBLE)
                    signupButton.setVisibility(View.GONE);

                ref.resetPassword(emailEditText.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Snackbar.make(relativeLayout, "Password Reset Mail Sent", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Snackbar.make(relativeLayout, "Please Try Again", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
