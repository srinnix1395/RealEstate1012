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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.HouseFavoriteAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 7/31/2016.
 */
public class HomesFavoriteFragment extends Fragment {
    private View view;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout refreshLayout;

    private ArrayList<CompactHouse> arrayListHouses;
    private HouseFavoriteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite_homes, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        initViews();
    }

    private void initData() {
        arrayListHouses = new ArrayList<>();
        adapter = new HouseFavoriteAdapter(arrayListHouses);
    }

    private void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            refreshLayout.setEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        requestData();
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            AlertUtils.showSnackBarNoInternet(view);
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
                                break;
                            }
                            case ApiConstant.SUCCESS: {
                                arrayListHouses.clear();
                                arrayListHouses.addAll(ProcessJson.getFavoriteHouses(response));
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
}
