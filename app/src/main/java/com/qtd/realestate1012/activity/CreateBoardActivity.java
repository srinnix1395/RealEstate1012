package com.qtd.realestate1012.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

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
 * Created by DELL on 8/18/2016.
 */
public class CreateBoardActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.etNameBoard)
    EditText etNameBoard;

    @BindView(R.id.tvNext)
    TextView tvNext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<String> listBoard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initData() {
        listBoard = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            String[] boards = intent.getStringArrayExtra(ApiConstant.LIST_BOARD);
            for (int i = 0; i < boards.length; i++) {
                listBoard.add(boards[i]);
            }
        }
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_dark_gray);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.DST_ATOP);
        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @OnClick({R.id.tvNext})
    void onClick() {
        onClickNext();
    }

    private void onClickNext() {
        String nameBoard = etNameBoard.getText().toString();

        if (nameBoard.length() == 0) {
            Toast.makeText(CreateBoardActivity.this, R.string.pleaseInputNameBoard, Toast.LENGTH_SHORT).show();
            return;
        }

        if (nameBoard.length() < 4) {
            Toast.makeText(CreateBoardActivity.this, R.string.nameBoardAtLeast3, Toast.LENGTH_SHORT).show();
            return;
        }

        for (String temp : listBoard) {
            if (nameBoard.equals(temp)) {
                Toast.makeText(CreateBoardActivity.this, R.string.boardHasExisted, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(CreateBoardActivity.this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        tvNext.setVisibility(View.INVISIBLE);
        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID, HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "-1"));
            jsonRequest.put(ApiConstant.NAME, nameBoard);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_CREATE_BOARD, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            Toast.makeText(CreateBoardActivity.this, R.string.errorConnection, Toast.LENGTH_SHORT).show();
                            progressBar.setEnabled(false);
                            progressBar.setVisibility(View.INVISIBLE);
                            tvNext.setVisibility(View.VISIBLE);
                            break;
                        }
                        case ApiConstant.SUCCESS: {
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
                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
                error.printStackTrace();
                Toast.makeText(CreateBoardActivity.this, R.string.errorConnection, Toast.LENGTH_SHORT).show();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseSuccess(final JSONObject response) {
        Single.fromCallable(new Callable<Board>() {
            @Override
            public Board call() throws Exception {
                Board board = ProcessJson.getBoard(response.getJSONObject(ApiConstant.BOARD));

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(CreateBoardActivity.this);
                databaseHelper.insertBoard(board);
                return board;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Board>() {
                    @Override
                    public void onSuccess(final Board value) {
                        AlertUtils.showToastSuccess(CreateBoardActivity.this, R.drawable.ic_check_ok, R.string.createBoardSuccesfully);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.putExtra(ApiConstant.BOARD, value);
                                setResult(RESULT_OK, intent);
                                CreateBoardActivity.this.finish();
                            }
                        }, 2500);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });


    }

    @Override
    protected void onDestroy() {
        View et = getCurrentFocus();
        if (et != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
        super.onDestroy();
    }
}
