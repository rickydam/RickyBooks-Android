package com.rickybooks.rickybooks.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonArray;

import com.rickybooks.rickybooks.Adapters.TextbookAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Textbook;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.RetrofitClient;
import com.rickybooks.rickybooks.Retrofit.TextbookService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment {
    private List<Textbook> textbookList = new ArrayList<>();
    private TextbookAdapter textbookAdapter;
    private List<Textbook> selectedTextbooks = new ArrayList<>();
    private boolean actionMode = false;
    private TextbookService textbookService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
        getUserTextbooksReq();
    }

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
                    getUserTextbooksReq();
                }
                srl.setRefreshing(false);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        textbookAdapter = new TextbookAdapter(activity, textbookList);

        RecyclerView profileRecycler = view.findViewById(R.id.profile_recycler);
        profileRecycler.setHasFixedSize(true);
        profileRecycler.setLayoutManager(layoutManager);
        profileRecycler.setAdapter(textbookAdapter);
        profileRecycler.addItemDecoration(new DividerItemDecoration(profileRecycler.getContext(),
                DividerItemDecoration.VERTICAL));

        Button button = view.findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutReq();
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

    public void deleteTextbookReq(final Textbook textbook) {
        Call<String> call = textbookService.deleteTextbook(getTokenString(), textbook.getId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                deleteTextbookRes(textbook, response);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Ricky", "deleteTextbookReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                            "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void deleteTextbookRes(Textbook textbook, Response<String> response) {
        if(response.isSuccessful()) {
            if(textbook.getImageUrls().size() > 0) {
                String signedDeleteUrl = response.body();
                deleteTextbookImageReq(signedDeleteUrl);
            }
        }
        else {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "deleteTextbookReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
    }

    public void deleteTextbookImageReq(String signedDeleteUrl) {
        Call<Void> call = textbookService.deleteImage(signedDeleteUrl);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                deleteTextbookImageRes(response);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Ricky", "deleteTextbookImageReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void deleteTextbookImageRes(Response<Void> response) {
        // No action needed for successful AWS S3 textbook image deletion
        if(!response.isSuccessful()) {
           try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "deleteTextbookImageReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
    }

    public void getUserTextbooksReq() {
        MainActivity activity = (MainActivity) getActivity();
        String userId = activity.getUserId();

        Call<JsonArray> call = textbookService.getUserTextbooks(userId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                getUserTextbooksRes(response);
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Ricky", "reqUserTextbookData error: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void getUserTextbooksRes(Response<JsonArray> response) {
        if(response.isSuccessful()) {
            try {
                String res = String.valueOf(response.body());
                JSONArray resData = new JSONArray(res);
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
                        imageUrls.add(url);
                    }

                    Textbook textbook = new Textbook(id, title, author, edition, condition,
                            type, coursecode, price, sellerId, sellerName, imageUrls);

                    textbookList.add(textbook);
                }
                textbookAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                Log.e("Ricky", "loadTextbookDataRes error: " + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
    }

    public void logoutReq() {
        Call<Void> call = textbookService.logout(getTokenString());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                logoutRes(response);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Ricky", "logoutReq failure: " + t.getMessage());
            }
        });
    }

    public void logoutRes(Response<Void> response) {
        if(response.isSuccessful()) {
            MainActivity activity = (MainActivity) getActivity();
            activity.logout();
        }
        else {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "logoutReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTokenString() {
        MainActivity activity = (MainActivity) getActivity();
        String token = activity.getToken();
        return "Token token=" + token;
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
