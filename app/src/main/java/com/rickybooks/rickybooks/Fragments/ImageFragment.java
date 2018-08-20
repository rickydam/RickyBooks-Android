package com.rickybooks.rickybooks.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.rickybooks.rickybooks.R;

public class ImageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        PhotoView fullscreenImageView = view.findViewById(R.id.fullscreen_imageview);
        Bundle bundle = getArguments();
        String imageUrl = bundle.getString("ImageUrl");
        Glide.with(this).load(imageUrl).into(fullscreenImageView);
        return view;
    }
}
