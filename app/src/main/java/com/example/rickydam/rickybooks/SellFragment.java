package com.example.rickydam.rickybooks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SellFragment extends Fragment {

    // Required empty public constructor
    public SellFragment() {}

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sell, container, false);

        // Create the Edition spinner
        final Spinner editionSpinner = view.findViewById(R.id.textbook_edition_spinner);
        editionSpinner.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        editionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(!text.equals("Edition")) {
                    selectedText.setTextColor(Color.BLACK);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> editionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.edition, R.layout.spinner_item);
        editionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editionSpinner.setAdapter(editionAdapter);

        // Create the Condition spinner
        final Spinner conditionSpinner = view.findViewById(R.id.textbook_condition_spinner);
        conditionSpinner.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(!text.equals("Condition")) {
                    selectedText.setTextColor(Color.BLACK);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.condition, R.layout.spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);

        // Hide keyboard when focus is lost for the title EditText
        EditText title = view.findViewById(R.id.textbook_title);
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Create the Type spinner
        final Spinner typeSpinner = view.findViewById(R.id.textbook_type_spinner);
        typeSpinner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(!text.equals("Type")) {
                    selectedText.setTextColor(Color.BLACK);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.type, R.layout.spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // Hide keyboard when focus is lost for the author EditText
        EditText author = view.findViewById(R.id.textbook_author);
        author.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Hide keyboard when focus is lost for the course EditText
        EditText course = view.findViewById(R.id.textbook_coursecode);
        course.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Hide keyboard when focus is lost for the price EditText
        EditText price = view.findViewById(R.id.textbook_price);
        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Initialize the Submit button
        final Button button = view.findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                final Context context = getActivity().getApplicationContext();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://rickybooks.herokuapp.com/textbooks";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        createAlert("Success!", "Your textbook has been successfully posted!");
                        EditText title_field = view.findViewById(R.id.textbook_title);
                        title_field.getText().clear();
                        EditText author_field = view.findViewById(R.id.textbook_author);
                        author_field.getText().clear();
                        editionSpinner.setSelection(0);
                        conditionSpinner.setSelection(0);
                        typeSpinner.setSelection(0);
                        EditText coursecode_field = view.findViewById(R.id.textbook_coursecode);
                        coursecode_field.getText().clear();
                        EditText price_field = view.findViewById(R.id.textbook_price);
                        price_field.getText().clear();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            byte[] errorData = error.networkResponse.data;
                            VolleyError volleyError = new VolleyError(new String(errorData));
                            String volleyErrorMessage = volleyError.getMessage();
                            JSONObject resObj = new JSONObject(volleyErrorMessage);
                            JSONObject resData = resObj.getJSONObject("data");
                            StringBuilder errorMessage = new StringBuilder();
                            for(int i=1; i<resData.length(); i++) {
                                String name = resData.names().getString(i);
                                String value = resData.get(name).toString();
                                value = value.replace("[\"can't be blank\"]", "Missing: ");
                                name = name.replace(name.substring(0, 9), "Textbook ");
                                name = name.substring(0, 9)
                                        + name.substring(9, 10).toUpperCase()
                                        + name.substring(10);
                                if(i == resData.length()-1) {
                                    errorMessage.append(value).append(name);
                                }
                                else {
                                    errorMessage.append(value).append(name).append("\n");
                                }
                            }
                            createAlert("Hmm.. you forgot something!", String.valueOf(errorMessage));
                        } catch(JSONException e) {
                            // Incorrect JSON format
                            e.printStackTrace();
                        } catch(NullPointerException e) {
                            // Unable to reach server-side backend
                            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                                    "reach the server at the moment.\n\nPlease try again later.");
                        }
                    }
                })
                {
                    @SuppressLint("NewApi")
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                                "com.rickydam.RickyBooks", Context.MODE_PRIVATE);
                        params.put("user_id", sharedPref.getString("user_id", null));

                        EditText title_field = view.findViewById(R.id.textbook_title);
                        String textbook_title = title_field.getText().toString();
                        params.put("textbook_title", textbook_title);

                        EditText author_field = view.findViewById(R.id.textbook_author);
                        String textbook_author = author_field.getText().toString();
                        params.put("textbook_author", textbook_author);

                        Spinner edition_field = view.findViewById(R.id.textbook_edition_spinner);
                        String textbook_edition = edition_field.getSelectedItem().toString();
                        if(!Objects.equals(textbook_edition, "Edition")) {
                            params.put("textbook_edition", textbook_edition);
                        }
                        else {
                            params.put("textbook_edition", "");
                        }

                        Spinner condition_field = view.findViewById(R.id.textbook_condition_spinner);
                        String textbook_condition = condition_field.getSelectedItem().toString();
                        if(!Objects.equals(textbook_condition, "Condition")) {
                            params.put("textbook_condition", textbook_condition);
                        }
                        else {
                            params.put("textbook_condition", "");
                        }

                        Spinner type_field = view.findViewById(R.id.textbook_type_spinner);
                        String textbook_type = type_field.getSelectedItem().toString();
                        if(!Objects.equals(textbook_type, "Type")) {
                            params.put("textbook_type", textbook_type);
                        }
                        else {
                            params.put("textbook_type", "");
                        }

                        EditText coursecode_field = view.findViewById(R.id.textbook_coursecode);
                        String textbook_coursecode = coursecode_field.getText().toString();
                        params.put("textbook_coursecode", textbook_coursecode);

                        EditText price_field = view.findViewById(R.id.textbook_price);
                        String textbook_price = price_field.getText().toString();
                        params.put("textbook_price", textbook_price);

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                        return headers;
                    }
                };
                queue.add(stringRequest);
            }
        });
        return view;
    }

    public void hideKeyboard(View view) {
        Context context = getActivity().getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void createAlert(String title, String message) {
        Activity activity = getActivity();
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
