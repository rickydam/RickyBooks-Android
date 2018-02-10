package com.example.rickydam.rickybooks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("RickyBooks");
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.frame, homeFragment);
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_buy:
                    setTitle("Buy a textbook!");
                    BuyFragment buyFragment = new BuyFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.frame, buyFragment);
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_messages:
                    setTitle("Messages");
                    MessagesFragment messagesFragment = new MessagesFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.frame, messagesFragment);
                    fragmentTransaction3.commit();
                    return true;
                case R.id.navigation_sell:
                    setTitle("Sell a textbook!");
                    SellFragment sellFragment = new SellFragment();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.frame, sellFragment);
                    fragmentTransaction4.commit();
                    return true;
                case R.id.navigation_profile:
                    setTitle("Profile");
                    ProfileFragment profileFragment = new ProfileFragment();
                    FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction5.replace(R.id.frame, profileFragment);
                    fragmentTransaction5.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTitle("RickyBooks");
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction0 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction0.replace(R.id.frame, homeFragment);
        fragmentTransaction0.commit();
    }

}
