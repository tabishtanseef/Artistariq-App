package com.tabishtanseef.artistariq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tabishtanseef.artistariq.Database.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ForgetPassword extends AppCompatActivity {

    ProgressBar progressBar;
    private Button nextBtn;
    private TextInputLayout phoneNumberTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);
        progressBar = findViewById(R.id.progress_bar);
        phoneNumberTextField = findViewById(R.id.forget_password_phone_number);
        nextBtn = findViewById(R.id.next);
        progressBar.setVisibility(View.GONE);
    }

    private Boolean validateContact(){
        String val = phoneNumberTextField.getEditText().getText().toString();
        if(val.isEmpty()){
            phoneNumberTextField.setError("Field cannot be empty");
            return false;
        }else{
            phoneNumberTextField.setError(null);
            return true;
        }
    }

    public void verifyPhone(View view){

        if(!isConnected(this)){
            showCustomDialog();
        }
        else {

            if (!validateContact()) {
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                isUser();
            }
        }
    }

    private boolean isConnected(ForgetPassword forgetPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) forgetPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
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
        final String userEnteredPhoneNo = phoneNumberTextField.getEditText().getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("phone").equalTo(userEnteredPhoneNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    phoneNumberTextField.setError(null);
                    phoneNumberTextField.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(),VerifyOTP.class);
                    intent.putExtra("phone",userEnteredPhoneNo);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.GONE);

                }else{
                    progressBar.setVisibility(View.GONE);
                    phoneNumberTextField.setError("No Such User Exists");
                    phoneNumberTextField.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}