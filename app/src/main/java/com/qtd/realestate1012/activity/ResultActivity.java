package com.qtd.realestate1012.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.qtd.realestate1012.adapter.HouseHasHeartAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.BottomSheetListBoard;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnHouse;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.ProcessJson;

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
 * Created by DELL on 9/26/2016.
 */
public class ResultActivity extends AppCompatActivity {

    @BindView(R.id.tvError)
    TextView tvError;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private ArrayList<CompactHouse> arrayList;
    private HouseHasHeartAdapter adapter;
    private JSONObject jsonObject;
    private JsonObjectRequest request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        initData();
        initViews();
        requestData();
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

    @Override
    protected void onDestroy() {
        if (request != null && !request.isCanceled()) {
            request.cancel();
        }
        super.onDestroy();
    }

    private void initData() {
        Intent intent = getIntent();
        try {
            jsonObject = new JSONObject(intent.getStringExtra(ApiConstant.CRITERIA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayList = new ArrayList<>();
        adapter = new HouseHasHeartAdapter(arrayList);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(R.string.result);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    @OnClick(R.id.tvError)
    public void onClickTvError() {
        tvError.setVisibility(View.INVISIBLE);
        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
        requestData();
    }

    private void requestData() {
        request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_SEARCH_HOUSE, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            progressBar.setEnabled(false);
                            progressBar.setVisibility(View.INVISIBLE);
                            tvError.setText(R.string.errorProcessing);
                            tvError.setVisibility(View.VISIBLE);
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleResponseFilterSuccess(response);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    tvError.setText(R.string.errorProcessing);
                    tvError.setVisibility(View.VISIBLE);
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

    private void handleResponseFilterSuccess(final JSONObject response) {
        Single.fromCallable(new Callable<ArrayList<CompactHouse>>() {
            @Override
            public ArrayList<CompactHouse> call() throws Exception {
                return ProcessJson.getListCompactHouse(response);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<CompactHouse>>() {
                    @Override
                    public void onSuccess(ArrayList<CompactHouse> value) {
                        arrayList.clear();
                        arrayList.addAll(value);
                        adapter.notifyDataSetChanged();
                        progressBar.setEnabled(false);
                        progressBar.setVisibility(View.INVISIBLE);

                        if (arrayList.size() == 0) {
                            tvError.setText(R.string.noResult);
                            tvError.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        tvError.setText(R.string.errorProcessing);
                        tvError.setVisibility(View.VISIBLE);
                        progressBar.setEnabled(false);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Subscribe
    public void handleMessageLikeHouse(MessageClickImvHeartOnHouse message) {
        if (HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            openDialogBoard(message.id);
        } else {
            Intent intent = new Intent(ResultActivity.this, LoginActivity.class);
            intent.putExtra(ApiConstant._ID_HOUSE, message.id);
            startActivityForResult(intent, AppConstant.REQUEST_CODE_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            final String idHouse = data.getStringExtra(ApiConstant._ID_HOUSE);

            Single.fromCallable(new Callable<ArrayList<String>>() {
                @Override
                public ArrayList<String> call() throws Exception {
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(ResultActivity.this);
                    return databaseHelper.getListIdFavoriteHouse();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<ArrayList<String>>() {
                        @Override
                        public void onSuccess(ArrayList<String> value) {
                            for (CompactHouse house : arrayList) {
                                if (value.contains(house.getId())) {
                                    house.setLiked(true);
                                }
                            }
                            adapter.notifyDataSetChanged();

                            openDialogBoard(idHouse);
                        }

                        @Override
                        public void onError(Throwable error) {
                            error.printStackTrace();
                        }
                    });
        }
    }

    public void openDialogBoard(String id) {
        BottomSheetListBoard dialog = new BottomSheetListBoard();
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant._ID, id);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "dialog");
    }


}
