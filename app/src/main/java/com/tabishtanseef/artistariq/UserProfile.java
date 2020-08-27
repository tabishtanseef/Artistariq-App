package com.tabishtanseef.artistariq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tabishtanseef.artistariq.Database.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserProfile extends AppCompatActivity {

    TextInputLayout fullname, email, phone, password;
    TextView fullNameLabel, usernameLable;

    String user_username, user_name, user_email, user_password, user_phone;
    SessionManager sessionManager;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        fullname = findViewById(R.id.name_profile);
        email = findViewById(R.id.email_profile);
        phone = findViewById(R.id.phone_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.full_name);
        usernameLable = findViewById(R.id.usernameLable);

        showAllUserData();

    }

    private void showAllUserData(){
        /*Intent intent = getIntent();
        user_username = intent.getStringExtra("username");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");*/

        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USERSESSION);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();
        String user_name = userDetails.get(SessionManager.KEY_FULLNAME);
        String user_email = userDetails.get(SessionManager.KEY_EMAIL);
        String user_username = userDetails.get(SessionManager.KEY_USERNAME);
        String user_phone = userDetails.get(SessionManager.KEY_CONTACT);
        String user_password = userDetails.get(SessionManager.KEY_PASSWORD);

        fullNameLabel.setText(user_name);
        usernameLable.setText(user_username);
        email.getEditText().setText(user_email);
        fullname.getEditText().setText(user_name);
        phone.getEditText().setText(user_phone);
        password.getEditText().setText(user_password);

    }

    public void update(View view){
        if(isNameChanged() || isPasswordChanged() ){
            Toast.makeText(this, "Data Has Been Updated", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Data is same and Cannot be Updated", Toast.LENGTH_LONG).show();
    }

    private boolean isNameChanged() {
        if(!user_name.equals(fullname.getEditText().getText().toString())){
            reference.child(user_username).child("name").setValue(fullname.getEditText().getText().toString());
            user_name = fullname.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isPasswordChanged() {
        if(!user_password.equals(password.getEditText().getText().toString())){
            reference.child(user_username).child("password").setValue(password.getEditText().getText().toString());
            user_password = password.getEditText().getText().toString();
            return true;
        }else{
            Toast.makeText(this, "vdfgdfg", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void logout(View view){
        sessionManager.logoutUserFromSession();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
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