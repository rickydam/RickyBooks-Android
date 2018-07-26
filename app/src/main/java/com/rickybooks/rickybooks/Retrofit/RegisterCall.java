package com.rickybooks.rickybooks.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;
import com.rickybooks.rickybooks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterCall {
    private MainActivity activity;
    private View view;
    private boolean isSuccessful;

    public RegisterCall(MainActivity activity, View view) {
        this.activity = activity;
        this.view = view;
        isSuccessful = false;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void req(JsonObject userObj) {
        Retrofit retrofit = new RetrofitClient().getClient();
        TextbookService textbookService = retrofit.create(TextbookService.class);

        try {
            Call<JsonObject> call = textbookService.register(userObj);
            Response<JsonObject> response = call.execute();

            isSuccessful = response.isSuccessful();
            if(isSuccessful) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EditText nameField = view.findViewById(R.id.register_name);
                        EditText emailField = view.findViewById(R.id.register_email);
                        EditText passwordField = view.findViewById(R.id.register_password);
                        EditText passwordCField = view.findViewById(R.id.register_passwordC);
                        nameField.getText().clear();
                        emailField.getText().clear();
                        passwordField.getText().clear();
                        passwordCField.getText().clear();
                    }
                });

                JsonObject obj = response.body();
                String token = obj.get("token").getAsString();
                String userId = obj.get("user_id").getAsString();
                String name = obj.get("name").getAsString();

                SharedPreferences sharedPref = activity.getSharedPreferences(
                        "com.rickybooks.rickybooks", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("token", token);
                editor.putString("user_id", userId);
                editor.putString("name", name);
                editor.apply();
            }
            else {
                Button registerButton = view.findViewById(R.id.register_button);
                registerButton.setClickable(true);

                JSONObject resObj = new JSONObject(response.errorBody().string());
                StringBuilder errorMessage = new StringBuilder();
                JSONObject errorsObj = new JSONObject();

                for(int i=0; i<resObj.length(); i++) {
                    String name = resObj.names().getString(i);
                    String value = resObj.get(name).toString();
                    value = value.replace("[\"", "");
                    value = value.replace("\"]", "");
                    value = value.replace("\",\"", " and ");
                    switch(value) {
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
                    errorMessage.append(errorsObj.getString("Name"));
                    errorMessage.append("\n");
                } catch(JSONException ignored) {
                    // No error for "Name" entry, this is good
                }
                try {
                    errorMessage.append(errorsObj.getString("Email"));
                    errorMessage.append("\n");
                } catch(JSONException ignored) {
                    // No error for "Email" entry, this is good
                }
                try {
                    errorMessage.append(errorsObj.getString("Password"));
                    errorMessage.append("\n");
                } catch(JSONException ignored) {
                    // No error for "Password" entry, this is good
                }
                try {
                    errorMessage.append(errorsObj.getString("Password Confirmation"));
                    errorMessage.append("\n");
                } catch(JSONException ignored) {
                    // No error for "Password Confirmation" entry, this is good
                }
                errorMessage.setLength(errorMessage.length()-1);
                Alert alert = new Alert(activity);
                alert.create("Uh oh... we got problems!", String.valueOf(errorMessage));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
