package com.qtd.realestate1012.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.HouseAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 9/7/2016.
 */
public class MyPostedHouseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tvError)
    TextView tvError;

    private ArrayList<CompactHouse> arrayList;
    private HouseAdapter adapter;
    private JsonObjectRequest request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posted_house);
        ButterKnife.bind(this);
        initData();
        initViews();
        requestData();
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(this)) {
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            tvError.setText(R.string.noInternetConnection);
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID, HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant
                .URL_WEB_SERVICE_MY_POST, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED:{
                            progressBar.setEnabled(false);
                            progressBar.setVisibility(View.INVISIBLE);
                            tvError.setText(R.string.errorProcessing);
                            tvError.setVisibility(View.VISIBLE);
                            break;
                        }
                        case ApiConstant.SUCCESS:{
                            handleResponseSuccess(response);
                            progressBar.setEnabled(false);
                            progressBar.setVisibility(View.INVISIBLE);
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
                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
                tvError.setText(R.string.errorProcessing);
                tvError.setVisibility(View.VISIBLE);
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseSuccess(JSONObject response) {
        arrayList.addAll(ProcessJson.getListCompactHouse(response));
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        arrayList = new ArrayList<>();
        adapter = new HouseAdapter(arrayList);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.yourPost);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);
    }

    @OnClick(R.id.fabPost)
    void onClickPost() {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvError)
    void onClickError() {
        arrayList.clear();
        adapter.notifyDataSetChanged();

        tvError.setVisibility(View.INVISIBLE);

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        requestData();
    }

    @Override
    protected void onDestroy() {
        if (request != null && !request.isCanceled()) {
            request.cancel();
        }
        super.onDestroy();
    }
}
