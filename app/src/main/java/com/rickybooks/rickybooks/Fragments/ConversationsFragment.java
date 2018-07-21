package com.rickybooks.rickybooks.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.gson.JsonArray;

import com.rickybooks.rickybooks.Adapters.ConversationAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Conversation;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.RetrofitClient;
import com.rickybooks.rickybooks.Retrofit.TextbookService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConversationsFragment extends Fragment {
    private List<Conversation> conversationsList = new ArrayList<>();
    private ConversationAdapter conversationAdapter;
    private TextbookService textbookService;
    private RecyclerView recyclerView;
    private List<Conversation> selectedConversations = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
        getConversationsReq();
        checkBundle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.conversations_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainActivity activity = (MainActivity) getActivity();
                boolean actionMode = activity.getActionMode();
                if(!actionMode) {
                    getConversationsReq();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        conversationAdapter = new ConversationAdapter(activity, conversationsList);

        recyclerView = view.findViewById(R.id.conversations_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(conversationAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        return view;
    }

    public void prepareSelection(final Conversation conversation) {
        ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setActionMode();
                activity.setMode(mode);
                menu.add("DELETE").setIcon(R.drawable.ic_delete);
                selectConversation(conversation);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.toString().equals("DELETE")) {
                    int selectedConversationsCount = selectedConversations.size();
                    if(selectedConversationsCount >= 1) {
                        for(int i=0; i<selectedConversationsCount; i++) {
                            Conversation convo = selectedConversations.get(i);
                            deleteConversationReq(convo);
                            conversationsList.remove(0);
                        }
                        conversationAdapter.notifyDataSetChanged();
                    }
                    if(selectedConversationsCount == 1) {
                        createAlert("Success!", "Successfully deleted " + selectedConversationsCount
                                + " conversation!");
                    }
                    if(selectedConversationsCount > 1) {
                        createAlert("Success!", "Successfully deleted " + selectedConversationsCount
                                + " conversations!");
                    }
                    mode.finish();
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setActionMode();
                selectedConversations.clear();
                conversationAdapter.notifyDataSetChanged();
            }
        };

        MainActivity activity = (MainActivity) getActivity();
        activity.startSupportActionMode(actionModeCallbacks);
    }

    public void selectConversation(Conversation conversation) {
        if(!conversationExists(conversation)) {
            selectedConversations.add(conversation);
        }
        else {
            selectedConversations.remove(conversation);
        }
        MainActivity activity = (MainActivity) getActivity();
        ActionMode mode = activity.getMode();
        mode.setTitle(selectedConversations.size() + " selected");
    }

    public boolean conversationExists(Conversation conversation) {
        return selectedConversations.contains(conversation);
    }

    public void deleteConversationReq(Conversation conversation) {
        MainActivity activity = (MainActivity) getActivity();
        String token = activity.getToken();
        String tokenString = "Token token=" + token;

        Call<Void> call = textbookService.deleteConversation(tokenString, conversation.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                deleteConversationRes(response);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Ricky", "deleteConversationReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void deleteConversationRes(Response<Void> response) {
        if(!response.isSuccessful()) {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "deleteConversationReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
    }

    public void checkBundle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bundle = getArguments();
                    String notificationConversationId = bundle.getString("notification_conversation_id");
                    int position = -1;
                    for(int i=0; i<conversationsList.size(); i++) {
                        Conversation conversation = conversationsList.get(i);
                        if(conversation.getId().equals(notificationConversationId)) {
                            position = i;
                        }
                    }
                    openConversation(position);
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }, 300);
    }

    public void openConversation(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.findViewHolderForAdapterPosition(position).itemView.performClick();
            }
        }, 300);
    }

    public void getConversationsReq() {
        MainActivity activity = (MainActivity) getActivity();
        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String userId = activity.getUserId();

        Call<JsonArray> call = textbookService.getConversations(tokenString, userId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                getConversationsRes(response);
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Ricky", "getConversationsReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void getConversationsRes(Response<JsonArray> response) {
        if(response.isSuccessful()) {
            clearConversations();
            try {
                String res = String.valueOf(response.body());
                JSONArray resData = new JSONArray(res);
                for(int i=0; i<resData.length(); i++) {
                    JSONObject obj = resData.getJSONObject(i);
                    String conversationId = obj.getString("id");

                    JSONObject recipientObj = obj.getJSONObject("recipient");
                    String recipientName = recipientObj.getString("name");

                    JSONObject senderObj = obj.getJSONObject("sender");
                    String senderName = senderObj.getString("name");

                    String otherName;
                    MainActivity activity = (MainActivity) getActivity();
                    String userName = activity.getUserName();

                    if(userName.equals(recipientName)) {
                        otherName = senderName;
                    }
                    else {
                        otherName = recipientName;
                    }

                    Conversation conversation = new Conversation(conversationId, otherName);
                    conversationsList.add(conversation);
                }
                conversationAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "getConversationsReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
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
        alertDialog.show();
    }
}
