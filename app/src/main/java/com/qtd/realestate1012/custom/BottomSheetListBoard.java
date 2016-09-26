package com.qtd.realestate1012.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.CreateBoardActivity;
import com.qtd.realestate1012.adapter.BoardAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnBoard;
import com.qtd.realestate1012.messageevent.MessageLikeBoardSuccess;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BoardHasHeart;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.model.FavoriteHouse;
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
 * Created by DELL on 8/23/2016.
 */
public class BottomSheetListBoard extends BottomSheetDialogFragment {

    private View view;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private BoardAdapter adapter;
    private ArrayList<Board> arrayListBoards;
    private String idHouse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_dialog_list_board, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        Bundle bundle = getArguments();
        idHouse = bundle.getString(ApiConstant._ID);

        arrayListBoards = new ArrayList<>();
        adapter = new BoardAdapter(arrayListBoards, true);

        Single.fromCallable(new Callable<ArrayList<BoardHasHeart>>() {
            @Override
            public ArrayList<BoardHasHeart> call() throws Exception {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                return databaseHelper.getAllBoardHasHeart(idHouse);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<BoardHasHeart>>() {
                    @Override
                    public void onSuccess(ArrayList<BoardHasHeart> value) {
                        arrayListBoards.addAll(value);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }

    @OnClick(R.id.fabAddBoard)
    void onClick() {
        Intent intent = new Intent(getActivity(), CreateBoardActivity.class);

        String listBoard[] = new String[arrayListBoards.size()];
        for (int i = 0; i < arrayListBoards.size(); i++) {
            listBoard[i] = arrayListBoards.get(i).getName();
        }
        intent.putExtra(ApiConstant.LIST_BOARD, listBoard);

        startActivityForResult(intent, AppConstant.REQUEST_CODE_CREATE_BOARD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_CREATE_BOARD && resultCode == Activity.RESULT_OK && data != null) {
            arrayListBoards.add((Board) data.getParcelableExtra(ApiConstant.BOARD));
            adapter.notifyItemInserted(arrayListBoards.size() - 1);
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
    public void handleEventClickImvHeartOnBoard(final MessageClickImvHeartOnBoard event) {
        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID_BOARD, event.id);
            jsonRequest.put(ApiConstant._ID_HOUSE, idHouse);
            jsonRequest.put(ApiConstant.ACTION, event.action);
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
                            Toast.makeText(getContext(), R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            response.put(ApiConstant._ID_BOARD, event.id);
                            response.put(ApiConstant._ID_HOUSE, idHouse);
                            response.put(ApiConstant.ACTION, event.action);
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
                Toast.makeText(getContext(), R.string.errorProcessing, Toast.LENGTH_SHORT).show();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseSuccess(final JSONObject response) {
        Single.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                String action = response.getString(ApiConstant.ACTION);
                if (action.equals(ApiConstant.ACTION_ADD)) {
                    CompactHouse house = ProcessJson.getCompactHouse(response.getString(ApiConstant.HOUSE));
                    databaseHelper.insertHouseFavorite(new FavoriteHouse(house, response.getString(ApiConstant._ID_BOARD)));
                } else {
                    databaseHelper.deleteHouseFavorite(idHouse);
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Void>() {
                    @Override
                    public void onSuccess(Void value) {
                        EventBus.getDefault().post(new MessageLikeBoardSuccess(response));
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });

    }
}
