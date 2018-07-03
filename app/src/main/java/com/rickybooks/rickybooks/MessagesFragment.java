package com.rickybooks.rickybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MessagesFragment extends Fragment {
    private List<Message> messagesList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMessages();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_messages, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.messages_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMessages();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Button sendButton = view.findViewById(R.id.messages_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final MainActivity activity = (MainActivity) v.getContext();
                final SharedPreferences sharedPref = activity.getSharedPreferences(
                        "com.rickybooks.rickybooks", Context.MODE_PRIVATE);
                final String conversationId = sharedPref.getString("conversation_id", null);
                String url = "https://rickybooks.herokuapp.com/conversations/" + conversationId +
                        "/messages";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        EditText messageInput = view.findViewById(R.id.messages_message_input);
                        messageInput.getText().clear();
                        hideKeyboard(view);
                        loadMessages();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideKeyboard(v);
                        createAlert("Seems like you forgot something...", "That's right! " +
                                "The message!");
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();

                        String token = activity.getToken();
                        headers.put("Authorization", "Token token=" + token);
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        EditText messageInput = view.findViewById(R.id.messages_message_input);
                        String message = String.valueOf(messageInput.getText());
                        params.put("body", message);

                        String userId = sharedPref.getString("user_id", null);
                        params.put("user_id", userId);

                        params.put("conversation_id", conversationId);

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                queue.add(stringRequest);
            }
        });

        RecyclerView messageRecycler = view.findViewById(R.id.messages_recycler);
        messageRecycler.setHasFixedSize(true);

        Context context = getActivity();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        messageRecycler.setLayoutManager(layoutManager);

        messageAdapter = new MessageAdapter(context, messagesList);
        messageRecycler.setAdapter(messageAdapter);

        return view;
    }

    public void loadMessages() {
        clearMessages();
        final MainActivity activity = (MainActivity) getActivity();
        SharedPreferences sharedPref = activity.getSharedPreferences("com.rickybooks.rickybooks",
                Context.MODE_PRIVATE);
        String conversationId = sharedPref.getString("conversation_id", null);
        String url = "https://rickybooks.herokuapp.com/conversations/" + conversationId + "/messages";

        StringRequest getMessagesReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray res = new JSONArray(response);
                    for(int i=0; i<res.length(); i++) {
                        JSONObject obj = res.getJSONObject(i);

                        String body = obj.getString("body");
                        JSONObject user = obj.getJSONObject("user");
                        String sender = user.getString("name");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                Locale.CANADA);
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String created_at = obj.getString("created_at");
                        Date date = sdf.parse(created_at);

                        Message message = new Message(body, sender, date);
                        messagesList.add(message);
                        messageAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();

                String token = activity.getToken();
                headers.put("Authorization", "Token token=" + token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(getMessagesReq);
    }

    public void clearMessages() {
        messagesList.clear();
    }

    public void createAlert(String title, String message) {
        Activity activity = getActivity();
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
}



























