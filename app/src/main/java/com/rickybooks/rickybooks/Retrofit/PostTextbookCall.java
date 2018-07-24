package com.rickybooks.rickybooks.Retrofit;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;
import com.rickybooks.rickybooks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostTextbookCall {
    private MainActivity activity;
    private View view;
    private String textbookId;

    public PostTextbookCall(MainActivity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    public String getData() {
        return textbookId;
    }

    public void req(String tokenString, String userId, String textbookTitle, String textbookAuthor,
                    String textbookEdition, String textbookCondition, String textbookType,
                    String textbookCoursecode, String textbookPrice) {
        Retrofit retrofit = new RetrofitClient().getClient();
        TextbookService textbookService = retrofit.create(TextbookService.class);

        Call<String> call = textbookService.postTextbook(tokenString, userId, textbookTitle,
                textbookAuthor, textbookEdition, textbookCondition, textbookType,
                textbookCoursecode, textbookPrice);
        try {
            Response<String> response = call.execute();

            if(response.isSuccessful()) {
                textbookId = response.body();

                Alert alert = new Alert(activity);
                alert.create("Success!", "Your textbook has been successfully posted!");

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clearAll();
                    }
                });
            }
            else {
                try {
                    String errorResponse = response.errorBody().string();

                    JSONObject resObj = new JSONObject(errorResponse);
                    JSONObject resData = resObj.getJSONObject("data");
                    StringBuilder errorMessage = new StringBuilder();

                    for(int i=0; i<resData.length(); i++) {
                        String name = resData.names().getString(i);
                        String value = resData.get(name).toString();
                        value = value.replace("[\"can't be blank\"]", "Missing: ");
                        name = name.replace(name.substring(0, 9), "Textbook ");
                        name = name.substring(0, 9) + name.substring(9, 10).toUpperCase() +
                                name.substring(10);
                        if(i == resData.length()-1) {
                            errorMessage.append(value).append(name);
                        }
                        else {
                            errorMessage.append(value).append(name).append("\n");
                        }
                    }

                    Alert alert = new Alert(activity);
                    alert.create("Hmm.. you forgot something!", String.valueOf(errorMessage));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearAll() {
        EditText titleField = view.findViewById(R.id.textbook_title);
        titleField.getText().clear();

        EditText authorField = view.findViewById(R.id.textbook_author);
        authorField.getText().clear();

        Spinner editionSpinner = view.findViewById(R.id.textbook_edition_spinner);
        editionSpinner.setSelection(0);

        Spinner conditionSpinner = view.findViewById(R.id.textbook_condition_spinner);
        conditionSpinner.setSelection(0);

        Spinner typeSpinner = view.findViewById(R.id.textbook_type_spinner);
        typeSpinner.setSelection(0);

        EditText coursecodeField = view.findViewById(R.id.textbook_coursecode);
        coursecodeField.getText().clear();

        EditText priceField = view.findViewById(R.id.textbook_price);
        priceField.getText().clear();
    }
}
