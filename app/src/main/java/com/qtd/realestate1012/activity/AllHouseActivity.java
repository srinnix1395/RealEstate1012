package com.qtd.realestate1012.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.HouseAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 8/17/2016.
 */
public class AllHouseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String type;
    private HouseAdapter adapter;
    private ArrayList<CompactHouse> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_house);
        ButterKnife.bind(this);
        initData();
        initViews();
        requestData();
    }

    private void initData() {
        arrayList = new ArrayList<>();
        adapter = new HouseAdapter(arrayList);
    }

    private void requestData() {
        if (ServiceUtils.isNetworkAvailable(this)) {
            String url = ApiConstant.URL_WEB_SERVICE_GET_ALL_HOUSE;

            JSONObject jsonRequest = new JSONObject();
            if (HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
                try {
                    jsonRequest.put(ApiConstant._ID, HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "-1"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
            HousieApplication.getInstance().addToRequestQueue(request);
        } else {

            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(type);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        progressBar.setEnabled(true);
    }


}
