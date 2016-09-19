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
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.LoginActivity;
import com.qtd.realestate1012.adapter.HouseCardViewAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.BottomSheetListBoard;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnCardHouseViewHolder;
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
    private String jsonBoard;
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

        jsonBoard = HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant.LIST_BOARD, "{}");
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

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
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

    private void handleResponseSuccess(JSONObject response) {
        arrayList.clear();
        arrayList.addAll(ProcessJson.getListCompactHouse(response));
        adapter.notifyDataSetChanged();
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
        if (requestCode == AppConstant.REQUEST_CODE_SIGN_IN && resultCode == Activity.RESULT_OK && data != null) {
            String idHouse = data.getStringExtra(ApiConstant._ID_HOUSE);
            openDialogBoard(idHouse);
        }
    }

    public void openDialogBoard(String id) {
        BottomSheetListBoard dialog = new BottomSheetListBoard();
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant._ID, id);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "dialog");
    }

    public void clearUserData() {
        for (CompactHouse house : arrayList) {
            house.setLiked(false);
        }
        adapter.notifyDataSetChanged();

        jsonBoard = "{}";
    }
}
