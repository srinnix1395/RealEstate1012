package com.qtd.realestate1012.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.BoardAdapter;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.utils.ProcessJSON;
import com.qtd.realestate1012.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/31/2016.
 */
public class BoardFragment extends Fragment {
    private View view;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout refreshLayout;

    private ArrayList<Board> arrayListBoards;
    private BoardAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_board, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
        int id = HousieApplication.getInstance().getSharedPreUtils().getInt(AppConstant.USER_LOGGED_IN, -1);
        if (id != -1) {
            initRecyclerView(id);
        }
    }

    private void initView() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestBoard(HousieApplication.getInstance().getSharedPreUtils().getInt(AppConstant.USER_LOGGED_IN, -1));
            }
        });
    }

    private void initRecyclerView(int idUserLoggedIn) {
        arrayListBoards = new ArrayList<>();
        adapter = new BoardAdapter(arrayListBoards);


        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);

        requestBoard(idUserLoggedIn);
    }

    private void requestBoard(int idUserLoggedIn) {
        if (idUserLoggedIn != -1) {
            String url = StringUtils.getURLBoardFavorite(idUserLoggedIn);
            JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    arrayListBoards.clear();
                    arrayListBoards.addAll(ProcessJSON.getBoardFavorite(response));
                    adapter.notifyDataSetChanged();

                    if (refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(false);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(false);
                    }
                }
            });
            HousieApplication.getInstance().addToRequestQueue(arrayRequest);
            return;
        }


    }

    @OnClick(R.id.fabAddBoard)
    void onClick(){

    }
}
