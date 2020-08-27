package com.tabishtanseef.artistariq;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tabishtanseef.artistariq.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {
    TextInputLayout newPassword, confirmPassword;
    Button updateBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_new_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        updateBtn = findViewById(R.id.updateBtn);
        progressBar = findViewById(R.id.progress_bar);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected(SetNewPassword.this)) {
                    showCustomDialog();
                } else {
                    if (!validatePassword() | !validateConfirmPassword()) {
                        return;
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        String _newPassword = newPassword.getEditText().getText().toString();
                        String _phoneNo = getIntent().getStringExtra("phone");
                        //Toast.makeText(SetNewPassword.this, _newPassword, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SetNewPassword.this, _phoneNo, Toast.LENGTH_SHORT).show();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                        reference.child(_phoneNo).child("password").setValue(_newPassword);
                        startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccessMessage.class));
                        finish();
                    }
                }
            }
        });

    }

    private Boolean validatePassword() {
        String val = newPassword.getEditText().getText().toString();

        if (val.isEmpty()) {
            newPassword.setError("Field cannot be empty");
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String val = confirmPassword.getEditText().getText().toString();

        if (val.isEmpty()) {
            confirmPassword.setError("Field cannot be empty");
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }


    private boolean isConnected(SetNewPassword setPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) setPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobConn != null && mobConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        //Toast.makeText(this, "Data Has Been Updated", Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPassword.this);
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

}