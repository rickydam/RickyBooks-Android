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
import com.rickybooks.rickybooks.Fragments.EditTextbookFragment;
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
    private boolean actionMode;
    private ActionMode mode;
    private Bundle detailsBundle;
    private Bundle messagesBundle;

    private int backStackCount;
    private Stack<String> titles;
    private Stack<Integer> bottomNavPositions;
    private Stack<String> fragmentNames;
    private String currentFragmentName;
    private String prevTitle;
    private int prevNavPosition;
    private String prevFragmentName;
    private String accountTitle;
    private int accountNavPosition;
    private String wantedFragmentName;
    private String notificationConversationId;

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

        actionMode = false;
        mode = null;
        detailsBundle = null;
        messagesBundle = null;
        backStackCount = 0;
        titles = new Stack<>();
        bottomNavPositions = new Stack<>();
        fragmentNames = new Stack<>();
        currentFragmentName = "HomeFragment";
        prevTitle = "RickyBooks";
        prevNavPosition = HOME;
        prevFragmentName = "HomeFragment";
        accountTitle = null;
        accountNavPosition = -1;
        wantedFragmentName = null;
        notificationConversationId = null;

        BottomNavigationView bnv = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bnv);
        bnv.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Fragment homeFragment = new HomeFragment();
        final FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, homeFragment, "HomeFragment");
        transaction.commit();

        setTitle("RickyBooks");

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(backStackCount == -1) {
                    backStackCount = 0;
                }
                else {
                    int numberOfBackStackItems = fm.getBackStackEntryCount();

                    // Back button pressed
                    if(backStackCount > numberOfBackStackItems) {
                        // Restore the title of the previous fragment
                        prevTitle = titles.pop();
                        setTitle(prevTitle);

                        // Restore the bottom navigation position of the previous fragment
                        prevNavPosition = bottomNavPositions.pop();

                        // Make sure to set the bottom navigation item back
                        setBottomNavigationItem(prevNavPosition);

                        // Restore the fragment name of the previous fragment
                        prevFragmentName = fragmentNames.pop();
                        currentFragmentName = prevFragmentName;

                        // Decrement backStackCount to reflect the correct number of backstack items
                        backStackCount--;
                    }

                    // New items added to the backstack
                    else {
                        backStackCount++;
                    }
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

    public void handleValues(String title, int navPosition, String currentFragmentName) {
        // Set the current app title
        setTitle(title);

        // Set the bottom navigation item
        setBottomNavigationItem(navPosition);

        // Store the currentFragmentName
        this.currentFragmentName = currentFragmentName;

        // Push the previously added values
        titles.push(prevTitle);
        bottomNavPositions.push(prevNavPosition);
        fragmentNames.push(prevFragmentName);

        // Store the current values for the next iteration
        prevTitle = title;
        prevNavPosition = navPosition;
        prevFragmentName = currentFragmentName;
    }

    public void loginPop() {
        titles.pop();
        bottomNavPositions.pop();
        fragmentNames.pop();
    }

    public void replaceFragment(String fragmentName) {
        if(actionMode) {
            mode.finish();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        boolean hasFragmentChanges = false;

        if(fragmentName.equals("HomeFragment")) {
            Fragment homeFragment = fm.findFragmentByTag("HomeFragment");
            if(homeFragment == null) {
                homeFragment = new HomeFragment();
            }
            if(!currentFragmentName.equals("HomeFragment")) {
                handleValues("RickyBooks", HOME, "HomeFragment");
                transaction.replace(R.id.fragment_container, homeFragment, currentFragmentName);
                hasFragmentChanges = true;
            }
        }

        if(fragmentName.equals("BuyFragment")) {
            Fragment buyFragment = fm.findFragmentByTag("BuyFragment");
            if(buyFragment == null) {
                buyFragment = new BuyFragment();
            }
            if(!currentFragmentName.equals("BuyFragment")) {
                handleValues("Buy a textbook!", BUY, "BuyFragment");
                transaction.replace(R.id.fragment_container, buyFragment, currentFragmentName);
                hasFragmentChanges = true;
            }
        }

        if(fragmentName.equals("DetailsFragment")) {
            Fragment detailsFragment = fm.findFragmentByTag("DetailsFragment");
            if(detailsFragment == null) {
                detailsFragment = new DetailsFragment();
            }
            if(!currentFragmentName.equals("DetailsFragment")) {
                detailsFragment.setArguments(detailsBundle);
                String title = "Buy " + detailsBundle.get("Title");
                handleValues(title, BUY, "DetailsFragment");
                transaction.replace(R.id.fragment_container, detailsFragment, currentFragmentName);
                hasFragmentChanges = true;
            }
        }

        if(fragmentName.equals("ConversationsFragment")) {
            if(getToken() == null) {
                accountTitle = "Conversations";
                accountNavPosition = MESSAGES;
                wantedFragmentName = "ConversationsFragment";
                replaceFragment("AccountFragment");
            }
            else {
                Fragment conversationsFragment = fm.findFragmentByTag("ConversationsFragment");
                if(conversationsFragment == null) {
                    conversationsFragment = new ConversationsFragment();
                }
                if(!currentFragmentName.equals("ConversationsFragment")) {
                    handleValues("Conversations", MESSAGES, "ConversationsFragment");
                    transaction.replace(R.id.fragment_container, conversationsFragment, currentFragmentName);
                    hasFragmentChanges = true;
                    if(notificationConversationId != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("notification_conversation_id", notificationConversationId);
                        conversationsFragment.setArguments(bundle);
                    }
                }
            }
        }

        if(fragmentName.equals("MessagesFragment")) {
            if(getToken() == null) {
                accountTitle = "Messages";
                accountNavPosition = MESSAGES;
                wantedFragmentName = "MessagesFragment";
                replaceFragment("AccountFragment");
            }
            else {
                Fragment messagesFragment = fm.findFragmentByTag("MessagesFragment");
                if(messagesFragment == null) {
                    messagesFragment = new MessagesFragment();
                }
                if(!currentFragmentName.equals("MessagesFragment")) {
                    handleValues("Messages", MESSAGES, "MessagesFragment");
                    messagesFragment.setArguments(messagesBundle);
                    transaction.replace(R.id.fragment_container, messagesFragment, currentFragmentName);
                    hasFragmentChanges = true;
                }
            }
        }

        if(fragmentName.equals("SellFragment")){
            if(getToken() == null) {
                accountTitle = "Sell a textbook!";
                accountNavPosition = SELL;
                wantedFragmentName = "SellFragment";
                replaceFragment("AccountFragment");
            }
            else {
                Fragment sellFragment = fm.findFragmentByTag("SellFragment");
                if(sellFragment == null) {
                    sellFragment = new SellFragment();
                }
                if(!currentFragmentName.equals("SellFragment")) {
                    handleValues("Sell a textbook!", SELL, "SellFragment");
                    transaction.replace(R.id.fragment_container, sellFragment, currentFragmentName);
                    hasFragmentChanges = true;
                }
            }
        }

        if(fragmentName.equals("ProfileFragment")) {
            if(getToken() == null) {
                accountTitle = "Profile";
                accountNavPosition = PROFILE;
                wantedFragmentName = "ProfileFragment";
                replaceFragment("AccountFragment");
            }
            else {
                Fragment profileFragment = fm.findFragmentByTag("ProfileFragment");
                if(profileFragment == null) {
                    profileFragment = new ProfileFragment();
                }
                if(!currentFragmentName.equals("ProfileFragment")) {
                    String title = getUserName();
                    handleValues(title, PROFILE, "ProfileFragment");
                    transaction.replace(R.id.fragment_container, profileFragment, currentFragmentName);
                    hasFragmentChanges = true;
                }
            }
        }

        if(fragmentName.equals("EditTextbookFragment")) {
            String title = "Edit " + editTextbookBundle.getString("Title");
            if(getToken() == null) {
                accountTitle = title;
                accountNavPosition = PROFILE;
                wantedFragmentName = "EditTextbookFragment";
                replaceFragment("AccountFragment");
            }
            else {
                Fragment editTextbookFragment = fm.findFragmentByTag("EditTextbookFragment");
                if(editTextbookFragment == null) {
                    editTextbookFragment = new EditTextbookFragment();
                }
                if(!currentFragmentName.equals("EditTextbookFragment")) {
                    handleValues(title, PROFILE, "EditTextbookFragment");
                    editTextbookFragment.setArguments(editTextbookBundle);
                    transaction.replace(R.id.fragment_container, editTextbookFragment, currentFragmentName);
                    hasFragmentChanges = true;
                }
            }
        }

        if(fragmentName.equals("AccountFragment")) {
            if(currentFragmentName.equals("DetailsFragment")) {
                accountTitle = prevTitle;
                accountNavPosition = prevNavPosition;
                wantedFragmentName = prevFragmentName;
            }
            Fragment accountFragment = fm.findFragmentByTag("AccountFragment");
            if(accountFragment == null) {
                accountFragment = new AccountFragment();
            }
            if(!currentFragmentName.equals("AccountFragment")) {
                handleValues(accountTitle, accountNavPosition, "AccountFragment");
                transaction.replace(R.id.fragment_container, accountFragment, currentFragmentName);
                hasFragmentChanges = true;
            }
        }

        if(fragmentName.equals("RegisterFragment")) {
            Fragment registerFragment = fm.findFragmentByTag("RegisterFragment");
            if(registerFragment == null) {
                registerFragment = new RegisterFragment();
            }
            if(!currentFragmentName.equals("RegisterFragment")) {
                handleValues(prevTitle, prevNavPosition, "RegisterFragment");
                transaction.replace(R.id.fragment_container, registerFragment, currentFragmentName);
                hasFragmentChanges = true;
            }
        }

        if(fragmentName.equals("LoginFragment")) {
            Fragment loginFragment = fm.findFragmentByTag("LoginFragment");
            if(loginFragment == null) {
                loginFragment = new LoginFragment();
            }
            if(!currentFragmentName.equals("LoginFragment")) {
                handleValues(prevTitle, prevNavPosition, "LoginFragment");
                transaction.replace(R.id.fragment_container, loginFragment, currentFragmentName);
                hasFragmentChanges = true;
            }
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

    public void setMessagesBundle(Bundle bundle) {
        messagesBundle = bundle;
    }

    public String getToken() {
        SharedPreferences sharedPref = getSharedPreferences("com.rickybooks.rickybooks",
                Context.MODE_PRIVATE);
        return sharedPref.getString("token", null);
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
        SharedPreferences sharedPref = this.getSharedPreferences("com.rickybooks.rickybooks",
                Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();

        FragmentManager fm = getSupportFragmentManager();
        backStackCount = -1;
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        setTitle("RickyBooks");
        setBottomNavigationItem(HOME);

        titles.clear();
        bottomNavPositions.clear();
        fragmentNames.clear();

        currentFragmentName = "HomeFragment";
        prevTitle = "RickyBooks";
        prevNavPosition = HOME;
        prevFragmentName = "HomeFragment";
        accountTitle = null;
        accountNavPosition = -1;
        wantedFragmentName = null;
        notificationConversationId = null;
        actionMode = false;
        mode = null;
        detailsBundle = null;
        messagesBundle = null;
    }
}
