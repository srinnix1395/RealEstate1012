package com.qtd.realestate1012.fragment;

import android.app.Activity;
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
import com.qtd.realestate1012.activity.HouseDetailActivity;
import com.qtd.realestate1012.activity.LoginActivity;
import com.qtd.realestate1012.adapter.HouseNewsAdapter;
import com.qtd.realestate1012.callback.ActivityCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.BottomSheetListBoard;
import com.qtd.realestate1012.database.DatabaseHelper;
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
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hamm.pinnedsectionlistview.PinnedSectionListView;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    @BindView(R.id.tvError)
    TextView tvError;

    private ArrayList<Object> arrayListHouseNews;
    private HouseNewsAdapter adapter;
    private JsonObjectRequest requestGetNew;
    private Bundle bundle;
    private ActivityCallback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = (ActivityCallback) getActivity();
    }

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
        requestData();

        if (bundle != null) {
            Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
            intent.putExtra(ApiConstant._ID, bundle.getString(ApiConstant._ID_HOUSE));
            getActivity().startActivity(intent);
        }
    }

    private void initData() {
        bundle = getArguments();
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
        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            if (arrayListHouseNews.size() == 0) {
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(R.string.noInternetConnection);
            }
            AlertUtils.showSnackBarNoInternet(view);
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(AppConstant.USER_LOGGED_IN, HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false));
            jsonRequest.put(ApiConstant._ID, HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "-1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestGetNew = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_GET_NEWS, jsonRequest, new Response.Listener<JSONObject>() {
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

                    handleResponseSuccess(response);
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
        HousieApplication.getInstance().addToRequestQueue(requestGetNew);
    }

    private void handleResponseSuccess(final JSONObject response) throws JSONException {
        Single.fromCallable(new Callable<ArrayList<Object>>() {
            @Override
            public ArrayList<Object> call() throws Exception {
                ArrayList<Board> boardArrayList = ProcessJson.getListBoard(response);
                if (boardArrayList.size() > 0) {
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                    databaseHelper.syncDataBoard(boardArrayList);
                }

                return ProcessJson.getArrayListHousesNew(response, response.getJSONArray(ApiConstant.LIST_HOUSE));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<Object>>() {
                    @Override
                    public void onSuccess(ArrayList<Object> value) {
                        arrayListHouseNews.clear();
                        arrayListHouseNews.addAll(value);
                        adapter.notifyDataSetChanged();

                        tvError.setVisibility(View.INVISIBLE);

                        progressBar.setEnabled(false);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (ServiceUtils.isNetworkAvailable(view.getContext())) {
                arrayListHouseNews.clear();
                adapter.notifyDataSetChanged();

                tvError.setVisibility(View.INVISIBLE);

                progressBar.setEnabled(true);
                progressBar.setVisibility(View.VISIBLE);

                requestData();
            }
        } else {
            if (requestGetNew != null && !requestGetNew.isCanceled()) {
                requestGetNew.cancel();
            }
        }
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
    public void handleEventClickImvHeartOnHouseNew(MessageClickImvHeartOnHouse message) {
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
            String idHouse = data.getStringExtra(ApiConstant._ID_HOUSE);
            updateData(idHouse);
            return;
        }

        if (requestCode == AppConstant.REQUEST_CODE_DETAIL_KIND && resultCode == Activity.RESULT_OK) {
            ArrayList<String> listId = data.getStringArrayListExtra(ApiConstant.LIST_ID);
            for (Object object : arrayListHouseNews) {
                if (object instanceof BunchHouse) {
                    ((BunchHouse) object).updateImvHeart(listId);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void updateData(final String idHouse) {
        Single.fromCallable(new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() throws Exception {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(HomeFragment.this.getContext());
                return databaseHelper.getListIdFavoriteHouse();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<String>>() {
                    @Override
                    public void onSuccess(ArrayList<String> value) {
                        for (Object obj : arrayListHouseNews) {
                            if (obj instanceof BunchHouse) {
                                ((BunchHouse) obj).resetImvHeart(value);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        if (idHouse != null) {
                            openDialogBoard(idHouse);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
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
        String idHouse = null;
        boolean isLike = false;
        try {
            idHouse = response.getString(ApiConstant._ID_HOUSE);
            isLike = response.getString(ApiConstant.ACTION).equals(ApiConstant.ACTION_ADD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Object object : arrayListHouseNews) {
            if (object instanceof BunchHouse) {
                ((BunchHouse) object).resetImvHeart(idHouse, isLike);
            }
        }
        adapter.notifyDataSetChanged();

        if (isLike) {
            AlertUtils.showToastSuccess(getContext(), R.drawable.ic_heart_white_large, R.string.homeSaved);
        } else {
            AlertUtils.showToastSuccess(getContext(), R.drawable.ic_heart_broken, R.string.homeUnsaved);
        }

        callback.updateDataFavoriteFragment();
    }

    @Subscribe
    public void handleEventClickSeeAllViewHolder(MessageEventClickSeeAllViewHolder message) {
        Intent intent = new Intent(getContext(), AllHouseActivity.class);
        intent.putExtra(ApiConstant.TYPE, message.type);
        startActivityForResult(intent, AppConstant.REQUEST_CODE_DETAIL_KIND);
    }

    @OnClick(R.id.tvError)
    void onClickError() {
        tvError.setVisibility(View.INVISIBLE);

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        requestData();
    }

    public void clearUserData() {
        for (Object obj : arrayListHouseNews) {
            if (obj instanceof BunchHouse) {
                ((BunchHouse) obj).clearHeart();
            }
        }
        adapter.notifyDataSetChanged();
    }
}
