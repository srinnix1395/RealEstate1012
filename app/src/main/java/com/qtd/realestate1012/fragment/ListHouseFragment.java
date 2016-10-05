package com.qtd.realestate1012.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.LoginActivity;
import com.qtd.realestate1012.adapter.HouseCardViewAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.BottomSheetListBoard;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnCardHouseViewHolder;
import com.qtd.realestate1012.messageevent.MessageLikeBoardSuccess;
import com.qtd.realestate1012.model.CompactHouse;
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
 * Created by DELL on 9/1/2016.
 */
public class ListHouseFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tvError)
    TextView tvError;

    private HouseCardViewAdapter adapter;
    private ArrayList<CompactHouse> arrayList;
    private String url;
    private String type;
    private boolean listHouseNull;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_house, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initViews();

        if (url.equals(ApiConstant.URL_WEB_SERVICE_GET_ALL_HOUSE_OF_KIND)) {
            requestData();
        } else {
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            if (listHouseNull) {
                tvError.setText(R.string.noInternetConnection);
                tvError.setVisibility(View.VISIBLE);
            } else if (arrayList.size() == 0) {
                tvError.setText(R.string.noHouses);
                tvError.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initData() {
        arrayList = new ArrayList<>();

        Bundle arguments = getArguments();
        url = arguments.getString(ApiConstant.URL_WEB_SERVICE);
        switch (url) {
            case ApiConstant.URL_WEB_SERVICE_GET_ALL_HOUSE_OF_KIND: {
                arrayList = new ArrayList<>();
                type = arguments.getString(ApiConstant.TYPE);
                break;
            }
            case ApiConstant.URL_WEB_SERVICE_GET_ALL_HOUSE: {
                JSONObject listHouse;
                try {
                    listHouse = new JSONObject(arguments.getString(ApiConstant.LIST_HOUSE));
                    arrayList = ProcessJson.getListCompactHouse(listHouse);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listHouseNull = true;
                }
                break;
            }
        }

        adapter = new HouseCardViewAdapter(arrayList);

    }

    @OnClick(R.id.tvError)
    public void onClickTvError() {
        if (url.equals(ApiConstant.URL_WEB_SERVICE_GET_ALL_HOUSE_OF_KIND)) {
            tvError.setVisibility(View.GONE);

            progressBar.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);
            requestData();
        }
    }

    private void initViews() {
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        progressBar.setEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant.PROPERTY_TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            tvError.setText(R.string.errorProcessing);
                            tvError.setVisibility(View.VISIBLE);
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
    }

    private void handleResponseSuccess(final JSONObject response) {
        Single.fromCallable(new Callable<ArrayList<CompactHouse>>() {
            @Override
            public ArrayList<CompactHouse> call() throws Exception {
                ArrayList<CompactHouse> arrayList = ProcessJson.getListCompactHouse(response);

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                ArrayList<String> arrayListId = databaseHelper.getListIdFavoriteHouse();

                for (CompactHouse house : arrayList) {
                    if (arrayListId.contains(house.getId())) {
                        house.setLiked(true);
                    }
                }
                return arrayList;

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<CompactHouse>>() {
                    @Override
                    public void onSuccess(ArrayList<CompactHouse> value) {
                        arrayList.clear();
                        arrayList.addAll(value);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void handleEventLikeHouseCardHouseViewHolder(MessageClickImvHeartOnCardHouseViewHolder message) {
        if (HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            openDialogBoard(message.id);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
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
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(ListHouseFragment.this.getContext());
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
        dialog.show(getFragmentManager(), "dialog");
    }

    @Subscribe
    public void handleEventSuccessFavoriteHouse(MessageLikeBoardSuccess messageEvent) {
        JSONObject response = messageEvent.message;
        String id = null;
        boolean isLike = false;
        try {
            id = response.getString(ApiConstant._ID_HOUSE);
            isLike = response.getString(ApiConstant.ACTION).equals(ApiConstant.ACTION_ADD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (CompactHouse house : arrayList) {
            if (house.getId().equals(id)) {
                house.setLiked(isLike);
            }
        }

        AlertUtils.showToastSuccess(getContext(), R.drawable.ic_heart_white_large, R.string.homeSaved);
    }

    public void clearUserData() {
        for (CompactHouse house : arrayList) {
            house.setLiked(false);
        }
        adapter.notifyDataSetChanged();
    }
}
