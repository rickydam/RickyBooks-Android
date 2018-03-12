package com.example.rickydam.rickybooks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AccountFragment extends Fragment {
    public AccountFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Button registerButton = view.findViewById(R.id.account_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    ((MainActivity) getActivity()).replaceFragment("RegisterFragment");
                }
            }
        });

        Button loginButton = view.findViewById(R.id.account_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    ((MainActivity) getActivity()).replaceFragment("LoginFragment");
                }
            }
        });

        return view;
    }
}
