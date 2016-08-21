package com.qtd.realestate1012.fragment;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.CreateBoardActivity;
import com.qtd.realestate1012.activity.LoginActivity;
import com.qtd.realestate1012.adapter.BoardAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;
import com.qtd.realestate1012.utils.AlertUtils;

import org.json.JSONException;
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
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestData();
    }

    private void initViews() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            refreshLayout.setEnabled(false);
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        arrayListBoards = new ArrayList<>();
        adapter = new BoardAdapter(arrayListBoards);

        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            AlertUtils.showSnackBarNoInternet(view);
            refreshLayout.setRefreshing(false);
            return;
        }

        if (HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            String url = ApiConstant.URL_WEB_SERVICE_GET_BOARDS;

            String idUserLoggedIn = HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "-1");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(ApiConstant._ID, idUserLoggedIn);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        switch (response.getString(ApiConstant.RESULT)) {
                            case ApiConstant.FAILED: {
                                Toast.makeText(getActivity(), R.string.errorConnection, Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case ApiConstant.SUCCESS: {
                                arrayListBoards.clear();
                                arrayListBoards.addAll(ProcessJson.getFavoriteBoards(response));
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        refreshLayout.setRefreshing(false);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.errorConnection, Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            });
            HousieApplication.getInstance().addToRequestQueue(request);
        }
    }

    @OnClick(R.id.fabAddBoard)
    void onClick() {
        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intentLogin, AppConstant.REQUEST_CODE_SIGN_IN);
            return;
        }

        Intent intent = new Intent(getActivity(), CreateBoardActivity.class);

        String listBoard = "";
        for (Board board : arrayListBoards) {
            listBoard += (board.getName() + "-");
        }
        intent.putExtra(ApiConstant.LIST_BOARD, listBoard);

        startActivityForResult(intent, AppConstant.REQUEST_CODE_CREATE_BOARD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_CREATE_BOARD && resultCode == Activity.RESULT_OK && data != null) {
            requestData();
        }
    }
}
