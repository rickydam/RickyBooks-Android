package com.example.rickydam.rickybooks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    String wantedFragmentName;
    String token = null;
    int backStackCount = 0;
    Stack<Integer> bottomNavigationPosition;
    int prevNavPosition = 0;
    Stack<String> titles;
    String prevTitle;
    Bundle detailsBundle;
    boolean popped = false;
    int poppedIndex = 0;
    String poppedTitle;
    String currentFragmentName;
    boolean fragmentChanges = true;

    static final int HOME = 0;
    static final int BUY = 1;
    static final int MESSAGES = 2;
    static final int SELL = 3;
    static final int PROFILE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationPosition = new Stack<>();
        titles = new Stack<>();

        BottomNavigationView bnv = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bnv);
        bnv.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Fragment homeFragment = new HomeFragment();
        final FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, homeFragment, "HomeFragment");
        transaction.commit();
        bottomNavigationPosition.push(prevNavPosition);
        String title = "RickyBooks";
        titles.push(title);
        prevTitle = title;
        currentFragmentName = "HomeFragment";

        fm.addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(backStackCount > fm.getBackStackEntryCount()) {
                    backStackCount--;
                    poppedTitle = titles.pop();
                    setTitle(poppedTitle);
                    poppedIndex = bottomNavigationPosition.pop();
                    setBottomNavigationItem(poppedIndex);
                    popped = true;
                }
                else {
                    if(popped) {
                        bottomNavigationPosition.push(poppedIndex);
                        titles.push(poppedTitle);
                    }
                    backStackCount++;
                    popped = false;
                }
            }
        });
    }

    private void setBottomNavigationItem(int index) {
        BottomNavigationView bnv = findViewById(R.id.navigation);
        bnv.getMenu().getItem(index).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment("HomeFragment");
                    break;
                case R.id.navigation_buy:
                    replaceFragment("BuyFragment");
                    break;
                case R.id.navigation_messages:
                    replaceFragment("ConversationsFragment");
                    break;
                case R.id.navigation_sell:
                    replaceFragment("SellFragment");
                    break;
                case R.id.navigation_profile:
                    replaceFragment("ProfileFragment");
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    private void setAppTitle(String title) {
        setTitle(title);
        titles.push(prevTitle);
        prevTitle = title;
    }

    public void replaceFragment(String fragmentName) {
        checkToken();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();

        if(fragmentName.equals("HomeFragment")) {
            setAppTitle("RickyBooks");
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = HOME;
            Fragment fragment = fm.findFragmentByTag("HomeFragment");
            if(fragment == null) {
                fragment = new HomeFragment();
            }
            fragmentChanges = false;
            if(!currentFragmentName.equals("HomeFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "HomeFragment");
                fragmentChanges = true;
            }
            currentFragmentName = "HomeFragment";
        }
        if(fragmentName.equals("BuyFragment")) {
            setAppTitle("Buy a textbook!");
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = BUY;
            Fragment fragment = fm.findFragmentByTag("BuyFragment");
            if(fragment == null) {
                fragment = new BuyFragment();
            }
            fragmentChanges = false;
            if(!currentFragmentName.equals("BuyFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "BuyFragment");
                fragmentChanges = true;
            }
            currentFragmentName = "BuyFragment";
        }
        if(fragmentName.equals("DetailsFragment")) {
            setAppTitle("Buy " + detailsBundle.get("Title"));
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = BUY;
            Fragment fragment = fm.findFragmentByTag("DetailsFragment");
            if(fragment == null) {
                fragment = new DetailsFragment();
                fragment.setArguments(detailsBundle);
            }
            fragmentChanges = false;
            if(!currentFragmentName.equals("DetailsFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "DetailsFragment");
                fragmentChanges = true;
            }
            currentFragmentName = "DetailsFragment";
        }
        if(fragmentName.equals("ConversationsFragment")) {
            setAppTitle("Conversations");
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = MESSAGES;
            if(token == null) {
                wantedFragmentName = "ConversationsFragment";
                Fragment fragment = fm.findFragmentByTag("AccountFragment");
                if(fragment == null) {
                    fragment = new AccountFragment();
                }
                fragmentChanges = false;
                if(!currentFragmentName.equals("ConversationsFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                    fragmentChanges = true;
                }
                currentFragmentName = "ConversationsFragment";
            }
            else {
                Fragment fragment = fm.findFragmentByTag("ConversationsFragment");
                if(fragment == null) {
                    fragment = new ConversationsFragment();
                }
                fragmentChanges = false;
                if(!currentFragmentName.equals("ConversationsFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "ConversationsFragment");
                    fragmentChanges = true;
                }
                currentFragmentName = "ConversationsFragment";
            }
        }
        if(fragmentName.equals("MessagesFragment")) {
            setAppTitle("Messages with ...");
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = MESSAGES;
            if(token == null) {
                wantedFragmentName = "MessagesFragment";
                Fragment fragment = fm.findFragmentByTag("AccountFragment");
                if(fragment == null) {
                    fragment = new AccountFragment();
                }
                fragmentChanges = false;
                if(!currentFragmentName.equals("MessagesFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                    fragmentChanges = true;
                }
                currentFragmentName = "MessagesFragment";
            }
            else {
                Fragment fragment = fm.findFragmentByTag("MessagesFragment");
                if(fragment == null) {
                    fragment = new MessagesFragment();
                }
                fragmentChanges = false;
                if(!currentFragmentName.equals("MessagesFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "MessagesFragment");
                    fragmentChanges = true;
                }
                currentFragmentName = "MessagesFragment";
            }
        }
        if(fragmentName.equals("SellFragment")) {
            setAppTitle("Sell a textbook!");
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = SELL;
            if(token == null) {
                wantedFragmentName = "SellFragment";
                Fragment fragment = fm.findFragmentByTag("AccountFragment");
                if(fragment == null) {
                    fragment = new AccountFragment();
                }
                fragmentChanges = false;
                if(!currentFragmentName.equals("SellFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                    fragmentChanges = true;
                }
                currentFragmentName = "SellFragment";
            }
            else {
                Fragment fragment = fm.findFragmentByTag("SellFragment");
                if(fragment == null) {
                    fragment = new SellFragment();
                }
                fragmentChanges = false;
                if(!currentFragmentName.equals("SellFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "SellFragment");
                    fragmentChanges = true;
                }
                currentFragmentName = "SellFragment";
            }
        }
        if(fragmentName.equals("ProfileFragment")) {
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = PROFILE;
            if(token == null) {
                setAppTitle("Profile");
                wantedFragmentName = "ProfileFragment";
                Fragment fragment = fm.findFragmentByTag("AccountFragment");
                if(fragment == null) {
                    fragment = new AccountFragment();
                }
                fragmentChanges = false;
                if(!currentFragmentName.equals("ProfileFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                    fragmentChanges = true;
                }
                currentFragmentName = "ProfileFragment";
            }
            else {
                SharedPreferences sharedPref = getSharedPreferences("com.rickydam.RickyBooks",
                        Context.MODE_PRIVATE);
                String name = sharedPref.getString("name", null);
                setAppTitle(name);
                Fragment fragment = fm.findFragmentByTag("ProfileFragment");
                if(fragment == null) {
                    fragment = new ProfileFragment();
                }
                fragmentChanges = false;
                if(!currentFragmentName.equals("ProfileFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "ProfileFragment");
                    fragmentChanges = true;
                }
                currentFragmentName = "ProfileFragment";
            }
        }
        if(fragmentName.equals("AccountFragment")) {
            bottomNavigationPosition.push(prevNavPosition);
            backStackCount++;
            Fragment fragment = fm.findFragmentByTag("AccountFragment");
            if(fragment == null) {
                fragment = new AccountFragment();
            }
            fragmentChanges = false;
            if(!currentFragmentName.equals("AccountFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                fragmentChanges = true;
            }
            currentFragmentName = "AccountFragment";
        }
        if(fragmentName.equals("RegisterFragment")) {
            setAppTitle("Create an account!");
            bottomNavigationPosition.push(prevNavPosition);
            backStackCount++;
            Fragment fragment = fm.findFragmentByTag("RegisterFragment");
            if(fragment == null) {
                fragment = new RegisterFragment();
            }
            fragmentChanges = false;
            if(!currentFragmentName.equals("RegisterFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "RegisterFragment");
                fragmentChanges = true;
            }
            currentFragmentName = "RegisterFragment";
        }
        if(fragmentName.equals("LoginFragment")) {
            setAppTitle("Log in to your account!");
            bottomNavigationPosition.push(prevNavPosition);
            backStackCount++;
            Fragment fragment = fm.findFragmentByTag("LoginFragment");
            if(fragment == null) {
                fragment = new LoginFragment();
            }
            fragmentChanges = false;
            if(!currentFragmentName.equals("LoginFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "LoginFragment");
                fragmentChanges = true;
            }
            currentFragmentName = "LoginFragment";
        }
        if(fragmentChanges) {
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public String getWantedFragmentName() {
        return wantedFragmentName;
    }

    public void setDetailsBundle(Bundle bundle) {
        detailsBundle = bundle;
    }

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
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("ConversationsFragment");
        if(fragment != null) {
            ((ConversationsFragment) fragment).clearConversations();
        }
        bottomNavigationPosition.clear();
        prevNavPosition = 0;
        backStackCount = 0;
        replaceFragment("HomeFragment");
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void checkToken() {
        SharedPreferences sharedPref = getSharedPreferences("com.rickydam.RickyBooks",
                Context.MODE_PRIVATE);
        token = sharedPref.getString("token", null);
    }
}
