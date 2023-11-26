package com.davidbelesp.mybookshelf.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.fragments.ConfigFragment;
import com.davidbelesp.mybookshelf.fragments.DatabaseFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConfigActivity extends AppCompatActivity {
    Toolbar configToolbar;

    DatabaseFragment dbFragment = new DatabaseFragment();
    ConfigFragment configFragment = new ConfigFragment();
    BottomNavigationView navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        loadFields();

        setEvents();

        setNavigationMenu();
    }

    private void setNavigationMenu() {

        navigationMenu = findViewById(R.id.navigationMenu);

        changeFragment(configFragment);


        navigationMenu.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.configNavButton:
                    changeFragment(configFragment);
                    break;
                case R.id.databaseNavButton:
                    changeFragment(dbFragment);
                    break;
            }
            return true;
        });

    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFrame, fragment)
                .commit();

    }

    private void loadFields() {
        this.configToolbar = (Toolbar) findViewById(R.id.configToolbar);
    }

    private void setEvents() {
        configToolbar.setNavigationOnClickListener(v -> finish());
    }

}