package com.rickybooks.rickybooks.Fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.rickybooks.rickybooks.Adapters.TextbookAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Textbook;
import com.rickybooks.rickybooks.R;

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

                    for(int i=0; i<resData.length(); i++) {
                        JSONObject textbookObj = resData.getJSONObject(i);
                        String id         = textbookObj.getString("id");
                        String title      = textbookObj.getString("textbook_title");
                        String author     = textbookObj.getString("textbook_author");
                        String edition    = textbookObj.getString("textbook_edition");
                        String condition  = textbookObj.getString("textbook_condition");
                        String type       = textbookObj.getString("textbook_type");
                        String coursecode = textbookObj.getString("textbook_coursecode");
                        String price      = "$ " + textbookObj.getString("textbook_price");

                        JSONObject user = textbookObj.getJSONObject("user");
                        String sellerId = textbookObj.getString("user_id");
                        String sellerName = user.getString("name");

                        List<String> imageUrls = new ArrayList<>();
                        JSONArray imagesJSONarr = textbookObj.getJSONArray("images");

                        for(int j=0; j<imagesJSONarr.length(); j++) {
                            JSONObject imageObj = imagesJSONarr.getJSONObject(j);
                            String url = imageObj.getString("url");
                            Log.e("Ricky", url);
                            imageUrls.add(url);
                        }

                        Textbook textbook = new Textbook(id, title, author, edition, condition,
                                type, coursecode, price, sellerId, sellerName, imageUrls);

                        textbookList.add(textbook);
                    }
                    textbookAdapter.notifyDataSetChanged();
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
