package com.qtd.realestate1012.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 7/31/2016.
 */
public class SearchesFavoriteFragment extends Fragment {

    private View view;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite_searches, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews();
    }

    private void initViews() {

    }
}
