package com.rickybooks.rickybooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import com.rickybooks.rickybooks.Fragments.AccountFragment;
import com.rickybooks.rickybooks.Fragments.BuyFragment;
import com.rickybooks.rickybooks.Fragments.DetailsFragment;
import com.rickybooks.rickybooks.Fragments.HomeFragment;
import com.rickybooks.rickybooks.Fragments.ConversationsFragment;
import com.rickybooks.rickybooks.Fragments.LoginFragment;
import com.rickybooks.rickybooks.Fragments.MessagesFragment;
import com.rickybooks.rickybooks.Fragments.ProfileFragment;
import com.rickybooks.rickybooks.Fragments.RegisterFragment;
import com.rickybooks.rickybooks.Fragments.SellFragment;
import com.rickybooks.rickybooks.Other.BottomNavigationViewHelper;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private String wantedFragmentName;
    private String token = null;
    private int backStackCount = 0;
    private Stack<Integer> bottomNavigationPosition;
    private int prevNavPosition = 0;
    private Stack<String> titles;
    private String prevTitle;
    private Bundle detailsBundle;
    private boolean popped = false;
    private int poppedIndex = 0;
    private int peekIndex = 0;
    private String poppedTitle;
    private String currentFragmentName;
    private Stack<String> fragmentNames;
    private boolean hasFragmentChanges = true;
    private String poppedFragmentName;
    private boolean justLoggedIn = false;
    private String notificationConversationId = null;
    private boolean actionMode = false;
    private ActionMode mode = null;

    private static final int HOME = 0;
    private static final int BUY = 1;
    private static final int MESSAGES = 2;
    private static final int SELL = 3;
    private static final int PROFILE = 4;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getStringExtra("conversation_id") != null) {
            notificationConversationId = intent.getStringExtra("conversation_id");
            replaceFragment("ConversationsFragment");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bottomNavigationPosition = new Stack<>();
        titles = new Stack<>();
        fragmentNames = new Stack<>();

        BottomNavigationView bnv = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bnv);
        bnv.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Fragment homeFragment = new HomeFragment();
        final FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, homeFragment, "HomeFragment");
        transaction.commit();

        String title = "RickyBooks";
        titles.push(title);
        prevTitle = title;
        currentFragmentName = "HomeFragment";
        fragmentNames.push(currentFragmentName);

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(backStackCount > fm.getBackStackEntryCount()) {
                    backStackCount--;
                    poppedTitle = titles.pop();
                    if(!justLoggedIn) {
                        setTitle(poppedTitle);
                    }
                    if(poppedTitle.equals("Profile") && justLoggedIn) {
                        justLoggedIn = false;
                    }
                    poppedIndex = bottomNavigationPosition.pop();
                    prevNavPosition = poppedIndex;
                    if(bottomNavigationPosition.size() >= 1) {
                        peekIndex = bottomNavigationPosition.peek();
                    }
                    else {
                        peekIndex = poppedIndex;
                    }
                    setBottomNavigationItem(poppedIndex);
                    popped = true;
                    poppedFragmentName = fragmentNames.pop();
                    currentFragmentName = poppedFragmentName;
                }
                else {
                    if(popped) {
                        titles.push(poppedTitle);
                        fragmentNames.push(poppedFragmentName);
                    }
                    backStackCount++;
                    popped = false;
                }
            }
        });
    }

    public void setBottomNavigationItem(int index) {
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

    public void setAppTitle(String title) {
        setTitle(title);
        titles.push(prevTitle);
        prevTitle = title;
    }

    public void replaceFragment(String fragmentName) {
        if(actionMode) {
            mode.finish();
        }
        checkToken();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();

        if(fragmentName.equals("HomeFragment")) {
            setAppTitle("RickyBooks");
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = HOME;
            Fragment fragment = fm.findFragmentByTag("HomeFragment");
            if(fragment == null) {
                fragment = new HomeFragment();
            }
            hasFragmentChanges = false;
            if(!currentFragmentName.equals("HomeFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "HomeFragment");
                hasFragmentChanges = true;
                fragmentNames.push(currentFragmentName);
            }
            currentFragmentName = "HomeFragment";
        }
        if(fragmentName.equals("BuyFragment")) {
            setAppTitle("Buy a textbook!");
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = BUY;
            Fragment fragment = fm.findFragmentByTag("BuyFragment");
            if(fragment == null) {
                fragment = new BuyFragment();
            }
            hasFragmentChanges = false;
            if(!currentFragmentName.equals("BuyFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "BuyFragment");
                hasFragmentChanges = true;
                fragmentNames.push(currentFragmentName);
            }
            currentFragmentName = "BuyFragment";
        }
        if(fragmentName.equals("DetailsFragment")) {
            setAppTitle("Buy " + detailsBundle.get("Title"));
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = BUY;
            Fragment fragment = fm.findFragmentByTag("DetailsFragment");
            if(fragment == null) {
                fragment = new DetailsFragment();
            }
            hasFragmentChanges = false;
            fragment.setArguments(detailsBundle);
            if(!currentFragmentName.equals("DetailsFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "DetailsFragment");
                hasFragmentChanges = true;
                fragmentNames.push(currentFragmentName);
            }
            currentFragmentName = "DetailsFragment";
        }
        if(fragmentName.equals("ConversationsFragment")) {
            setBottomNavigationItem(MESSAGES);
            setAppTitle("Conversations");
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = MESSAGES;
            if(token == null) {
                wantedFragmentName = "ConversationsFragment";
                Fragment fragment = fm.findFragmentByTag("AccountFragment");
                if(fragment == null) {
                    fragment = new AccountFragment();
                }
                hasFragmentChanges = false;
                if(!currentFragmentName.equals("ConversationsFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                    hasFragmentChanges = true;
                    fragmentNames.push(currentFragmentName);
                }
            }
            else {
                Fragment fragment = fm.findFragmentByTag("ConversationsFragment");
                if(fragment == null) {
                    fragment = new ConversationsFragment();
                }
                hasFragmentChanges = false;
                if(!currentFragmentName.equals("ConversationsFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "ConversationsFragment");
                    hasFragmentChanges = true;
                    fragmentNames.push(currentFragmentName);
                }
                if(notificationConversationId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("notification_conversation_id", notificationConversationId);
                    fragment.setArguments(bundle);
                }
            }
            currentFragmentName = "ConversationsFragment";
        }
        if(fragmentName.equals("MessagesFragment")) {
            setAppTitle("Messages");
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = MESSAGES;
            if(token == null) {
                wantedFragmentName = "MessagesFragment";
                Fragment fragment = fm.findFragmentByTag("AccountFragment");
                if(fragment == null) {
                    fragment = new AccountFragment();
                }
                hasFragmentChanges = false;
                if(!currentFragmentName.equals("MessagesFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                    hasFragmentChanges = true;
                    fragmentNames.push(currentFragmentName);
                }
            }
            else {
                Fragment fragment = fm.findFragmentByTag("MessagesFragment");
                if(fragment == null) {
                    fragment = new MessagesFragment();
                }
                hasFragmentChanges = false;
                if(!currentFragmentName.equals("MessagesFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "MessagesFragment");
                    hasFragmentChanges = true;
                    fragmentNames.push(currentFragmentName);
                }
            }
            currentFragmentName = "MessagesFragment";
        }
        if(fragmentName.equals("SellFragment")) {
            setAppTitle("Sell a textbook!");
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = SELL;
            if(token == null) {
                wantedFragmentName = "SellFragment";
                Fragment fragment = fm.findFragmentByTag("AccountFragment");
                if(fragment == null) {
                    fragment = new AccountFragment();
                }
                hasFragmentChanges = false;
                if(!currentFragmentName.equals("SellFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                    hasFragmentChanges = true;
                    fragmentNames.push(currentFragmentName);
                }
            }
            else {
                Fragment fragment = fm.findFragmentByTag("SellFragment");
                if(fragment == null) {
                    fragment = new SellFragment();
                }
                hasFragmentChanges = false;
                if(!currentFragmentName.equals("SellFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "SellFragment");
                    hasFragmentChanges = true;
                    fragmentNames.push(currentFragmentName);
                }
            }
            currentFragmentName = "SellFragment";
        }
        if(fragmentName.equals("ProfileFragment")) {
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            prevNavPosition = PROFILE;
            if(token == null) {
                setAppTitle("Profile");
                wantedFragmentName = "ProfileFragment";
                Fragment fragment = fm.findFragmentByTag("AccountFragment");
                if(fragment == null) {
                    fragment = new AccountFragment();
                }
                hasFragmentChanges = false;
                if(!currentFragmentName.equals("ProfileFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                    hasFragmentChanges = true;
                    fragmentNames.push(currentFragmentName);
                }
            }
            else {
                SharedPreferences sharedPref = getSharedPreferences("com.rickybooks.rickybooks",
                        Context.MODE_PRIVATE);
                String name = sharedPref.getString("name", null);
                setAppTitle(name);
                Fragment fragment = fm.findFragmentByTag("ProfileFragment");
                if(fragment == null) {
                    fragment = new ProfileFragment();
                }
                hasFragmentChanges = false;
                if(!currentFragmentName.equals("ProfileFragment")) {
                    transaction.replace(R.id.fragment_container, fragment, "ProfileFragment");
                    hasFragmentChanges = true;
                    fragmentNames.push(currentFragmentName);
                }
            }
            currentFragmentName = "ProfileFragment";
        }
        if(fragmentName.equals("AccountFragment")) {
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            backStackCount++;
            Fragment fragment = fm.findFragmentByTag("AccountFragment");
            if(fragment == null) {
                fragment = new AccountFragment();
            }
            hasFragmentChanges = false;
            if(!currentFragmentName.equals("AccountFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "AccountFragment");
                hasFragmentChanges = true;
                fragmentNames.push(currentFragmentName);
            }
            currentFragmentName = "AccountFragment";
        }
        if(fragmentName.equals("RegisterFragment")) {
            setAppTitle("Create an account!");
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            backStackCount++;
            Fragment fragment = fm.findFragmentByTag("RegisterFragment");
            if(fragment == null) {
                fragment = new RegisterFragment();
            }
            hasFragmentChanges = false;
            if(!currentFragmentName.equals("RegisterFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "RegisterFragment");
                hasFragmentChanges = true;
                fragmentNames.push(currentFragmentName);
            }
            currentFragmentName = "RegisterFragment";
        }
        if(fragmentName.equals("LoginFragment")) {
            setAppTitle("Log in to your account!");
            if(popped) {
                bottomNavigationPosition.push(peekIndex);
            }
            bottomNavigationPosition.push(prevNavPosition);
            backStackCount++;
            Fragment fragment = fm.findFragmentByTag("LoginFragment");
            if(fragment == null) {
                fragment = new LoginFragment();
            }
            hasFragmentChanges = false;
            if(!currentFragmentName.equals("LoginFragment")) {
                transaction.replace(R.id.fragment_container, fragment, "LoginFragment");
                hasFragmentChanges = true;
                fragmentNames.push(currentFragmentName);
            }
            currentFragmentName = "LoginFragment";
        }
        if(hasFragmentChanges) {
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public String getCurrentFragmentName() {
        return currentFragmentName;
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

    public String getUserId() {
        SharedPreferences sharedPref = this.getSharedPreferences("com.rickybooks.rickybooks",
                Context.MODE_PRIVATE);
        return sharedPref.getString("user_id", null);
    }

    public String getUserName() {
        SharedPreferences sharedPref = this.getSharedPreferences("com.rickybooks.rickybooks",
                Context.MODE_PRIVATE);
        return sharedPref.getString("name", null);
    }

    public String getConversationId() {
        SharedPreferences sharedPref = this.getSharedPreferences("com.rickybooks.rickybooks",
                Context.MODE_PRIVATE);
        return sharedPref.getString("conversation_id", null);
    }

    public void setActionMode() {
        actionMode = !actionMode;
    }

    public boolean getActionMode() {
        return actionMode;
    }

    public void setMode(ActionMode mode) {
        this.mode = mode;
    }

    public ActionMode getMode() {
        return mode;
    }

    public void setInitialState() {
        clearAll();
        replaceFragment("HomeFragment");
        fragmentNames.push(currentFragmentName);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void clearAll() {
        try {
            SharedPreferences sharedPref = this.getSharedPreferences("com.rickybooks.rickybooks",
                    Context.MODE_PRIVATE);
            sharedPref.edit().clear().apply();
            token = null;
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag("ConversationsFragment");
            if(fragment != null) {
                ((ConversationsFragment) fragment).clearConversations();
            }
            bottomNavigationPosition.clear();
            titles.clear();
            fragmentNames.clear();
            prevTitle = "RickyBooks";
            prevNavPosition = 0;
            backStackCount = 0;
            poppedIndex = 0;
            peekIndex = 0;
            justLoggedIn = false;
            notificationConversationId = null;
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void loggedIn() {
        justLoggedIn = true;
    }

    public void checkToken() {
        SharedPreferences sharedPref = getSharedPreferences("com.rickybooks.rickybooks",
                Context.MODE_PRIVATE);
        token = sharedPref.getString("token", null);
    }
}
