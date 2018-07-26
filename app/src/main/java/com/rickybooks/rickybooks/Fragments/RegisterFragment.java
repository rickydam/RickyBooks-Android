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
import com.rickybooks.rickybooks.Retrofit.RegisterCall;
import com.rickybooks.rickybooks.Retrofit.StoreFirebaseTokenCall;

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        final Button registerbutton = view.findViewById(R.id.register_button);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                hideKeyboard(v);
                unfocus(v);
                registerbutton.setClickable(false);
                registerButtonPressed(view);
            }
        });
        return view;
    }

    public void registerButtonPressed(final View view) {
        JsonObject paramsObj = new JsonObject();

        EditText nameField = view.findViewById(R.id.register_name);
        String name = nameField.getText().toString();
        paramsObj.addProperty("name", name);

        EditText emailField = view.findViewById(R.id.register_email);
        String email = emailField.getText().toString();
        paramsObj.addProperty("email", email);

        EditText passwordField = view.findViewById(R.id.register_password);
        String password = passwordField.getText().toString();
        paramsObj.addProperty("password", password);

        EditText passwordCField = view.findViewById(R.id.register_passwordC);
        String passwordConfirmation = passwordCField.getText().toString();
        paramsObj.addProperty("password_confirmation", passwordConfirmation);

        final JsonObject userObj = new JsonObject();
        userObj.add("user", paramsObj);

        new Thread(new Runnable() {
            @Override
            public void run() {
                register(userObj, view);
            }
        }).start();
    }

    public void register(JsonObject userObj, View view) {
        MainActivity activity = (MainActivity) getActivity();

        RegisterCall registerCall = new RegisterCall(activity, view);
        registerCall.req(userObj);

        boolean isSuccessful = registerCall.isSuccessful();
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
            EditText name = view.findViewById(R.id.register_name);
            name.clearFocus();
        } catch(NullPointerException ignored) {
            // The name EditText was not in focus
        }
        try {
            EditText email = view.findViewById(R.id.register_email);
            email.clearFocus();
        } catch(NullPointerException ignored) {
            // The email EditText was not in focus
        }
        try {
            EditText password = view.findViewById(R.id.register_password);
            password.clearFocus();
        } catch(NullPointerException ignored) {
            // The password EditText was not in focus
        }
        try {
            EditText passwordC = view.findViewById(R.id.register_passwordC);
            passwordC.clearFocus();
        } catch(NullPointerException ignored) {
            // The password confirmation EditText was not in focus
        }
    }
}
