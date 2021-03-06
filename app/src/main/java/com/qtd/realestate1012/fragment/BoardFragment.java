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
        initViews();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshLayout.setRefreshing(true);
        refreshData();
    }

    private void refreshData() {
        if (HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            if (!refreshLayout.isEnabled()) {
                refreshLayout.setEnabled(true);
                refreshLayout.setRefreshing(true);
            }
            if (!ServiceUtils.isNetworkAvailable(getContext())) {
                if (getParentFragment().isVisible() && getUserVisibleHint()) {
                    AlertUtils.showSnackBarNoInternet(getView());
                }
                getDataFromDatabase();
            } else {
                getDataFromServer();
            }
        } else {
            refreshLayout.setRefreshing(false);
            refreshLayout.setEnabled(false);
            recyclerView.setVisibility(View.INVISIBLE);
            layoutNoBoard.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        Glide.with(this)
                .load(R.drawable.house_paint)
                .override(324, 324)
                .into(imvNoBoard);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }

    private void initData() {
        arrayListBoards = new ArrayList<>();
        adapter = new BoardAdapter(arrayListBoards, false);

        recyclerView.setAdapter(adapter);
    }

    public void getDataFromDatabase() {
        Single.fromCallable(new Callable<ArrayList<Board>>() {
            @Override
            public ArrayList<Board> call() throws Exception {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                return databaseHelper.getListBoard();
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

                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        refreshLayout.setRefreshing(false);
                        Log.e("board fragment", "onError: Đã có lỗi trong quá trình lấy sync data");
                    }
                });

    }

    private void getDataFromServer() {
        String idUserLoggedIn = HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "-1");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiConstant._ID, idUserLoggedIn);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiConstant.URL_WEB_SERVICE_GET_BOARDS,
                jsonObject, new Response.Listener<JSONObject>() {
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
    }

    private void handleResponseSuccess(final JSONObject response) {
        Single.fromCallable(new Callable<ArrayList<Board>>() {
            @Override
            public ArrayList<Board> call() throws Exception {
                ArrayList<Board> boardNews = ProcessJson.getListBoard(response);
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
            intentLogin.putExtra(ApiConstant._ID_HOUSE, "");
            startActivityForResult(intentLogin, AppConstant.REQUEST_CODE_SIGN_IN);
            return;
        }

        showActivityCreateBoard(true, new String[]{});
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            ArrayList<Board> boardArrayList = data.getParcelableArrayListExtra(ApiConstant.LIST_BOARD);

            if (boardArrayList != null) {
                String listBoard[] = new String[boardArrayList.size()];
                for (int i = 0; i < boardArrayList.size(); i++) {
                    listBoard[i] = boardArrayList.get(i).getName();
                }
                showActivityCreateBoard(false, listBoard);
            } else {
                showActivityCreateBoard(false, new String[]{});
            }
        }
    }

    private void showActivityCreateBoard(boolean useLocalBoard, String[] boardArrayList) {
        Intent intent = new Intent(getActivity(), CreateBoardActivity.class);

        if (useLocalBoard) {
            String listBoard[] = new String[arrayListBoards.size()];
            for (int i = 0; i < arrayListBoards.size(); i++) {
                listBoard[i] = arrayListBoards.get(i).getName();
            }
            intent.putExtra(ApiConstant.LIST_BOARD, listBoard);
        } else {
            intent.putExtra(ApiConstant.LIST_BOARD, boardArrayList);
        }
        startActivity(intent);
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
