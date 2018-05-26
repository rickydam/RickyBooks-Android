package com.example.rickydam.rickybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        final Button button = view.findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(view);
                unfocus(view);

                Context context = getActivity().getApplicationContext();
                RequestQueue queue = Volley.newRequestQueue(context);
                String URL = "http://rickybooks.herokuapp.com/login";

                JSONObject paramsObj = new JSONObject();
                try {
                    EditText email_field = view.findViewById(R.id.login_email);
                    String email = email_field.getText().toString();
                    paramsObj.put("email", email);

                    EditText password_field = view.findViewById(R.id.login_password);
                    String password = password_field.getText().toString();
                    paramsObj.put("password", password);
                } catch(JSONException e) {
                    // Incorrect JSON format
                    e.printStackTrace();
                }

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, paramsObj,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EditText email_field = view.findViewById(R.id.login_email);
                        email_field.getText().clear();
                        EditText password_field = view.findViewById(R.id.login_password);
                        password_field.getText().clear();

                        String token = null;
                        String userId = null;
                        String name = null;
                        try {
                            token = response.getString("token");
                            userId = response.getString("user_id");
                            name = response.getString("name");

                        } catch (JSONException e) {
                            // Incorrect JSON format
                            e.printStackTrace();
                        }

                        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                                "com.rickydam.RickyBooks", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", token);
                        editor.putString("user_id", userId);
                        editor.putString("name", name);
                        editor.apply();

                        String previousFragmentName = ((MainActivity) getActivity())
                                .getPreviousFragmentName();
                        ((MainActivity) getActivity()).replaceFragment(previousFragmentName);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            byte[] errorData = error.networkResponse.data;
                            VolleyError volleyError = new VolleyError(new String(errorData));
                            String volleyErrorMessage = volleyError.getMessage();
                            JSONObject resObj = new JSONObject(volleyErrorMessage);
                            String resErr = resObj.getJSONArray("errors").get(0).toString();
                            JSONObject errorObj = new JSONObject(resErr);
                            String errorMessage = String.valueOf(errorObj.get("detail"));
                            createAlert("Typo! Try again!", errorMessage);
                        } catch(JSONException e) {
                            // Incorrect JSON format
                            e.printStackTrace();
                        } catch(NullPointerException e) {
                            // Unable to reach server-side backend
                            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                                    "reach the server at the moment.\n\nPlease try again later.");
                        }
                    }
                });
                queue.add(req);
            }
        });
        return view;
    }

    public void createAlert(String title, String message) {
        final Activity activity = getActivity();
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
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
