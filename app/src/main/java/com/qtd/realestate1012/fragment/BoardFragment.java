package com.qtd.realestate1012.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.CreateBoardActivity;
import com.qtd.realestate1012.activity.LoginActivity;
import com.qtd.realestate1012.adapter.BoardAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

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

    @BindView(R.id.layoutNoBoard)
    RelativeLayout layoutNoBoard;

    @BindView(R.id.imvNoBoard)
    ImageView imvNoBoard;

    private ArrayList<Board> arrayListBoards;
    private BoardAdapter adapter;
    private String jsonBoard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_board, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestData();
    }

    private void initViews() {
        Glide.with(view.getContext())
                .load(R.drawable.house_paint)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(324, 324)
                .into(imvNoBoard);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        arrayListBoards = new ArrayList<>();


        adapter = new BoardAdapter(arrayListBoards, false);

        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);

        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            refreshLayout.setEnabled(false);
        }

        if (arrayListBoards.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            layoutNoBoard.setVisibility(View.VISIBLE);


        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutNoBoard.setVisibility(View.INVISIBLE);
        }

//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
//        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
//        if(isMapIndex) {
//            params.setScrollFlags(0);
//            appBarLayoutParams.setBehavior(null);
//            mAppBarLayout.setLayoutParams(appBarLayoutParams);
//        } else {
//            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
//            appBarLayoutParams.setBehavior(new AppBarLayout.Behavior());
//            mAppBarLayout.setLayoutParams(appBarLayoutParams);
//        }
    }

    private void requestData() {
        refreshLayout.setRefreshing(true);
        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            if (this.getUserVisibleHint()) {
                FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddBoard);
                AlertUtils.showSnackBarNoInternet(fab);
            }
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
                                int size = arrayListBoards.size();
                                arrayListBoards.clear();
                                adapter.notifyItemRangeRemoved(0, size);
                                arrayListBoards.addAll(ProcessJson.getFavoriteBoards(response));
                                adapter.notifyItemRangeInserted(0, arrayListBoards.size());

                                if (arrayListBoards.size() == 0) {
                                    recyclerView.setVisibility(View.INVISIBLE);
                                    layoutNoBoard.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layoutNoBoard.setVisibility(View.INVISIBLE);
                                }
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    refreshLayout.setRefreshing(false);

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
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(true);
            requestData();
        }
    }
}
