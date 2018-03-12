package com.example.rickydam.rickybooks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    BuyFragment buyFragment;
    MessagesFragment messagesFragment;
    SellFragment sellFragment;
    ProfileFragment profileFragment;
    RegisterFragment registerFragment;
    LoginFragment loginFragment;
    AccountFragment accountFragment;
    Fragment currentFragment;
    String currentFragmentName = "";
    String previousFragmentName = "";
    String token = null;

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
        registerFragment = new RegisterFragment();
        loginFragment = new LoginFragment();
        accountFragment = new AccountFragment();

        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, homeFragment);
        currentFragment = homeFragment;
        currentFragmentName = "HomeFragment";

        fragmentTransaction.add(R.id.fragment_container, buyFragment);
        fragmentTransaction.hide(buyFragment);

        fragmentTransaction.add(R.id.fragment_container, messagesFragment);
        fragmentTransaction.hide(messagesFragment);

        fragmentTransaction.add(R.id.fragment_container, sellFragment);
        fragmentTransaction.hide(sellFragment);

        fragmentTransaction.add(R.id.fragment_container, profileFragment);
        fragmentTransaction.hide(profileFragment);

        fragmentTransaction.add(R.id.fragment_container, registerFragment);
        fragmentTransaction.hide(registerFragment);

        fragmentTransaction.add(R.id.fragment_container, loginFragment);
        fragmentTransaction.hide(loginFragment);

        fragmentTransaction.add(R.id.fragment_container, accountFragment);
        fragmentTransaction.hide(accountFragment);

        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment("HomeFragment");
                    return true;
                case R.id.navigation_buy:
                    replaceFragment("BuyFragment");
                    return true;
                case R.id.navigation_messages:
                    replaceFragment("MessagesFragment");
                    return true;
                case R.id.navigation_sell:
                    replaceFragment("SellFragment");
                    return true;
                case R.id.navigation_profile:
                    replaceFragment("ProfileFragment");
                    return true;
                default:
                    return false;
            }
        }
    };

    public String getToken() {
        return token;
    }

    public void logout() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "com.rickydam.RickyBooks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", null);
        editor.apply();
        token = null;
    }

    public void checkToken() {
        SharedPreferences sharedPref = getSharedPreferences("com.rickydam.RickyBooks",
                Context.MODE_PRIVATE);
        token = sharedPref.getString("token", null);
    }

    public String getPreviousFragmentName() {
        return previousFragmentName;
    }

    @SuppressLint("NewApi")
    public void replaceFragment(String fragmentName) {
        checkToken();
        if(Objects.equals(fragmentName, "HomeFragment")) {
            previousFragmentName = currentFragmentName;
            currentFragmentName = "HomeFragment";
            if(currentFragment != homeFragment) {
                setTitle("RickyBooks");
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(currentFragment);
                fragmentTransaction.show(homeFragment);
                fragmentTransaction.commit();
                currentFragment = homeFragment;
            }
        }
        if(Objects.equals(fragmentName, "BuyFragment")) {
            previousFragmentName = currentFragmentName;
            currentFragmentName = "BuyFragment";
            if(currentFragment != buyFragment) {
                setTitle("Buy a textbook!");
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(currentFragment);
                fragmentTransaction.show(buyFragment);
                fragmentTransaction.commit();
                currentFragment = buyFragment;
            }
        }
        if(Objects.equals(fragmentName, "MessagesFragment")) {
            previousFragmentName = currentFragmentName;
            currentFragmentName = "MessagesFragment";
            setTitle("Messages");
            if(token != null) {
                if(currentFragment != messagesFragment) {
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(currentFragment);
                    fragmentTransaction.show(messagesFragment);
                    fragmentTransaction.commit();
                    currentFragment = messagesFragment;
                }
            }
            else {
                previousFragmentName = currentFragmentName;
                currentFragmentName = "AccountFragment";
                if(currentFragment != accountFragment) {
                    replaceFragment("AccountFragment");
                }
            }
        }
        if(Objects.equals(fragmentName, "SellFragment")) {
            previousFragmentName = currentFragmentName;
            currentFragmentName = "SellFragment";
            setTitle("Sell a textbook!");
            if(token != null) {
                if(currentFragment != sellFragment) {
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(currentFragment);
                    fragmentTransaction.show(sellFragment);
                    fragmentTransaction.commit();
                    currentFragment = sellFragment;
                }
            }
            else {
                previousFragmentName = currentFragmentName;
                currentFragmentName = "AccountFragment";
                if(currentFragment != accountFragment) {
                    replaceFragment("AccountFragment");
                }
            }
        }
        if(Objects.equals(fragmentName, "ProfileFragment")) {
            previousFragmentName = currentFragmentName;
            currentFragmentName = "ProfileFragment";
            setTitle("Profile");
            if(token != null) {
                if(currentFragment != profileFragment) {
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(currentFragment);
                    fragmentTransaction.show(profileFragment);
                    fragmentTransaction.commit();
                    currentFragment = profileFragment;
                }
            }
            else {
                previousFragmentName = currentFragmentName;
                currentFragmentName = "AccountFragment";
                if(currentFragment != accountFragment) {
                    replaceFragment("AccountFragment");
                }
            }
        }
        if(Objects.equals(fragmentName, "RegisterFragment")) {
            setTitle("Create an account!");
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(registerFragment);
            fragmentTransaction.commit();
            currentFragment = registerFragment;
        }
        if(Objects.equals(fragmentName, "LoginFragment")) {
            setTitle("Log in to your account!");
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(loginFragment);
            fragmentTransaction.commit();
            currentFragment = loginFragment;
        }
        if(Objects.equals(fragmentName, "AccountFragment")) {
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(accountFragment);
            fragmentTransaction.commit();
            currentFragment = accountFragment;
        }
    }
}
