package com.qtd.realestate1012.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.AllHouseActivity;
import com.qtd.realestate1012.adapter.HouseNewsAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.BottomSheetListBoard;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnHouse;
import com.qtd.realestate1012.messageevent.MessageEventClickSeeAllViewHolder;
import com.qtd.realestate1012.messageevent.MessageLikeBoardSuccess;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BunchHouse;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hamm.pinnedsectionlistview.PinnedSectionListView;

/**
 * Created by Dell on 7/30/2016.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "homeFragment";
    private View view;

    @BindView(R.id.listView)
    PinnedSectionListView listView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tvNoInternet)
    TextView tvError;

    private ArrayList<Object> arrayListHouseNews;
    private HouseNewsAdapter adapter;
    private ArrayList<Board> arrayListBoard;
    private boolean isInit;
    private JSONObject jsonBoard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initView();
    }

    private void initData() {
        jsonBoard = new JSONObject();
        arrayListBoard = new ArrayList<>();
        arrayListHouseNews = new ArrayList<>();
        adapter = new HouseNewsAdapter(arrayListHouseNews);
    }


    private void initView() {
        listView.setAdapter(adapter);

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            tvError.setVisibility(View.INVISIBLE);
        }

    }

    private void requestData() {
        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            AlertUtils.showSnackBarNoInternet(view);
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(AppConstant.USER_LOGGED_IN, HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false));
//            jsonRequest.put(ApiConstant._ID, HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "-1"));
            jsonRequest.put(ApiConstant._ID, "57ac3429f71b399577118c72");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_GET_NEWS, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "onResponse: " + response);
                    if (!response.getString(ApiConstant.RESULT).equals(ApiConstant.SUCCESS)) {
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText(R.string.errorConnection);

                        progressBar.setEnabled(false);
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }

                    jsonBoard = response.getJSONObject(ApiConstant.BOARD);
                    arrayListBoard.clear();
                    arrayListBoard.addAll(ProcessJson.getFavoriteBoards(jsonBoard));

                    arrayListHouseNews.clear();
                    arrayListHouseNews.addAll(ProcessJson.getArrayListHousesNew(arrayListBoard, response.getJSONArray(ApiConstant.LIST_HOUSE)));

                    adapter.notifyDataSetChanged();

                    tvError.setVisibility(View.INVISIBLE);

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
                tvError.setText(R.string.errorConnection);
                tvError.setVisibility(View.VISIBLE);
                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && ServiceUtils.isNetworkAvailable(view.getContext())) {
            arrayListHouseNews.clear();
            adapter.notifyDataSetChanged();

            progressBar.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.INVISIBLE);

            requestData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        if (!isInit) {
            requestData();
            isInit = true;
            return;
        }

        if (this.isVisible()) {
            requestData();
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void handleEventClickImvHeartOnHouseNew(MessageClickImvHeartOnHouse event) {
        openDialogBoard(event.id);
    }

    public void openDialogBoard(String id) {
        BottomSheetListBoard dialog = new BottomSheetListBoard();
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant._ID, id);
        bundle.putString(ApiConstant.BOARD, jsonBoard.toString());
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Subscribe
    public void handleEventSuccessFavoriteHouse(MessageLikeBoardSuccess messageEvent) {
        JSONObject response = messageEvent.message;
        String id = null;
        boolean action = false;
        try {
            id = response.getString(ApiConstant._ID_HOUSE);
            action = response.getString(ApiConstant.ACTION).equals(ApiConstant.ACTION_ADD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Object object : arrayListHouseNews) {
            if (object instanceof BunchHouse) {
                ((BunchHouse) object).resetImvHeart(id, action);
                break;
            }
        }

        AlertUtils.showToastSuccess(getContext(), R.drawable.ic_heart_white_large, R.string.homeSaved);
    }

    @Subscribe
    public void handleEventClickSeeAllViewHolder(MessageEventClickSeeAllViewHolder message) {
        Intent intent = new Intent(getContext(), AllHouseActivity.class);
        intent.putExtra(ApiConstant.TYPE, message.type);
        intent.putExtra(ApiConstant.LIST_BOARD, jsonBoard.toString());
        getContext().startActivity(intent);
    }

    public String getJSONBoard() {
        return jsonBoard.toString();
    }
}
