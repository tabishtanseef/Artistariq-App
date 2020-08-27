package com.tabishtanseef.artistariq;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName, regUsername, regEmail, regContact, regPassword;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    Button callLogin, signup_btn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        callLogin = findViewById(R.id.login_screen);
        image = findViewById(R.id.logoimage);
        logoText = findViewById(R.id.welcometext);
        sloganText = findViewById(R.id.signuptext);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signup_btn = findViewById(R.id.signup);

        regName = findViewById(R.id.name);
        regUsername = findViewById(R.id.username);
        regPassword = findViewById(R.id.password);
        regEmail = findViewById(R.id.email);
        regContact = findViewById(R.id.phone);



        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (SignUp.this,Login.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logoText,"logo_text");
                pairs[2] = new Pair<View,String>(sloganText,"signintitle");
                pairs[3] = new Pair<View,String>(username,"username");
                pairs[4] = new Pair<View,String>(password,"password");
                pairs[5] = new Pair<View,String>(signup_btn,"go");
                pairs[6] = new Pair<View,String>(callLogin,"switch");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
                    startActivity(intent,options.toBundle());
                }
            }
        });
    }

    private Boolean validateName(){
        String val = regName.getEditText().getText().toString();
        if(val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }else{
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUserName(){
        String val = regUsername.getEditText().getText().toString();
        String nowhitespace = "\\\\A\\\\w{4,20}\\\\z";
        if(val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
        }else if(val.length()>=15){
            regUsername.setError("Username too long");
            return false;
        }else if(val.matches(nowhitespace)){
            regUsername.setError("White Spaces are not allowed");
            return false;
        }else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";
        if(val.isEmpty()){
            regEmail.setError("Field cannot be empty");
            return false;
        }else if (val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateContact(){
        String val = regContact.getEditText().getText().toString();
        if(val.isEmpty()){
            regContact.setError("Field cannot be empty");
            return false;
        }else{
            regContact.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if(val.isEmpty()){
            regPassword.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(View v){
        if(!validateName() | !validatePassword() | !validateContact() | !validateEmail() | ! validateUserName()){
            return;
        }else {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");

            String name = regName.getEditText().getText().toString();
            String username = regUsername.getEditText().getText().toString();
            String email = regEmail.getEditText().getText().toString();
            String contact = regContact.getEditText().getText().toString();
            String password = regPassword.getEditText().getText().toString();

            Intent intent = new Intent(getApplicationContext(),verifyPhone.class);
            intent.putExtra("fullname",name);
            intent.putExtra("username",username);
            intent.putExtra("email",email);
            intent.putExtra("phoneNo",contact);
            intent.putExtra("password",password);
            startActivity(intent);
            finish();

            //UserHelperClass helperClass = new UserHelperClass(name, username, email, contact, password);
            //reference.child(username).setValue(helperClass);

            //Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getApplicationContext(),Login.class);
            //startActivity(intent);
            //finish();

        }
    }

}