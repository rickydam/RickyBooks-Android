package com.example.rickydam.rickybooks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    BuyFragment buyFragment;
    MessagesFragment messagesFragment;
    SellFragment sellFragment;
    ProfileFragment profileFragment;
    Fragment currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    setTitle("RickyBooks");
                    if(currentFragment != homeFragment) {
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(currentFragment);
                        fragmentTransaction.show(homeFragment);
                        fragmentTransaction.commit();
                        currentFragment = homeFragment;
                    }
                    return true;

                case R.id.navigation_buy:
                    setTitle("Buy a textbook!");
                    if(currentFragment != buyFragment) {
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(currentFragment);
                        fragmentTransaction.show(buyFragment);
                        fragmentTransaction.commit();
                        currentFragment = buyFragment;
                    }
                    return true;

                case R.id.navigation_messages:
                    setTitle("Messages");
                    if(currentFragment != messagesFragment) {
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(currentFragment);
                        fragmentTransaction.show(messagesFragment);
                        fragmentTransaction.commit();
                        currentFragment = messagesFragment;
                    }
                    return true;

                case R.id.navigation_sell:
                    setTitle("Sell a textbook!");
                    if(currentFragment != sellFragment) {
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(currentFragment);
                        fragmentTransaction.show(sellFragment);
                        fragmentTransaction.commit();
                        currentFragment = sellFragment;
                    }
                    return true;

                case R.id.navigation_profile:
                    setTitle("Profile");
                    if(currentFragment != profileFragment) {
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(currentFragment);
                        fragmentTransaction.show(profileFragment);
                        fragmentTransaction.commit();
                        currentFragment = profileFragment;
                    }
                    return true;

                default:
                    return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        homeFragment = new HomeFragment();
        buyFragment = new BuyFragment();
        messagesFragment = new MessagesFragment();
        sellFragment = new SellFragment();
        profileFragment = new ProfileFragment();

        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, homeFragment);
        currentFragment = homeFragment;

        fragmentTransaction.add(R.id.fragment_container, buyFragment);
        fragmentTransaction.hide(buyFragment);

        fragmentTransaction.add(R.id.fragment_container, messagesFragment);
        fragmentTransaction.hide(messagesFragment);

        fragmentTransaction.add(R.id.fragment_container, sellFragment);
        fragmentTransaction.hide(sellFragment);

        fragmentTransaction.add(R.id.fragment_container, profileFragment);
        fragmentTransaction.hide(profileFragment);

        fragmentTransaction.commit();
    }
}
