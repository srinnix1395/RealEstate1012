package com.qtd.realestate1012.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.HouseAdapter;
import com.qtd.realestate1012.callback.FavoriteFragmentCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.model.FavoriteHouse;
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
public class HomesFavoriteFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.layoutNoHouses)
    RelativeLayout layoutNoHouses;

    private ArrayList<CompactHouse> arrayListHouses;
    private HouseAdapter adapter;
    private FavoriteFragmentCallback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (FavoriteFragmentCallback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_homes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initViews();
    }

    private void initData() {
        arrayListHouses = new ArrayList<>();
        adapter = new HouseAdapter(arrayListHouses);

        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            Single.fromCallable(new Callable<ArrayList<CompactHouse>>() {
                @Override
                public ArrayList<CompactHouse> call() throws Exception {
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                    return databaseHelper.getAllFavoriteHouse();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<ArrayList<CompactHouse>>() {
                        @Override
                        public void onSuccess(ArrayList<CompactHouse> value) {
                            arrayListHouses.addAll(value);
                            adapter.notifyDataSetChanged();

                            if (arrayListHouses.size() == 0) {
                                recyclerView.setVisibility(View.INVISIBLE);
                                layoutNoHouses.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                layoutNoHouses.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            error.printStackTrace();
                            Log.e("homes favorite fragment", "onError: Đã có lỗi trong quá trình lấy sync data");
                        }
                    });
        }

    }

    private void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        recyclerView.setAdapter(adapter);

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

        if (arrayListHouses.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            layoutNoHouses.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutNoHouses.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (arrayListHouses.size() == 0 || getUserVisibleHint()) {
            refreshLayout.setRefreshing(true);
            requestData();
        }
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            if (this.getUserVisibleHint()) {
                AlertUtils.showSnackBarNoInternet(getView());
            }
            refreshLayout.setRefreshing(false);
            return;
        }

        if (HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            String idUserLoggedIn = HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "-1");
            String url = ApiConstant.URL_WEB_SERVICE_GET_FAVORITE_HOUSES;

            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put(ApiConstant._ID, idUserLoggedIn);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        switch (response.getString(ApiConstant.RESULT)) {
                            case ApiConstant.FAILED: {
                                Toast.makeText(getActivity(), R.string.errorConnection, Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
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
        Single.fromCallable(new Callable<ArrayList<FavoriteHouse>>() {
            @Override
            public ArrayList<FavoriteHouse> call() throws Exception {
                ArrayList<CompactHouse> compactHouses = ProcessJson.getListCompactHouse(response);

                if (compactHouses.size() > 0) {
                    response.put(ApiConstant.HAS_BOARD, true);
                } else {
                    response.put(ApiConstant.HAS_BOARD, false);
                }
                ArrayList<Board> arrayListBoard = ProcessJson.getFavoriteBoards(response);

                ArrayList<FavoriteHouse> arrayListFavoriteHouse = new ArrayList<>();
                for (Board board : arrayListBoard) {
                    if (compactHouses.size() == 0) {
                        break;
                    } else {
                        for (int i = compactHouses.size() - 1; i >= 0; i--) {
                            if (board.getListHouse().contains(compactHouses.get(i).getId())) {
                                arrayListFavoriteHouse.add(new FavoriteHouse(compactHouses.get(i), board.getId()));
                                compactHouses.remove(i);
                            }
                        }
                    }
                }

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                databaseHelper.syncDataFavoriteHouse(arrayListFavoriteHouse);

                return arrayListFavoriteHouse;
            }
        }).observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<FavoriteHouse>>() {
                    @Override
                    public void onSuccess(ArrayList<FavoriteHouse> value) {
                        int size = arrayListHouses.size();
                        arrayListHouses.clear();
                        adapter.notifyItemRangeRemoved(0, size);
                        arrayListHouses.addAll(value);
                        adapter.notifyItemRangeInserted(0, arrayListHouses.size());

                        if (arrayListHouses.size() == 0) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            layoutNoHouses.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            layoutNoHouses.setVisibility(View.INVISIBLE);
                        }
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }

    @OnClick(R.id.tvSearch)
    void onClick() {
        if (callback != null) {
            callback.showSearchFragment();
        }
    }

    public void clearUserData() {
        int size = arrayListHouses.size();
        arrayListHouses.clear();
        adapter.notifyItemRangeRemoved(0, size);
        refreshLayout.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.INVISIBLE);
                layoutNoHouses.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_SIGN_IN && resultCode == Activity.RESULT_OK) {

        }
    }
}
