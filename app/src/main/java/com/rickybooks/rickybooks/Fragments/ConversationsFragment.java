package com.rickybooks.rickybooks.Fragments;

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

import com.rickybooks.rickybooks.Adapters.ConversationAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Conversation;
import com.rickybooks.rickybooks.Other.Alert;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.DeleteConversationCall;
import com.rickybooks.rickybooks.Retrofit.GetConversationsCall;

import java.util.ArrayList;
import java.util.List;

public class ConversationsFragment extends Fragment {
    private List<Conversation> conversations = new ArrayList<>();
    private ConversationAdapter conversationAdapter;
    private List<Conversation> selectedConversations = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getConversations();
            }
        }).start();
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getConversations();
                        }
                    }).start();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        conversationAdapter = new ConversationAdapter(activity, conversations);

        RecyclerView recyclerView = view.findViewById(R.id.conversations_recycler);
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
                            final Conversation convo = selectedConversations.get(i);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    deleteConversation(convo);
                                }
                            }).start();
                            conversations.remove(conversations.indexOf(convo));
                        }
                        conversationAdapter.notifyDataSetChanged();
                    }
                    MainActivity activity = (MainActivity) getActivity();
                    if(selectedConversationsCount == 1) {
                        Alert alert = new Alert(activity);
                        alert.create("Success!", "Successfully deleted " + selectedConversationsCount
                                + " conversation!");
                    }
                    if(selectedConversationsCount > 1) {
                        Alert alert = new Alert(activity);
                        alert.create("Success!", "Successfully deleted " + selectedConversationsCount
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

    public void getConversations() {
        conversations.clear();
        MainActivity activity = (MainActivity) getActivity();

        GetConversationsCall getConversationsCall = new GetConversationsCall(activity);
        getConversationsCall.req();
        conversations.addAll(getConversationsCall.getData());

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                conversationAdapter.notifyDataSetChanged();
            }
        });
    }

    public void deleteConversation(Conversation conversation) {
        MainActivity activity = (MainActivity) getActivity();

        DeleteConversationCall deleteConversationCall = new DeleteConversationCall(activity);
        deleteConversationCall.req(conversation);
    }
}
