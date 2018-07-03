package com.example.rickydam.rickybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.List;

public class BuyFragment extends Fragment {
    private List<Textbook> textbookList = new ArrayList<>();
    private TextbookAdapter textbookAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTextbookData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_buy, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.textbooks_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                textbookList.clear();
                loadTextbookData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.textbook_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        MainActivity activity = (MainActivity) getActivity();

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        textbookAdapter = new TextbookAdapter(activity, textbookList);
        recyclerView.setAdapter(textbookAdapter);

        return view;
    }

    private void loadTextbookData() {
        Context context = getActivity().getApplicationContext();
        String url = "https://rickybooks.herokuapp.com/textbooks";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray resData = new JSONArray(response);

                    int resDataLength = resData.length();
                    for(int i=0; i<resDataLength; i++) {
                        JSONObject obj = resData.getJSONObject(i);
                        String id = obj.getString("id");
                        String title = obj.getString("textbook_title");
                        String author = obj.getString("textbook_author");
                        String edition = obj.getString("textbook_edition");
                        String condition = obj.getString("textbook_condition");
                        String type = obj.getString("textbook_type");
                        String coursecode = obj.getString("textbook_coursecode");
                        String price = "$ " + obj.getString("textbook_price");

                        JSONObject user = obj.getJSONObject("user");
                        String sellerId = obj.getString("user_id");
                        String sellerName = user.getString("name");

                        Textbook textbook = new Textbook(id, title, author, edition, condition,
                                type, coursecode, price, sellerId, sellerName);
                        textbookList.add(textbook);
                        textbookAdapter.notifyDataSetChanged();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
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
}
