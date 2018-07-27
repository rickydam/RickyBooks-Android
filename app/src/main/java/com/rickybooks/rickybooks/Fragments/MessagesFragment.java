package com.rickybooks.rickybooks.Fragments;

import android.content.Context;
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

import com.rickybooks.rickybooks.Adapters.MessageAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Message;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.GetMessagesCall;
import com.rickybooks.rickybooks.Retrofit.SendMessageCall;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {
    private List<Message> messages = new ArrayList<>();
    private MessageAdapter messageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getMessages();
            }
        }).start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_messages, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.messages_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMessages();
                    }
                }).start();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Button sendButton = view.findViewById(R.id.messages_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(view);
                    }
                }).start();
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setStackFromEnd(true);
        messageAdapter = new MessageAdapter(activity, messages);

        RecyclerView messageRecycler = view.findViewById(R.id.messages_recycler);
        messageRecycler.setHasFixedSize(true);
        messageRecycler.setLayoutManager(layoutManager);
        messageRecycler.setAdapter(messageAdapter);

        return view;
    }

    public void getMessages() {
        messages.clear();
        MainActivity activity = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        String conversationId = bundle.getString("conversation_id");

        GetMessagesCall getMessagesCall = new GetMessagesCall(activity);
        getMessagesCall.req(conversationId);
        messages.addAll(getMessagesCall.getData());

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.notifyDataSetChanged();
            }
        });
    }

    public void sendMessage(final View view) {
        MainActivity activity = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        String conversationId = bundle.getString("conversation_id");
        final EditText messageInput = view.findViewById(R.id.messages_message_input);
        String body = String.valueOf(messageInput.getText());

        SendMessageCall sendMessageCall = new SendMessageCall(activity);
        sendMessageCall.req(conversationId, body);

        if(sendMessageCall.isSuccessful()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageInput.getText().clear();
                    messageInput.clearFocus();
                    hideKeyboard(view);
                }
            });
            getMessages();
        }
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
