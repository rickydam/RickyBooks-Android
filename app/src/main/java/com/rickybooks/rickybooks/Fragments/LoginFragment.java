package com.rickybooks.rickybooks.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.LoginCall;
import com.rickybooks.rickybooks.Retrofit.StoreFirebaseTokenCall;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        final Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(view);
                unfocus(view);
                loginButton.setClickable(false);
                loginPressed(view);
            }
        });
        return view;
    }

    public void loginPressed(final View view) {
        final JsonObject userObj = new JsonObject();

        EditText emailField = view.findViewById(R.id.login_email);
        String email = emailField.getText().toString();
        userObj.addProperty("email", email);

        EditText passwordField = view.findViewById(R.id.login_password);
        String password = passwordField.getText().toString();
        userObj.addProperty("password", password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                login(userObj, view);
            }
        }).start();
    }

    public void login(JsonObject userObj, View view) {
        MainActivity activity = (MainActivity) getActivity();

        LoginCall loginCall = new LoginCall(activity, view);
        loginCall.req(userObj);

        boolean isSuccessful = loginCall.isSuccessful();
        if(isSuccessful) {
            StoreFirebaseTokenCall storeFirebaseTokenCall = new StoreFirebaseTokenCall(activity);
            storeFirebaseTokenCall.req();
        }
    }

    public void hideKeyboard(View view) {
        Context context = getActivity().getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void unfocus(View view) {
        try {
            EditText email = view.findViewById(R.id.login_email);
            email.clearFocus();
        } catch(NullPointerException ignored) {
            // The email EditText was not in focus
        }
        try {
            EditText password = view.findViewById(R.id.login_password);
            password.clearFocus();
        } catch(NullPointerException ignored) {
            // The password EditText was not in focus
        }
    }
}
