package com.rickybooks.rickybooks.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.RetrofitClient;
import com.rickybooks.rickybooks.Retrofit.TextbookService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {
    private TextbookService textbookService;
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = view.findViewById(R.id.login_button);
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

    public void loginPressed(View view) {
        JsonObject paramsObj = new JsonObject();

        emailField = view.findViewById(R.id.login_email);
        String email = emailField.getText().toString();
        paramsObj.addProperty("email", email);

        passwordField = view.findViewById(R.id.login_password);
        String password = passwordField.getText().toString();
        paramsObj.addProperty("password", password);

        loginReq(paramsObj);
    }

    public void loginReq(JsonObject obj) {
        Call<JsonObject> call = textbookService.login(obj);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loginRes(response);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loginButton.setClickable(true);
                Log.e("Ricky", "loginReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void loginRes(Response<JsonObject> response) {
        if(response.isSuccessful()) {
            emailField.getText().clear();
            passwordField.getText().clear();

            String token = "";
            String userId = "";
            String name = "";

            try {
                String res = String.valueOf(response.body());
                JSONObject obj = new JSONObject(res);
                token = obj.getString("token");
                userId = obj.getString("user_id");
                name = obj.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences sharedPref = getActivity().getSharedPreferences(
                    "com.rickybooks.rickybooks", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token", token);
            editor.putString("user_id", userId);
            editor.putString("name", name);
            editor.apply();

            String tokenString = "Token token=" + token;
            storeFirebaseToken(tokenString, userId);

            MainActivity activity = (MainActivity) getActivity();

            // LoginFragment -> AccountFragment
            activity.getSupportFragmentManager().popBackStack();

            // AccountFragment -> The fragment the user was at
            activity.getSupportFragmentManager().popBackStack();

            // Set justLoggedIn boolean to true
            activity.loggedIn();

            // Get the fragment the user wanted
            String wantedFragmentName = activity.getWantedFragmentName();

            // Redirect to the fragment the user wants
            activity.replaceFragment(wantedFragmentName);
        }
        else {
            loginButton.setClickable(true);
            try {
                JSONObject resObj = new JSONObject(response.errorBody().string());
                String resErr = resObj.getJSONArray("errors").get(0).toString();
                JSONObject errorObj = new JSONObject(resErr);
                String errorMessage = String.valueOf(errorObj.get("detail"));
                createAlert("Typo! Try again!", errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void storeFirebaseToken(String tokenString, String userId) {
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Call<Void> call = textbookService.storeFirebaseToken(tokenString, userId, firebaseToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("Ricky", "storeFirebaseToken unsuccessful: " + errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                            "reach the server at the moment.\n\nPlease try again later.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Ricky", "storeFirebaseToken failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void createAlert(String title, String message) {
        final Activity activity = getActivity();
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                unfocus(activity.findViewById(android.R.id.content));
            }
        });
        alertDialog.show();
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
