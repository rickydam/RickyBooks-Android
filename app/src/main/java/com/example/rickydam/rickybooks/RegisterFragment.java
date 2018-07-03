package com.example.rickydam.rickybooks;

import android.annotation.SuppressLint;
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

import java.util.Objects;

public class RegisterFragment extends Fragment {
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

                final Context context = getActivity().getApplicationContext();
                String URL = "https://rickybooks.herokuapp.com/users";

                JSONObject paramsObj = new JSONObject();
                try {
                    EditText name_field = view.findViewById(R.id.register_name);
                    String name = name_field.getText().toString();
                    paramsObj.put("name", name);

                    EditText email_field = view.findViewById(R.id.register_email);
                    String email = email_field.getText().toString();
                    paramsObj.put("email", email);

                    EditText password_field = view.findViewById(R.id.register_password);
                    String password = password_field.getText().toString();
                    paramsObj.put("password", password);

                    EditText passwordC_field = view.findViewById(R.id.register_passwordC);
                    String password_confirmation = passwordC_field.getText().toString();
                    paramsObj.put("password_confirmation", password_confirmation);
                } catch(JSONException e) {
                    // Incorrect JSON format
                    e.printStackTrace();
                }

                JSONObject userObj = new JSONObject();
                try {
                    userObj.put("user", paramsObj);
                } catch(JSONException e) {
                    // Incorrect JSON format
                    e.printStackTrace();
                }

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, userObj,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EditText name_field = view.findViewById(R.id.register_name);
                        name_field.getText().clear();
                        EditText email_field = view.findViewById(R.id.register_email);
                        email_field.getText().clear();
                        EditText password_field = view.findViewById(R.id.register_password);
                        password_field.getText().clear();
                        EditText passwordC_field = view.findViewById(R.id.register_passwordC);
                        passwordC_field.getText().clear();

                        createAlert("Success!",
                                "You have been successfully registered.\n" +
                                "You are now logged in.");

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

                        MainActivity activity = (MainActivity) getActivity();

                        // RegisterFragment -> AccountFragment
                        activity.getSupportFragmentManager().popBackStack();

                        // AccountFragment -> The fragment the user was at
                        activity.getSupportFragmentManager().popBackStack();

                        // Get the fragment the user wanted
                        String wantedFragmentName = activity.getWantedFragmentName();

                        // Redirect to the fragment the user wants
                        activity.replaceFragment(wantedFragmentName);
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            byte[] errorData = error.networkResponse.data;
                            VolleyError volleyError = new VolleyError(new String(errorData));
                            String volleyErrorMessage = volleyError.getMessage();
                            JSONObject resObj = new JSONObject(volleyErrorMessage);
                            StringBuilder errorMessage = new StringBuilder();
                            JSONObject errors = new JSONObject();
                            for(int i=0; i<resObj.length(); i++) {
                                String name = resObj.names().getString(i);
                                String value = resObj.get(name).toString();
                                value = value.replace("[\"", "");
                                value = value.replace("\"]", "");
                                value = value.replace("\",\"", " and ");
                                if(Objects.equals(value, "can't be blank and is invalid")) {
                                    value = value.replace("can't be blank and is invalid",
                                            "Missing: ");
                                }
                                else if(Objects.equals(value, "can't be blank")) {
                                    value = value.replace("can't be blank", "Missing: ");
                                }
                                else if(Objects.equals(value, "is invalid")) {
                                    value = value.replace("is invalid", "Invalid: ");
                                }
                                else if(Objects.equals(value, "doesn't match Password")) {
                                    value = value.replace("doesn't match Password", "Mismatch: ");
                                    name = name.replace("_c", " C");
                                }
                                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                                errors.put(name, value + name);
                            }
                            try {
                                errorMessage.append(errors.get("Name"));
                                errorMessage.append("\n");
                            } catch(JSONException ignored) {
                                // No error for "Name" entry, this is good
                            }
                            try {
                                errorMessage.append(errors.get("Email"));
                                errorMessage.append("\n");
                            } catch(JSONException ignored) {
                                // No error for "Email" entry, this is good
                            }
                            try {
                                errorMessage.append(errors.get("Password"));
                                errorMessage.append("\n");
                            } catch(JSONException ignored) {
                                // No error for "Password" entry, this is good
                            }
                            try {
                                errorMessage.append(errors.get("Password Confirmation"));
                                errorMessage.append("\n");
                            } catch(JSONException ignored) {
                                // No error for "Password Confirmation" entry, this is good
                            }
                            errorMessage.setLength(errorMessage.length()-1);
                            createAlert("Uh oh... we got problems!", String.valueOf(errorMessage));
                        } catch(JSONException e) {
                            // Incorrect format
                            e.printStackTrace();
                        } catch(NullPointerException e) {
                            // Unable to reach server-side backend
                            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                                    "reach the server at the moment.\n\nPlease try again later.");
                        }
                    }
                });
                RequestQueue queue = Volley.newRequestQueue(context);
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
