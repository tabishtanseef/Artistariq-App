package com.tabishtanseef.artistariq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.WindowManager;

import com.tabishtanseef.artistariq.UserOptions.artistariq;
import com.tabishtanseef.artistariq.UserOptions.dashboard;
import com.tabishtanseef.artistariq.UserOptions.profile;
import com.tabishtanseef.artistariq.UserOptions.resume;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Dashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_web, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new artistariq()).commit();
        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.bottom_nav_web:
                        fragment = new artistariq();
                        break;
                    case R.id.bottom_nav_dashboard:
                        fragment = new dashboard();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new profile();
                        break;
                    case R.id.bottom_resume:
                        fragment = new resume();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

    }
}