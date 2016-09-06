package com.qtd.realestate1012.activity;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.HouseHasHeartAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.messageevent.MessageRemoveHouseToBoard;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 9/5/2016.
 */
public class BoardDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvError)
    TextView tvError;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ProgressDialog progressDialog;

    private ArrayList<CompactHouse> arrayListHouse;
    private HouseHasHeartAdapter adapter;

    private String id;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);
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
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant
                .URL_WEB_SERVICE_DETAIL_BOARD, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            tvError.setText(R.string.errorProcessing);
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleResponseGetDataSuccess(response);
                            break;
                        }
                    }

                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);
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
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseGetDataSuccess(JSONObject response) {
        arrayListHouse.addAll(ProcessJson.getListCompactHouse(response));
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        Intent data = getIntent();
        if (data != null) {
            id = data.getStringExtra(ApiConstant._ID);
            name = data.getStringExtra(ApiConstant.NAME);
        }

        arrayListHouse = new ArrayList<>();
        adapter = new HouseHasHeartAdapter(arrayListHouse);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setRemoveDuration(1000);

        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void handleEventRemoveHouseToBoard(final MessageRemoveHouseToBoard message) {
        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID_BOARD, id);
            jsonRequest.put(ApiConstant._ID_HOUSE, message.idHouse);
            jsonRequest.put(ApiConstant.ACTION, ApiConstant.ACTION_DELETE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_HANDLE_FAVORITE_HOUSES, jsonRequest
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            Toast.makeText(BoardDetailActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleResponseActionSuccess(message.idHouse);
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
                Toast.makeText(BoardDetailActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseActionSuccess(String idHouse) {
        progressDialog.dismiss();

        int position = 0;
        for (CompactHouse house : arrayListHouse) {
            if (house.getId().equals(idHouse)) {
                position = arrayListHouse.indexOf(house);
                arrayListHouse.remove(house);
                break;
            }
        }
        adapter.notifyItemRemoved(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onClickRenameBoard();
        return super.onOptionsItemSelected(item);
    }

    private void onClickRenameBoard() {

    }
}
