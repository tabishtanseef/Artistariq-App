package com.tabishtanseef.artistariq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tabishtanseef.artistariq.Database.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    Button callSignUp, login_btn;
    ImageView image;
    ProgressBar progressBar;
    TextView logoText, sloganText;
    TextInputLayout username, password;
    TextInputEditText usernameEdittext,passwordEdittext;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logoimage);
        logoText = findViewById(R.id.welcome);
        sloganText = findViewById(R.id.signintext);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login);
        rememberMe = findViewById(R.id.rememberMe);
        usernameEdittext = findViewById(R.id.usernameEdittext);
        passwordEdittext = findViewById(R.id.passwordEdittext);

        SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
        if(sessionManager.checkRememberMe()){
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
            usernameEdittext.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONCONTACT));
            passwordEdittext.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Login.this,SignUp.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logoText,"welcome");
                pairs[2] = new Pair<View,String>(sloganText,"signintitle");
                pairs[3] = new Pair<View,String>(username,"username");
                pairs[4] = new Pair<View,String>(password,"password");
                pairs[5] = new Pair<View,String>(login_btn,"go");
                pairs[6] = new Pair<View,String>(callSignUp,"switch");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                    startActivity(intent,options.toBundle());
                }
            }
        });
    }

    private Boolean validateUserName(){
        String val = username.getEditText().getText().toString();
        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();
        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void onLogin(View v){

        if(!isConnected(this)){
            showCustomDialog();
        }
        else {

            if (!validatePassword() | !validateUserName()) {
                return;
            } else {
                isUser();
            }
        }

    }


    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn!=null && wifiConn.isConnected()) || (mobConn!=null && mobConn.isConnected())){
            return true;
        }else{
            return false;
        }
    }

    private void showCustomDialog() {
        //Toast.makeText(this, "Data Has Been Updated", Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Please connect to the Internet")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void isUser(){
        final String userEnteredUsername = username.getEditText().getText().toString();
        final String userEnteredPassword = password.getEditText().getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("phone").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if(passwordDB.equals(userEnteredPassword)){

                        username.setError(null);
                        username.setErrorEnabled(false);

                        String nameDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String emailDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String phoneDB = dataSnapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String usernameDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);

                        if(rememberMe.isChecked()){
                            SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
                            sessionManager.createRememberMeSession(phoneDB,passwordDB);
                        }

                        SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_USERSESSION);
                        sessionManager.createLoginSession(nameDB,usernameDB,phoneDB,emailDB,passwordDB);

                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("name",nameDB);
                        intent.putExtra("username",usernameDB);
                        intent.putExtra("phone",phoneDB);
                        intent.putExtra("email",emailDB);
                        intent.putExtra("password",passwordDB);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }else{
                    username.setError("No Such User Exists");
                    username.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onforget(View view){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
        startActivity(intent);
    }

    public void skip(View v){
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}