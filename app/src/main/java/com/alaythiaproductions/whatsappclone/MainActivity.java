package com.alaythiaproductions.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

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
}
