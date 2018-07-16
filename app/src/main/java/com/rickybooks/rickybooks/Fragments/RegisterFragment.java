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

public class RegisterFragment extends Fragment {
    private TextbookService textbookService;
    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordCField;

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
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        final Button button = view.findViewById(R.id.register_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                hideKeyboard(v);
                unfocus(v);
                registerButtonPressed(view);
            }
        });
        return view;
    }

    public void registerButtonPressed(View view) {
        JsonObject paramsObj = new JsonObject();

        nameField = view.findViewById(R.id.register_name);
        String name = nameField.getText().toString();
        paramsObj.addProperty("name", name);

        emailField = view.findViewById(R.id.register_email);
        String email = emailField.getText().toString();
        paramsObj.addProperty("email", email);

        passwordField = view.findViewById(R.id.register_password);
        String password = passwordField.getText().toString();
        paramsObj.addProperty("password", password);

        passwordCField = view.findViewById(R.id.register_passwordC);
        String passwordConfirmation = passwordCField.getText().toString();
        paramsObj.addProperty("password_confirmation", passwordConfirmation);

        JsonObject userObj = new JsonObject();
        userObj.add("user", paramsObj);

        registerReq(userObj);
    }

    public void registerReq(JsonObject obj) {
        Call<JsonObject> call = textbookService.register(obj);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                registerRes(response);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Ricky", "registerReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                            "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void registerRes(Response<JsonObject> response) {
        if(response.isSuccessful()) {
            nameField.getText().clear();
            emailField.getText().clear();
            passwordField.getText().clear();
            passwordCField.getText().clear();

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

            // RegisterFragment -> AccountFragment
            activity.getSupportFragmentManager().popBackStack();

            // AccountFragment -> The fragment the user was at
            activity.getSupportFragmentManager().popBackStack();

            // Get the fragment the user wanted
            String wantedFragmentName = activity.getWantedFragmentName();

            // Redirect to the fragment the user wants
            activity.replaceFragment(wantedFragmentName);

            createAlert("Success!", "You have been successfully registered.\n" +
                                    "You are now logged in.");
        }
        else {
            try {
                JSONObject resObj = new JSONObject(response.errorBody().string());
                StringBuilder errorMessage = new StringBuilder();
                JSONObject errorsObj = new JSONObject();
                for(int i=0; i<resObj.length(); i++) {
                    String name = resObj.names().getString(i);
                    String value = resObj.get(name).toString();
                    value = value.replace("[\"", "");
                    value = value.replace("\"]", "");
                    value = value.replace("\",\"", " and ");
                    switch (value) {
                        case "can't be blank and is invalid":
                            value = value.replace("can't be blank and is invalid", "Missing: ");
                            break;
                        case "can't be blank":
                            value = value.replace("can't be blank", "Missing: ");
                            break;
                        case "is invalid":
                            value = value.replace("is invalid", "Invalid: ");
                            break;
                        case "doesn't match Password":
                            value = value.replace("doesn't match Password", "Mismatch: ");
                            name = name.replace("_c", " C");
                            break;
                        case "has already been taken":
                            value = value.replace("has already been taken", "Taken: ");
                    }
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    errorsObj.put(name, value + name);
                }
                try {
                    errorMessage.append(errorsObj.get("Name"));
                    errorMessage.append("\n");
                } catch(JSONException ignored) {
                    // No error for "Name" entry, this is good
                }
                try {
                    errorMessage.append(errorsObj.get("Email"));
                    errorMessage.append("\n");
                } catch(JSONException ignored) {
                    // No error for "Email" entry, this is good
                }
                try {
                    errorMessage.append(errorsObj.get("Password"));
                    errorMessage.append("\n");
                } catch(JSONException ignored) {
                    // No error for "Password" entry, this is good
                }
                try {
                    errorMessage.append(errorsObj.get("Password Confirmation"));
                    errorMessage.append("\n");
                } catch(JSONException ignored) {
                    // No error for "Password Confirmation" entry, this is good
                }
                errorMessage.setLength(errorMessage.length()-1);
                createAlert("Uh oh... we got problems!", String.valueOf(errorMessage));
            } catch(JSONException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            } catch(NullPointerException e) {
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                            "reach the server at the moment.\n\nPlease try again later.");
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
