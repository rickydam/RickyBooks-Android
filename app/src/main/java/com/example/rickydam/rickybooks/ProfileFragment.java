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
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class ProfileFragment extends Fragment {
    private List<Textbook> textbookList = new ArrayList<>();
    private TextbookAdapter textbookAdapter;
    private List<Textbook> selectedTextbooks = new ArrayList<>();
    private boolean actionMode = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final SwipeRefreshLayout srl = view.findViewById(R.id.profile_textbooks_refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!actionMode) {
                    textbookList.clear();
                    reqUserTextbookData();
                }
                srl.setRefreshing(false);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        textbookAdapter = new TextbookAdapter(activity, textbookList);

        RecyclerView recyclerView = view.findViewById(R.id.profile_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(textbookAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        Button button = view.findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    public void prepareSelection(final Textbook textbook) {
        ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                actionMode = true;
                menu.add("DELETE").setIcon(R.drawable.ic_delete);
                selectTextbook(textbook);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.toString().equals("DELETE")) {
                    int selectedTextbooksCount = selectedTextbooks.size();
                    if(selectedTextbooksCount >= 1) {
                        for(int i=0; i<selectedTextbooksCount; i++) {
                            Textbook book = selectedTextbooks.get(i);
                            deleteTextbookReq(book);
                            textbookList.remove(0);
                        }
                        textbookAdapter.notifyDataSetChanged();
                    }
                    if(selectedTextbooksCount == 1) {
                        createAlert("Success!", "Successfully deleted " + selectedTextbooksCount +
                                " textbook!");
                    }
                    if(selectedTextbooksCount > 1) {
                        createAlert("Success!", "Successfully deleted " + selectedTextbooksCount +
                                " textbooks!");
                    }
                    mode.finish();
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = false;
                selectedTextbooks.clear();
                textbookAdapter.notifyDataSetChanged();
            }
        };

        MainActivity activity = (MainActivity) getActivity();
        activity.startSupportActionMode(actionModeCallbacks);
    }

    public void deleteTextbookReq(Textbook textbook) {
        final String textbookId = textbook.getId();
        String url = "https://rickybooks.herokuapp.com/textbooks/" + textbookId;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // No action required
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

                MainActivity activity = (MainActivity) getActivity();
                String token = activity.getToken();
                headers.put("Authorization", "Token token=" + token);

                return headers;
            }
        };
        MainActivity activity = (MainActivity) getActivity();
        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(stringRequest);
    }

    public void selectTextbook(Textbook textbook) {
        if(!textbookExists(textbook)) {
            selectedTextbooks.add(textbook);
        }
        else {
            selectedTextbooks.remove(textbook);
        }
    }

    public boolean textbookExists(Textbook textbook) {
        return selectedTextbooks.contains(textbook);
    }

    public boolean getActionMode() {
        return actionMode;
    }

    public void reqUserTextbookData() {
        MainActivity activity = (MainActivity) getActivity();

        SharedPreferences sharedPref = activity.getSharedPreferences("com.rickydam.RickyBooks",
                Context.MODE_PRIVATE);
        String userId = sharedPref.getString("user_id", null);

        String url = "https://rickybooks.herokuapp.com/users/" + userId + "/textbooks";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadUserTextbookData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(stringRequest);
    }

    public void loadUserTextbookData(String response) {
        try {
            JSONArray res = new JSONArray(response);

            for(int i=0; i<res.length(); i++) {
                JSONObject textbookObj = res.getJSONObject(i);
                String id         = textbookObj.getString("id");
                String title      = textbookObj.getString("textbook_title");
                String author     = textbookObj.getString("textbook_author");
                String edition    = textbookObj.getString("textbook_edition");
                String condition  = textbookObj.getString("textbook_condition");
                String type       = textbookObj.getString("textbook_type");
                String coursecode = textbookObj.getString("textbook_coursecode");
                String price      = "$ " + textbookObj.getString("textbook_price");
                String sellerId   = textbookObj.getString("user_id");

                JSONObject user = textbookObj.getJSONObject("user");
                String sellerName = user.getString("name");

                List<String> imageUrls = new ArrayList<>();
                JSONArray imagesJSONarr = textbookObj.getJSONArray("images");

                for(int j=0; j<imagesJSONarr.length(); j++) {
                    JSONObject imageObj = imagesJSONarr.getJSONObject(j);
                    String url = imageObj.getString("url");
                    imageUrls.add(url);
                }

                Textbook textbook = new Textbook(id, title, author, edition, condition, type,
                        coursecode, price, sellerId, sellerName, imageUrls);
                textbookList.add(textbook);
            }
            textbookAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        final Context context = getActivity().getApplicationContext();
        String url = "https://rickybooks.herokuapp.com/logout";

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MainActivity activity = (MainActivity) getActivity();
                activity.logout();
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

                MainActivity activity = (MainActivity) getActivity();
                String token = activity.getToken();
                headers.put("Authorization", "Token token=" + token);

                return headers;
            }
        };
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
