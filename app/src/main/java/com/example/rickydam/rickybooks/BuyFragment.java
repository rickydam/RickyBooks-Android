package com.example.rickydam.rickybooks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private TextbookAdapter mTextbookAdapter;

    // Required empty public constructor
    public BuyFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadInitialTextbookData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_buy, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewTextbookData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RecyclerView mRecyclerView = view.findViewById(R.id.items_list);
        mRecyclerView.setHasFixedSize(true);

        Context context = getActivity().getApplicationContext();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mTextbookAdapter = new TextbookAdapter(context, textbookList);
        mRecyclerView.setAdapter(mTextbookAdapter);

        return view;
    }

    private void loadNewTextbookData() {
        Context context = getActivity().getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://rickybooks.herokuapp.com/textbooks";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    JSONArray resData = resObj.getJSONArray("data");

                    int resDataLength = resData.length();
                    int currentDataLength = textbookList.size();
                    int newEntriesCount = resDataLength - currentDataLength;

                    // Case 1: New textbooks added
                    // Case 2: Number of textbooks added > Number of textbooks deleted
                    if(newEntriesCount > 0) {
                        for(int i=0; i<newEntriesCount; i++) {
                            JSONObject obj = resData.getJSONObject(i);
                            String title = obj.getString("textbook_title");
                            String author = obj.getString("textbook_author");
                            String edition = obj.getString("textbook_edition");
                            String condition = obj.getString("textbook_condition");
                            String type = obj.getString("textbook_type");
                            String coursecode = obj.getString("textbook_coursecode");
                            String price = "$ " + obj.getString("textbook_price");
                            Textbook textbook = new Textbook(title, author, edition, condition,
                                    type, coursecode, price);
                            textbookList.add(i, textbook);
                            mTextbookAdapter.notifyDataSetChanged();
                        }
                    }

                    // Case 3: No textbooks added nor deleted
                    // Case 4: Number of textbooks added == Number of textbooks deleted
                    else if(newEntriesCount == 0) {
                        // TODO: Handle this in a more efficient way
                        textbookList.clear();
                        loadInitialTextbookData();
                    }

                    // Case 5: Textbooks have been deleted
                    // Case 6: Number of textbooks added < Number of textbooks deleted
                    else {
                        // TODO: Handle this in a more efficient way
                        textbookList.clear();
                        loadInitialTextbookData();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", String.valueOf(error));
            }
        });
        queue.add(stringRequest);
    }

    private void loadInitialTextbookData() {
        // Instantiate the RequestQueue
        Context context = getActivity().getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://rickybooks.herokuapp.com/textbooks";

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    JSONArray resData = resObj.getJSONArray("data");
                    int resDataLength = resData.length();
                    for(int i=0; i<resDataLength; i++) {
                        JSONObject obj = resData.getJSONObject(i);
                        String title = obj.getString("textbook_title");
                        String author = obj.getString("textbook_author");
                        String edition = obj.getString("textbook_edition");
                        String condition = obj.getString("textbook_condition");
                        String type = obj.getString("textbook_type");
                        String coursecode = obj.getString("textbook_coursecode");
                        String price = "$ " + obj.getString("textbook_price");
                        Textbook textbook = new Textbook(title, author, edition, condition, type,
                                coursecode, price);
                        textbookList.add(textbook);
                        mTextbookAdapter.notifyDataSetChanged();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", String.valueOf(error));
            }
        });
        queue.add(stringRequest);
    }
}
