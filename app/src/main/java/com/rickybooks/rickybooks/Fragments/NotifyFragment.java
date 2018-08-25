package com.rickybooks.rickybooks.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.rickybooks.rickybooks.Adapters.NotifyItemAdapter;
import com.rickybooks.rickybooks.Adapters.TextbookAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.NotifyItem;
import com.rickybooks.rickybooks.Models.Textbook;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.GetNotifyItemsCall;
import com.rickybooks.rickybooks.Retrofit.GetNotifyResultsCall;
import com.rickybooks.rickybooks.Retrofit.PostNotifyItemCall;

import java.util.ArrayList;
import java.util.List;

public class NotifyFragment extends Fragment {
    private List<NotifyItem> notifyItems;
    private List<Textbook> notifyResults;
    private NotifyItemAdapter notifyItemAdapter;
    private TextbookAdapter notifyResultAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notifyItems = new ArrayList<>();
        notifyResults = new ArrayList<>();
        getNotifyItems();
        getNotifyResults();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        MainActivity activity = (MainActivity) getActivity();

        Button addItemButton = view.findViewById(R.id.add_notify_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemButtonPressed();
            }
        });

        final SwipeRefreshLayout notifyItemsRefresh = view.findViewById(R.id.notify_items_refresh);
        notifyItemsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotifyItems();
                notifyItemsRefresh.setRefreshing(false);
            }
        });

        LinearLayoutManager notifyItemsLayoutManager = new LinearLayoutManager(activity);
        notifyItemAdapter = new NotifyItemAdapter(notifyItems);
        RecyclerView notifyItemsRecycler = view.findViewById(R.id.notify_items_recycler);
        notifyItemsRecycler.setHasFixedSize(true);
        notifyItemsRecycler.setLayoutManager(notifyItemsLayoutManager);
        notifyItemsRecycler.setAdapter(notifyItemAdapter);
        notifyItemsRecycler.addItemDecoration(new DividerItemDecoration(notifyItemsRecycler.getContext(),
                DividerItemDecoration.VERTICAL));

        final SwipeRefreshLayout notifyResultsRefresh = view.findViewById(R.id.notify_results_refresh);
        notifyResultsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotifyResults();
                notifyResultsRefresh.setRefreshing(false);
            }
        });

        LinearLayoutManager notifyResultsLayoutManager = new LinearLayoutManager(activity);
        notifyResultAdapter = new TextbookAdapter(activity, notifyResults);
        RecyclerView notifyResultsRecycler = view.findViewById(R.id.notify_results_recycler);
        notifyResultsRecycler.setHasFixedSize(true);
        notifyResultsRecycler.setLayoutManager(notifyResultsLayoutManager);
        notifyResultsRecycler.setAdapter(notifyResultAdapter);
        notifyResultsRecycler.addItemDecoration(new DividerItemDecoration(notifyResultsRecycler.getContext(),
                DividerItemDecoration.VERTICAL));

        return view;
    }

    public void getNotifyItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                notifyItems.clear();
                MainActivity activity = (MainActivity) getActivity();
                GetNotifyItemsCall getNotifyItemsCall = new GetNotifyItemsCall(activity);
                getNotifyItemsCall.req();
                if(getNotifyItemsCall.isSuccessful()) {
                    notifyItems.addAll(getNotifyItemsCall.getData());
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public void getNotifyResults() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                notifyResults.clear();
                MainActivity activity = (MainActivity) getActivity();
                GetNotifyResultsCall getNotifyResultsCall = new GetNotifyResultsCall(activity);
                getNotifyResultsCall.req();
                if(getNotifyResultsCall.isSuccessful()) {
                    notifyResults.addAll(getNotifyResultsCall.getData());
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyResultAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public void addItemButtonPressed() {
        MainActivity activity = (MainActivity) getActivity();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity,
                R.style.AppTheme_AlertDialogTheme);

        final Spinner categorySpinner = new Spinner(activity);
        categorySpinner.setBackground(getResources().getDrawable(R.drawable.border));
        categorySpinner.setPadding(10, 10, 10, 10);
        categorySpinner.setId(Spinner.generateViewId());

        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("Title");
        categoryList.add("Author");
        categoryList.add("Coursecode");
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, categoryList);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryArrayAdapter);

        RelativeLayout.LayoutParams categoryLayoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        categoryLayoutParams.height = 100;
        categoryLayoutParams.setMargins(5, 10, 5, 10);
        categorySpinner.setLayoutParams(categoryLayoutParams);

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setHint("Type a keyword...");
        input.setBackgroundResource(R.drawable.border);
        input.setPadding(20, 10, 20, 10);
        input.setHintTextColor(getResources().getColor(R.color.lightGray));
        input.setHeight(40);
        input.setTextSize(15);

        RelativeLayout.LayoutParams inputLayoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inputLayoutParams.height = 100;
        inputLayoutParams.setMargins(10, 10, 10, 10);
        inputLayoutParams.addRule(RelativeLayout.BELOW, categorySpinner.getId());
        input.setLayoutParams(inputLayoutParams);

        RelativeLayout parentLayout = new RelativeLayout(activity);
        parentLayout.setMinimumWidth(600);
        parentLayout.setMinimumHeight(200);
        parentLayout.addView(categorySpinner);
        parentLayout.addView(input);

        alertDialog.setView(parentLayout);

        alertDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryValue = categorySpinner.getSelectedItem().toString();
                String inputValue = String.valueOf(input.getText());
                postNotifyItem(categoryValue, inputValue);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void postNotifyItem(final String categoryValue, final String inputValue) {
        final MainActivity activity = (MainActivity) getActivity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                PostNotifyItemCall postNotifyItemCall = new PostNotifyItemCall(activity);
                postNotifyItemCall.req(categoryValue, inputValue);

                if(postNotifyItemCall.isSuccessful()) {
                    getNotifyItems();
                    getNotifyResults();
                }
            }
        }).start();
    }
}
