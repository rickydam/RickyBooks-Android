package com.rickybooks.rickybooks.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rickybooks.rickybooks.Adapters.TextbookAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Textbook;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.GetTextbooksCall;

import java.util.ArrayList;
import java.util.List;

public class BuyFragment extends Fragment {
    private List<Textbook> textbooks;
    private TextbookAdapter textbookAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        textbooks = new ArrayList<>();
        getTextbooksThread();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_buy, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.textbooks_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTextbooksThread();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        textbookAdapter = new TextbookAdapter(activity, textbooks);

        RecyclerView recyclerView = view.findViewById(R.id.textbook_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(textbookAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.buy, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity activity = (MainActivity) getActivity();
        switch(item.getItemId()) {
            case R.id.buy_menu_search:
                activity.replaceFragment("SearchFragment");
                break;
            case R.id.buy_menu_notifyme:
                activity.replaceFragment("NotifyFragment");
        }
        return true;
    }

    public void getTextbooksThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getTextbooks();
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getTextbooks() {
        textbooks.clear();
        MainActivity activity = (MainActivity) getActivity();

        GetTextbooksCall getTextbooksCall = new GetTextbooksCall(activity);
        getTextbooksCall.req();
        textbooks.addAll(getTextbooksCall.getData());

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textbookAdapter.notifyDataSetChanged();
            }
        });
    }
}
