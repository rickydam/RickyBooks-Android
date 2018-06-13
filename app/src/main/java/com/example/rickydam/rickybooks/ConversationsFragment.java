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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationsFragment extends Fragment {
    private List<Conversation> conversationsList = new ArrayList<>();
    private ConversationAdapter conversationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.conversations_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadConversations();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        conversationAdapter = new ConversationAdapter(conversationsList);

        RecyclerView recyclerView = view.findViewById(R.id.conversations_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(conversationAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        loadConversations();
        return view;
    }

    public void loadConversations() {
        clearConversations();

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences("com.rickydam.RickyBooks",
                Context.MODE_PRIVATE);
        String userId = sharedPref.getString("user_id", null);
        String url = "http://rickybooks.herokuapp.com/conversations/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray res = new JSONArray(response);

                    for(int i=0; i<res.length(); i++) {
                        JSONObject obj = res.getJSONObject(i);
                        String conversationId = obj.getString("id");
                        JSONObject recipient = obj.getJSONObject("recipient");
                        String recipientName = (String) recipient.get("name");

                        MainActivity activity = (MainActivity) getActivity();
                        SharedPreferences sharedPref = activity.getSharedPreferences(
                                "com.rickydam.RickyBooks", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("recipient_name", recipientName);
                        editor.apply();

                        Conversation conversation = new Conversation(conversationId, recipientName);
                        conversationsList.add(conversation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                conversationAdapter.notifyDataSetChanged();
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

                String token = ((MainActivity) getActivity()).getToken();
                headers.put("Authorization", "Token token=" + token);

                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    public void clearConversations() {
        conversationsList.clear();
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
    }
}