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

public class LoginCall {
    private MainActivity activity;
    private View view;
    private boolean isSuccessful;

    public LoginCall(MainActivity activity, View view) {
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
            Call<JsonObject> call = textbookService.login(userObj);
            Response<JsonObject> response = call.execute();

            isSuccessful = response.isSuccessful();
            if(isSuccessful) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EditText emailField = view.findViewById(R.id.login_email);
                        EditText passwordField = view.findViewById(R.id.login_password);
                        emailField.getText().clear();
                        passwordField.getText().clear();
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
                Button loginButton = view.findViewById(R.id.login_button);
                loginButton.setClickable(true);

                JSONObject resObj = new JSONObject(response.errorBody().string());
                String resErr = resObj.getJSONArray("errors").get(0).toString();
                JSONObject errObj = new JSONObject(resErr);
                String errMsg = String.valueOf(errObj.get("detail"));

                Alert alert = new Alert(activity);
                alert.create("Typo! Try again!", errMsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
