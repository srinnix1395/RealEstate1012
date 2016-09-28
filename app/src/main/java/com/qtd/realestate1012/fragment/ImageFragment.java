package com.qtd.realestate1012.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;

/**
 * Created by DELL on 9/27/2016.
 */
public class ImageFragment extends Fragment {

    public static ImageFragment newInstance(String urlImage) {
        Bundle args = new Bundle();
        args.putString(ApiConstant.IMAGE, urlImage);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView imageView = new ImageView(getContext());

        Bundle bundle = getArguments();
        String url = "";
        if (bundle != null) {
            url = bundle.getString(ApiConstant.IMAGE);
        }

        Glide.with(this)
                .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE_HOUSE + url)
                .placeholder(R.drawable.ic_apartment)
                .error(R.drawable.ic_apartment)
                .into(imageView);
        return imageView;
    }
}
