package com.rickybooks.rickybooks.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rickybooks.rickybooks.Adapters.TextbookAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Textbook;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.SearchTextbooksCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchFragment extends Fragment {
    private List<Textbook> textbooks;
    private TextbookAdapter textbookAdapter;
    private View view;
    private Timer timer;
    private EditText searchInput;
    private Spinner searchSpinner;
    private Spinner editionSpinner;
    private RelativeLayout editionLayout;
    private Spinner conditionSpinner;
    private RelativeLayout conditionLayout;
    private Spinner typeSpinner;
    private RelativeLayout typeLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textbooks = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initSearchSpinner();
        initRecyclerView();
        initSearchInput();
        initEditionSpinner();
        initConditionSpinner();
        initTypeSpinner();
        return view;
    }

    public void initSearchInput() {
        searchInput = view.findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void afterTextChanged(final Editable s) {
                if(s.length() > 2) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            performSearch(String.valueOf(s));
                        }
                    }, 500);
                }
                if((s.length() <= 2) && (s.length() > 0)) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            performSearch(String.valueOf(s));
                        }
                    }, 1000);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // User is typing, reset the already started timer, if there is one
                if(timer != null) {
                    timer.cancel();
                }
                if((s.length() < 3) && (count == 0)) {
                    resetAll();
                }
            }
        });
    }

    public void performSearch(final String input) {
        searchSpinner = view.findViewById(R.id.search_spinner);
        final String category = searchSpinner.getSelectedItem().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchTextbooks(category, input);
            }
        }).start();
    }

    public void searchTextbooks(String category, String input) {
        textbooks.clear();
        MainActivity activity = (MainActivity) getActivity();

        SearchTextbooksCall searchTextbooksCall = new SearchTextbooksCall(activity);
        searchTextbooksCall.req(category, input);
        textbooks.addAll(searchTextbooksCall.getData());

        try {
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

    public void initRecyclerView() {
        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        textbookAdapter = new TextbookAdapter(activity, textbooks);

        RecyclerView recyclerView = view.findViewById(R.id.textbook_results);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(textbookAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    public void initSearchSpinner() {
        searchSpinner = view.findViewById(R.id.search_spinner);

        searchSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                hideKeyboard();
                return true;
            }
        });

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String searchSpinnerItemText = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(selectedText != null) {
                    if(!searchSpinnerItemText.equals("Category")) {
                        selectedText.setTextColor(Color.BLACK);
                        switch(searchSpinnerItemText) {
                            case "Edition":
                                hideSearchInput();
                                hideConditionSpinner();
                                hideTypeSpinner();
                                editionLayout.setVisibility(View.VISIBLE);
                                editionSpinner.setVisibility(View.VISIBLE);
                                break;
                            case "Condition":
                                hideSearchInput();
                                hideEditionSpinner();
                                hideTypeSpinner();
                                conditionLayout.setVisibility(View.VISIBLE);
                                conditionSpinner.setVisibility(View.VISIBLE);
                                break;
                            case "Type":
                                hideSearchInput();
                                hideEditionSpinner();
                                hideConditionSpinner();
                                typeLayout.setVisibility(View.VISIBLE);
                                typeSpinner.setVisibility(View.VISIBLE);
                                break;
                            default:
                                hideEditionSpinner();
                                hideConditionSpinner();
                                hideTypeSpinner();
                                searchInput.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                    else {
                        hideSearchInput();
                        hideEditionSpinner();
                        hideConditionSpinner();
                        hideTypeSpinner();
                        resetAll();
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> searchAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.search, R.layout.spinner_item);
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(searchAdapter);
    }

    public void hideSearchInput() {
        if(searchInput != null) {
            searchInput.setVisibility(View.INVISIBLE);
            searchInput.getText().clear();
        }
    }

    public void hideEditionSpinner() {
        if((editionLayout != null) && (editionSpinner != null)) {
            editionLayout.setVisibility(View.INVISIBLE);
            editionSpinner.setVisibility(View.INVISIBLE);
            editionSpinner.setSelection(0);
        }
    }

    public void hideConditionSpinner() {
        if((conditionLayout != null) && (conditionSpinner != null)) {
            conditionLayout.setVisibility(View.INVISIBLE);
            conditionSpinner.setVisibility(View.INVISIBLE);
            conditionSpinner.setSelection(0);
        }
    }

    public void hideTypeSpinner() {
        if((typeLayout != null) && (typeSpinner != null)) {
            typeLayout.setVisibility(View.INVISIBLE);
            typeSpinner.setVisibility(View.INVISIBLE);
            typeSpinner.setSelection(0);
        }
    }

    public void initEditionSpinner() {
        editionLayout =  view.findViewById(R.id.search_edition_spinner_layout);
        editionSpinner = view.findViewById(R.id.search_edition_spinner);

        editionSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                hideKeyboard();
                return true;
            }
        });

        editionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(selectedText != null) {
                    if(!text.equals("Edition")) {
                        selectedText.setTextColor(Color.BLACK);
                        performSearch(text);
                    }
                    else {
                        resetAll();
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> editionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.edition, R.layout.spinner_item);
        editionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editionSpinner.setAdapter(editionAdapter);
    }

    public void initConditionSpinner() {
        conditionLayout =  view.findViewById(R.id.search_condition_spinner_layout);
        conditionSpinner = view.findViewById(R.id.search_condition_spinner);

        conditionSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                hideKeyboard();
                return true;
            }
        });

        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(selectedText != null) {
                    if(!text.equals("Condition")) {
                        selectedText.setTextColor(Color.BLACK);
                        performSearch(text);
                    }
                    else {
                        resetAll();
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.condition, R.layout.spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);
    }

    public void initTypeSpinner() {
        typeLayout =  view.findViewById(R.id.search_type_spinner_layout);
        typeSpinner = view.findViewById(R.id.search_type_spinner);

        typeSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                hideKeyboard();
                return true;
            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(selectedText != null) {
                    if(!text.equals("Type")) {
                        selectedText.setTextColor(Color.BLACK);
                        performSearch(text);
                    }
                    else {
                        resetAll();
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.type, R.layout.spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
    }

    public void resetAll() {
        textbooks.clear();
        textbookAdapter.notifyDataSetChanged();
        getActivity().setTitle("Search for a textbook!");
    }

    public void hideKeyboard() {
        Context context = getActivity().getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
