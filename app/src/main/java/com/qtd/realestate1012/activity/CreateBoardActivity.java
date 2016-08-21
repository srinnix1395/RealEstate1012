package com.qtd.realestate1012.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.qtd.realestate1012.utils.ServiceUtils;
import com.qtd.realestate1012.utils.AlertUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
            String boards = intent.getStringExtra(ApiConstant.LIST_BOARD);
            String[] list = boards.split("-");
            for (int i = 0, size = list.length - 1; i < size; i++) {
                listBoard.add(list[i]);
            }
        }
    }

    private void initViews() {
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.DST_ATOP);
        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @OnClick({R.id.imvBack})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.imvBack: {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
            case R.id.tvNext: {
                onClickNext();
                break;
            }
        }
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
                    if (response.getString(ApiConstant.RESULT).equals(ApiConstant.FAILED)) {
                        Toast.makeText(CreateBoardActivity.this, R.string.errorConnection, Toast.LENGTH_SHORT).show();
                        tvNext.setVisibility(View.VISIBLE);
                    } else {
                        AlertUtils.showToastSuccess(CreateBoardActivity.this, R.drawable.ic_check_ok, R.string.createBoard);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CreateBoardActivity.this.finish();
                            }
                        }, 2000);
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
                error.printStackTrace();
                Toast.makeText(CreateBoardActivity.this, R.string.errorConnection, Toast.LENGTH_SHORT).show();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }
}