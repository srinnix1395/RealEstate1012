package com.qtd.realestate1012.fragment;

import android.app.ProgressDialog;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.SearchesAdapter;
import com.qtd.realestate1012.callback.ActivityCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.messageevent.MessageRemoveSavedSearch;
import com.qtd.realestate1012.model.ItemSavedSearch;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
public class SearchesFavoriteFragment extends Fragment {
    private View view;

    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layoutNoSearches)
    RelativeLayout layoutNoSearches;

    private ActivityCallback activityCallback;
    private ArrayList<ItemSavedSearch> arrayList;
    private SearchesAdapter adapter;
    private JsonObjectRequest request;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCallback = (ActivityCallback) getActivity();
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
        initViews();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshLayout.setRefreshing(true);
        refreshData();
        EventBus.getDefault().register(this);
    }

    private void refreshData() {
        if (HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            if (!refreshLayout.isEnabled()) {
                refreshLayout.setEnabled(true);
                refreshLayout.setRefreshing(true);
            }
            if (!ServiceUtils.isNetworkAvailable(getContext())) {
                if (getUserVisibleHint()) {
                    AlertUtils.showSnackBarNoInternet(getView());
                }
                getDataFromDatabase();
            } else {
                getDataFromServer();
            }
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (request != null && !request.isCanceled()) {
            request.cancel();
        }
        super.onDestroy();
    }

    private void initViews() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            refreshLayout.setEnabled(false);
        }
    }

    private void initData() {
        arrayList = new ArrayList<>();
        adapter = new SearchesAdapter(arrayList);

        recyclerView.setAdapter(adapter);
    }


    private void getDataFromDatabase() {
        refreshLayout.setRefreshing(true);

        Single.fromCallable(new Callable<ArrayList<ItemSavedSearch>>() {
            @Override
            public ArrayList<ItemSavedSearch> call() throws Exception {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(SearchesFavoriteFragment.this.getContext());

                return databaseHelper.getListSavedSearch();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<ItemSavedSearch>>() {
                    @Override
                    public void onSuccess(ArrayList<ItemSavedSearch> value) {
                        int size = arrayList.size();
                        arrayList.clear();
                        adapter.notifyItemRangeRemoved(0, size);
                        arrayList.addAll(value);
                        adapter.notifyItemRangeInserted(0, arrayList.size());

                        if (arrayList.size() == 0) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            layoutNoSearches.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            layoutNoSearches.setVisibility(View.INVISIBLE);
                        }

                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        refreshLayout.setRefreshing(false);
                        Log.e("saved search fragment", "onError: Đã có lỗi trong quá trình lấy sync data");
                    }
                });
    }

    private void getDataFromServer() {

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID, HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_FIND_SEARCH, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            refreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(), R.string.errorProcessing, Toast.LENGTH_SHORT).show();
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
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), R.string.errorProcessing, Toast.LENGTH_SHORT).show();
            }
        });

        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseSuccess(final JSONObject response) {
        Single.fromCallable(new Callable<ArrayList<ItemSavedSearch>>() {
            @Override
            public ArrayList<ItemSavedSearch> call() throws Exception {
                ArrayList<ItemSavedSearch> arrayList = ProcessJson.getListItemSearch(response);

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(SearchesFavoriteFragment.this.getContext());
                databaseHelper.syncDataSavedSearch(arrayList);

                return arrayList;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<ItemSavedSearch>>() {
                    @Override
                    public void onSuccess(ArrayList<ItemSavedSearch> value) {
                        int size = arrayList.size();
                        arrayList.clear();
                        adapter.notifyItemRangeRemoved(0, size);
                        arrayList.addAll(value);
                        adapter.notifyItemRangeInserted(0, arrayList.size());

                        if (arrayList.size() == 0) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            layoutNoSearches.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            layoutNoSearches.setVisibility(View.INVISIBLE);
                        }

                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        refreshLayout.setRefreshing(false);
                        Log.e("saved search fragment", "onError: Đã có lỗi trong quá trình lấy sync data");
                    }
                });
    }


    @OnClick(R.id.tvSearch)
    void onClick() {
        if (activityCallback != null) {
            activityCallback.showSearchFragment();
        }
    }

    public void clearUserData() {
        int size = arrayList.size();
        arrayList.clear();
        adapter.notifyItemRangeRemoved(0, size);
        refreshLayout.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.INVISIBLE);
                layoutNoSearches.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    @Subscribe
    public void handleMessageRemoveSaveSearch(MessageRemoveSavedSearch message) {
        final int position = message.position;

        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.show();

        ItemSavedSearch item = arrayList.get(position);

        String url = String.format(ApiConstant.URL_WEB_SERVICE_REMOVE_SEARCH, item.getId());

        JsonObjectRequest requestRemove = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            progressDialog.dismiss();
                            arrayList.remove(position);
                            adapter.notifyItemRemoved(position);
                            AlertUtils.showToastSuccess(getContext(), R.drawable.ic_playlist_remove, R.string.removeSuccessfully);
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
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.errorProcessing, Toast.LENGTH_SHORT).show();
            }
        });

        HousieApplication.getInstance().addToRequestQueue(requestRemove);
    }
}
