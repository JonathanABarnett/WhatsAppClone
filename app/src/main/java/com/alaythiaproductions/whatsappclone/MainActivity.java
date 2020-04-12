package com.alaythiaproductions.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager2 viewPager;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        /**
         *
         */
         mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("What's Up");

        viewPager = findViewById(R.id.main_tabs_pager);
        viewPager.setAdapter(new TabsPagerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.main_tabs);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Chats");
                            tab.setIcon(R.drawable.ic_check);
                            break;
                        case 1:
                            tab.setText("Groups");
                            tab.setIcon(R.drawable.ic_paint);
                            break;
                        case 2:
                            tab.setText("Contacts");
                            tab.setIcon(R.drawable.ic_pending);
                            break;
                    }
                }
        );
        tabLayoutMediator.attach();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {
            sendUserToLoginActivity();
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
    private void sendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_logout_option) {
            mAuth.signOut();
            sendUserToLoginActivity();
        }
        if (item.getItemId() == R.id.main_settings_option) {
            sendUserToSettingsActivity();
        }
        if (item.getItemId() == R.id.main_find_friends_option) {

        }

        return true;
    }
}
