package com.qtd.realestate1012.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.SearchesAdapter;
import com.qtd.realestate1012.callback.FavoriteFragmentCallback;
import com.qtd.realestate1012.model.ItemSearch;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/31/2016.
 */
public class SearchesFavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;

    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;
    private FavoriteFragmentCallback callback;
    private ArrayList<ItemSearch> arrayList;
    private SearchesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (FavoriteFragmentCallback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite_searches, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initViews();
    }

    private void initData() {
        arrayList = new ArrayList<>();
        adapter = new SearchesAdapter(arrayList);
    }

    private void initViews() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public void onStart() {
        super.onStart();

        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = recyclerView.getScrollY();
                if (scrollY == 0)
                    refreshLayout.setEnabled(true);
                else
                    refreshLayout.setEnabled(false);

            }
        };
        refreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);
        requestData();
    }

    @Override
    public void onStop() {
        refreshLayout.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        super.onStop();
    }

    private void requestData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 3000);
    }



    @OnClick(R.id.tvSearch)
    void onClick(){
        callback.showSearchFragment();
    }


}
