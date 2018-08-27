package com.rickybooks.rickybooks.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.R;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView homeBanner = view.findViewById(R.id.home_banner);
        homeBanner.setAdjustViewBounds(true);
        homeBanner.setImageDrawable(getResources().getDrawable(R.drawable.rickybooks_banner));

        Button aboutButton = view.findViewById(R.id.home_about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutButtonPressed();
            }
        });

        return view;
    }

    public void aboutButtonPressed() {
        MainActivity activity = (MainActivity) getActivity();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity,
                R.style.AppTheme_AlertDialogTheme);

        TextView intro = new TextView(activity);
        intro.setText(R.string.intro);
        intro.setTextSize(16);
        intro.setTextColor(getResources().getColor(R.color.black));
        intro.setId(TextView.generateViewId());

        ImageView rickyPhoto = new ImageView(activity);
        rickyPhoto.setImageDrawable(getResources().getDrawable(R.drawable.rickydam));
        rickyPhoto.setAdjustViewBounds(true);
        rickyPhoto.setMaxWidth(400);
        rickyPhoto.setMaxHeight(400);

        RelativeLayout.LayoutParams rickyPhotoParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rickyPhotoParams.addRule(RelativeLayout.BELOW, intro.getId());
        rickyPhotoParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rickyPhoto.setLayoutParams(rickyPhotoParams);

        RelativeLayout parentLayout = new RelativeLayout(activity);
        parentLayout.setPadding(15, 15, 15, 15);
        parentLayout.addView(intro);
        parentLayout.addView(rickyPhoto);

        alertDialog.setView(parentLayout);

        alertDialog.show();
    }
}
