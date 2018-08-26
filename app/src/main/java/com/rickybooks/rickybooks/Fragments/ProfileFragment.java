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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rickybooks.rickybooks.Adapters.TextbookAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Conversation;
import com.rickybooks.rickybooks.Models.Textbook;
import com.rickybooks.rickybooks.Other.Alert;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.DeleteConversationCall;
import com.rickybooks.rickybooks.Retrofit.DeleteImageCall;
import com.rickybooks.rickybooks.Retrofit.DeleteTextbookCall;
import com.rickybooks.rickybooks.Retrofit.DeleteUserCall;
import com.rickybooks.rickybooks.Retrofit.GetConversationsCall;
import com.rickybooks.rickybooks.Retrofit.GetUserTextbooksCall;
import com.rickybooks.rickybooks.Retrofit.LogoutCall;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private List<Textbook> textbooks;
    private TextbookAdapter textbookAdapter;
    private List<Textbook> selectedTextbooks;
    private List<Conversation> conversations;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        textbooks = new ArrayList<>();
        selectedTextbooks = new ArrayList<>();
        conversations = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getUserTextbooks();
            }
        }).start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final SwipeRefreshLayout srl = view.findViewById(R.id.profile_textbooks_refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final MainActivity activity = (MainActivity) getActivity();
                boolean actionMode = activity.getActionMode();
                if(!actionMode) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getUserTextbooks();
                        }
                    }).start();
                }
                srl.setRefreshing(false);
            }
        });

        final MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        textbookAdapter = new TextbookAdapter(activity, textbooks);

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logoutThread();
                    }
                }).start();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_delete_textbook:
                prepareSelection(null);
                break;
            case R.id.options_delete_account:
                createAlertPrompt("Delete your account?", "Are you sure? This is irreversible.");
                break;
        }
        return true;
    }

    public void prepareSelection(final Textbook textbook) {
        ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setActionMode();
                activity.setMode(mode);
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
                    deleteActionPressed(mode);
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setActionMode();
                selectedTextbooks.clear();
                textbookAdapter.notifyDataSetChanged();
            }
        };

        MainActivity activity = (MainActivity) getActivity();
        activity.startSupportActionMode(actionModeCallbacks);
    }

    public void deleteActionPressed(final ActionMode mode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity activity = (MainActivity) getActivity();
                int selectedTextbooksCount = selectedTextbooks.size();

                if(selectedTextbooksCount >= 1) {
                    deleteTextbook(selectedTextbooks, true);
                }

                if(selectedTextbooksCount == 1) {
                    Alert alert = new Alert(activity);
                    alert.create("Success!", "Successfully deleted " + selectedTextbooksCount +
                            " textbook!");
                }

                if(selectedTextbooksCount > 1) {
                    Alert alert = new Alert(activity);
                    alert.create("Success!", "Successfully deleted " + selectedTextbooksCount +
                            " textbooks!");
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mode.finish();
                    }
                });
            }
        }).start();
    }

    public void selectTextbook(Textbook textbook) {
        if(textbook != null) {
            if(!textbookExists(textbook)) {
                selectedTextbooks.add(textbook);
            }
            else {
                selectedTextbooks.remove(textbook);
            }
        }
        MainActivity activity = (MainActivity) getActivity();
        ActionMode mode = activity.getMode();
        mode.setTitle(selectedTextbooks.size() + " selected");
    }

    public boolean textbookExists(Textbook textbook) {
        return selectedTextbooks.contains(textbook);
    }

    public void deleteAccount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Get all the user's conversations
                getConversations();

                // Delete all the user's conversations
                deleteConversation();

                // Get all the user's textbooks
                getUserTextbooks();

                // Delete all the user's textbooks
                deleteTextbook(textbooks, false);

                // Delete the user
                deleteUser();
            }
        }).start();
    }

    public void getUserTextbooks() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    textbooks.clear();
                    MainActivity activity = (MainActivity) getActivity();

                    GetUserTextbooksCall getUserTextbooksCall = new GetUserTextbooksCall(activity);
                    getUserTextbooksCall.req();
                    textbooks.addAll(getUserTextbooksCall.getData());

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textbookAdapter.notifyDataSetChanged();
                        }
                    });
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void logoutThread() {
        MainActivity activity = (MainActivity) getActivity();
        LogoutCall logoutCall = new LogoutCall(activity);
        logoutCall.req();
    }

    public void getConversations() {
        MainActivity activity = (MainActivity) getActivity();
        GetConversationsCall getConversationsCall = new GetConversationsCall(activity);
        getConversationsCall.req();
        conversations = getConversationsCall.getConversationsData();
    }

    public void deleteConversation() {
        MainActivity activity = (MainActivity) getActivity();
        DeleteConversationCall deleteConversationCall = new DeleteConversationCall(activity);
        for(int i=0; i<conversations.size(); i++) {
            Conversation conversation = conversations.get(i);
            deleteConversationCall.req(conversation);
        }
    }

    public void deleteTextbook(final List<Textbook> textbooksToDelete, final boolean selected) {
        MainActivity activity = (MainActivity) getActivity();

        for(int i=0; i<textbooksToDelete.size(); i++) {
            Textbook textbook = textbooksToDelete.get(i);

            DeleteTextbookCall deleteTextbookCall = new DeleteTextbookCall(activity);
            deleteTextbookCall.req(textbook.getId());

            // Only proceed if the textbook has images to delete
            if(textbook.getImageUrls().size() > 0) {
                String signedDeleteUrl = deleteTextbookCall.getData();

                // Delete all the user's textbook images
                DeleteImageCall deleteImageCall = new DeleteImageCall(activity);
                deleteImageCall.req(signedDeleteUrl);
            }
            if(selected) {
                textbooks.remove(textbooks.indexOf(textbook));
            }
        }
        if(selected) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textbookAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void deleteUser() {
        MainActivity activity = (MainActivity) getActivity();
        DeleteUserCall deleteUserCall = new DeleteUserCall(activity);
        deleteUserCall.req();
    }

    public void createAlertPrompt(String title, String message) {
        Activity activity = getActivity();
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
