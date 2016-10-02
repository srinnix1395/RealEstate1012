package com.qtd.realestate1012.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dell on 7/31/2016.
 */
public class BoardFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.layoutNoBoard)
    RelativeLayout layoutNoBoard;

    @BindView(R.id.imvNoBoard)
    ImageView imvNoBoard;

    @BindView(R.id.fabAddBoard)
    FloatingActionButton fabAddBoard;

    private ArrayList<Board> arrayListBoards;
    private BoardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initViews();
    }

    private void initData() {
        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
            arrayListBoards = databaseHelper.getAllBoards();
            arrayListBoards.add(arrayListBoards.get(0));
            arrayListBoards.add(arrayListBoards.get(0));
            arrayListBoards.add(arrayListBoards.get(0));
            arrayListBoards.add(arrayListBoards.get(0));
            arrayListBoards.add(arrayListBoards.get(0));
            arrayListBoards.add(arrayListBoards.get(0));
            arrayListBoards.add(arrayListBoards.get(0));
        } else {
            arrayListBoards = new ArrayList<>();
        }
        adapter = new BoardAdapter(arrayListBoards, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (arrayListBoards.size() == 0 || getUserVisibleHint()) {
            refreshLayout.setRefreshing(true);
            requestData();
        }
    }

    private void initViews() {
        Glide.with(this)
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

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            if (getUserVisibleHint()) {
                AlertUtils.showSnackBarNoInternet(fabAddBoard);
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
                                handleResponseSuccess(response);
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
        } else {
            refreshLayout.setRefreshing(false);
        }
    }

    private void handleResponseSuccess(final JSONObject response) {
        Single.fromCallable(new Callable<ArrayList<Board>>() {
            @Override
            public ArrayList<Board> call() throws Exception {
                ArrayList<Board> boardNews = ProcessJson.getFavoriteBoards(response);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                databaseHelper.syncDataBoard(boardNews);
                return boardNews;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<Board>>() {
                    @Override
                    public void onSuccess(ArrayList<Board> value) {
                        int size = arrayListBoards.size();
                        arrayListBoards.clear();
                        adapter.notifyItemRangeRemoved(0, size);

                        arrayListBoards.addAll(value);
                        adapter.notifyItemRangeInserted(0, arrayListBoards.size());

                        if (arrayListBoards.size() == 0) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            layoutNoBoard.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            layoutNoBoard.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        Log.e("board fragment", "onError: Đã có lỗi trong quá trình lấy sync data");

                    }
                });
    }

    @OnClick(R.id.fabAddBoard)
    void onClick() {
        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intentLogin, AppConstant.REQUEST_CODE_SIGN_IN);
            return;
        }

        Intent intent = new Intent(getActivity(), CreateBoardActivity.class);

        String listBoard[] = new String[arrayListBoards.size()];
        for (int i = 0; i < arrayListBoards.size(); i++) {
            listBoard[i] = arrayListBoards.get(i).getName();
        }
        intent.putExtra(ApiConstant.LIST_BOARD, listBoard);

        startActivityForResult(intent, AppConstant.REQUEST_CODE_CREATE_BOARD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case AppConstant.REQUEST_CODE_CREATE_BOARD: {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    arrayListBoards.add((Board) data.getParcelableExtra(ApiConstant.BOARD));
                    adapter.notifyItemInserted(arrayListBoards.size() - 1);
                }
                break;
            }
        }

    }

    public void clearUserData() {
        int size = arrayListBoards.size();
        arrayListBoards.clear();
        adapter.notifyItemRangeRemoved(0, size);
        refreshLayout.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.INVISIBLE);
                layoutNoBoard.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }
}
