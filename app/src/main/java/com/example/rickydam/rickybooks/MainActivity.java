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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    BuyFragment buyFragment;
    ConversationsFragment conversationsFragment;
    MessagesFragment messagesFragment;
    SellFragment sellFragment;
    ProfileFragment profileFragment;
    RegisterFragment registerFragment;
    LoginFragment loginFragment;
    AccountFragment accountFragment;
    DetailsFragment detailsFragment;
    Fragment currentFragment;
    String currentFragmentName = "";
    String previousFragmentName = "";
    String token = null;
    String textbookTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        homeFragment = new HomeFragment();
        buyFragment = new BuyFragment();
        conversationsFragment = new ConversationsFragment();
        messagesFragment = new MessagesFragment();
        sellFragment = new SellFragment();
        profileFragment = new ProfileFragment();
        registerFragment = new RegisterFragment();
        loginFragment = new LoginFragment();
        accountFragment = new AccountFragment();
        detailsFragment = new DetailsFragment();

        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, homeFragment);
        currentFragment = homeFragment;
        currentFragmentName = "HomeFragment";

        fragmentTransaction.add(R.id.fragment_container, buyFragment);
        fragmentTransaction.hide(buyFragment);

        fragmentTransaction.add(R.id.fragment_container, conversationsFragment);
        fragmentTransaction.hide(conversationsFragment);

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

        fragmentTransaction.add(R.id.fragment_container, detailsFragment);
        fragmentTransaction.hide(detailsFragment);

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
                    loadConversations();
                    replaceFragment("ConversationsFragment");
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
        conversationsFragment.clearConversations();
    }

    public void checkToken() {
        SharedPreferences sharedPref = getSharedPreferences("com.rickydam.RickyBooks",
                Context.MODE_PRIVATE);
        token = sharedPref.getString("token", null);
    }

    public void setDetails(Bundle bundle) {
        textbookTitle = bundle.getString("Title");

        detailsFragment.setArguments(bundle);
        TextView detailsTitle = detailsFragment.getView().findViewById(R.id.details_title);
        TextView detailsAuthor = detailsFragment.getView().findViewById(R.id.details_author);
        TextView detailsEdition = detailsFragment.getView().findViewById(R.id.details_edition);
        TextView detailsCondition = detailsFragment.getView().findViewById(R.id.details_condition);
        TextView detailsType = detailsFragment.getView().findViewById(R.id.details_type);
        TextView detailsCoursecode = detailsFragment.getView().findViewById(R.id.details_coursecode);
        TextView detailsPrice = detailsFragment.getView().findViewById(R.id.details_price);
        TextView detailsSeller = detailsFragment.getView().findViewById(R.id.details_seller);

        detailsTitle.setText(bundle.getString("Title"));
        detailsAuthor.setText(bundle.getString("Author"));
        detailsEdition.setText(bundle.getString("Edition"));
        detailsCondition.setText(bundle.getString("Condition"));
        detailsType.setText(bundle.getString("Type"));
        detailsCoursecode.setText(bundle.getString("Coursecode"));
        detailsPrice.setText(bundle.getString("Price"));
        detailsSeller.setText(bundle.getString("SellerName"));

        ImageView detailsImage = detailsFragment.getView().findViewById(R.id.details_image);
        Glide.with(this).load(R.drawable.placeholder_img).into(detailsImage);
    }

    public void initializeConversation(String conversationId) {
        SharedPreferences sharedPref = getSharedPreferences("com.rickydam.RickyBooks",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("conversation_id", conversationId);
        editor.apply();
    }

    public void loadConversations() {
        conversationsFragment.loadConversations();
    }

    public void clearMessages() {
        messagesFragment.clearMessages();
    }

    public void loadMessages() {
        messagesFragment.loadMessages();
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
        if(Objects.equals(fragmentName, "ConversationsFragment")) {
            previousFragmentName = currentFragmentName;
            currentFragmentName = "ConversationsFragment";
            setTitle("Conversations");
            if(token != null) {
                if(currentFragment != conversationsFragment) {
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(currentFragment);
                    fragmentTransaction.show(conversationsFragment);
                    fragmentTransaction.commit();
                    currentFragment = conversationsFragment;
                }
            }
            else {
                previousFragmentName = currentFragmentName;
                currentFragmentName = "ConversationsFragment";
                if(currentFragment != accountFragment) {
                    replaceFragment("AccountFragment");
                }
            }
        }
        if(Objects.equals(fragmentName, "MessagesFragment")) {
            previousFragmentName = currentFragmentName;
            currentFragmentName = "MessagesFragment";
            if(currentFragment != messagesFragment) {
                setTitle("Messages");
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(currentFragment);
                fragmentTransaction.show(messagesFragment);
                fragmentTransaction.commit();
                currentFragment = messagesFragment;
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

            if(token != null) {
                if(currentFragment != profileFragment) {
                    SharedPreferences sharedPref = this.getSharedPreferences(
                            "com.rickydam.RickyBooks", Context.MODE_PRIVATE);
                    setTitle(sharedPref.getString("name", null));
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(currentFragment);
                    fragmentTransaction.show(profileFragment);
                    fragmentTransaction.commit();
                    currentFragment = profileFragment;
                }
            }
            else {
                setTitle("Profile");
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
            if(currentFragmentName.equals("DetailsFragment")) {
                previousFragmentName = currentFragmentName;
                currentFragmentName = "AccountFragment";
            }
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(accountFragment);
            fragmentTransaction.commit();
            currentFragment = accountFragment;
        }
        if(Objects.equals(fragmentName, "DetailsFragment")) {
            previousFragmentName = currentFragmentName;
            currentFragmentName = "DetailsFragment";
            setTitle("Buy " + textbookTitle + "!");
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(detailsFragment);
            fragmentTransaction.commit();
            currentFragment = detailsFragment;
        }
    }
}
