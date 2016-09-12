package com.qtd.realestate1012.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.SearchesAdapter;
import com.qtd.realestate1012.callback.FavoriteFragmentCallback;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.ItemSearch;
import com.qtd.realestate1012.utils.ServiceUtils;

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

    @BindView(R.id.layoutNoSearches)
    RelativeLayout layoutNoSearches;

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

        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            refreshLayout.setEnabled(false);
        }

        if (arrayList.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            layoutNoSearches.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutNoSearches.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestData();
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }


    }



    @OnClick(R.id.tvSearch)
    void onClick() {
        if (callback != null) {
            callback.showSearchFragment();
        }
    }
}
