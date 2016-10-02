package com.qtd.realestate1012.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.qtd.realestate1012.activity.LoginActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;

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
 * Created by Dell on 7/29/2016.
 */
public class LoginPasswordFragment extends Fragment {

    private View view;
    private String email;
    private String type;

    @BindView(R.id.tvHello)
    TextView tvHello;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        setupData(bundle.getString(ApiConstant.EMAIL), bundle.getString(ApiConstant.TYPE));
        initViews();
    }

    private void initViews() {
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setupData(String email, String type) {
        this.email = email;
        this.type = type;

        switch (type) {
            case ApiConstant.TYPE_LOGIN: {
                tvHello.setText(R.string.enterYourPassword);
                btnSubmit.setText(R.string.login);
                break;
            }
            case ApiConstant.TYPE_REGISTER: {
                tvHello.setText(R.string.createYourPassword);
                btnSubmit.setText(R.string.register);
                break;
            }
        }
    }

    @OnClick({R.id.btnSubmit, R.id.tvClose})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit: {
                onClickSubmit();
                break;
            }
            case R.id.tvClose: {
                getActivity().finish();
            }
        }
    }

    private void onClickSubmit() {
        if (etPassword.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), R.string.pleaseInputPassword, Toast.LENGTH_SHORT).show();
            return;
        }

        if (etPassword.getText().toString().length() < 7) {
            Toast.makeText(getActivity(), R.string.passwordNotValid, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (type) {
            case ApiConstant.TYPE_LOGIN: {
                sendRequestToServer(ApiConstant.URL_WEB_SERVICE_LOGIN, ApiConstant.TYPE_LOGIN);
                break;
            }
            case ApiConstant.TYPE_REGISTER: {
                sendRequestToServer(ApiConstant.URL_WEB_SERVICE_REGISTER, ApiConstant.TYPE_REGISTER);
                break;
            }
        }
    }

    private void sendRequestToServer(String url, final String type) {
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiConstant.EMAIL, email);
            jsonObject.put(ApiConstant.PASSWORD, etPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            Toast.makeText(view.getContext(), R.string.errorLogin, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleLoginSuccess(response);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), R.string.errorLogin, Toast.LENGTH_SHORT).show();
                } finally {
                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(view.getContext(), R.string.errorLogin, Toast.LENGTH_SHORT).show();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleLoginSuccess(final JSONObject response) throws JSONException {
        HousieApplication.getInstance().getSharedPreUtils().putUserData(
                true,
                response.getString(ApiConstant._ID),
                response.getString(ApiConstant.EMAIL),
                response.getString(ApiConstant.PROVIDER));

        if (response.has(ApiConstant.BOARD)) {
            Single.fromCallable(new Callable<ArrayList<Board>>() {
                @Override
                public ArrayList<Board> call() throws Exception {
                    ArrayList<Board> arrayList = ProcessJson.getFavoriteBoards(response.getJSONObject(ApiConstant.BOARD));

                    DatabaseHelper database = DatabaseHelper.getInstance(getContext());
                    database.insertMultiplesBoard(arrayList);
                    return arrayList;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<ArrayList<Board>>() {
                        @Override
                        public void onSuccess(ArrayList<Board> value) {
                            finishActivity(value);
                        }

                        @Override
                        public void onError(Throwable error) {
                            error.printStackTrace();
                        }
                    });

        } else {
            finishActivity(null);
        }
    }

    private void finishActivity(final ArrayList<Board> arrayList) {
        if (type.equals(ApiConstant.TYPE_LOGIN)) {
            AlertUtils.showToastSuccess(view.getContext(), R.drawable.ic_account_checked, R.string.loginSuccess);
        } else {
            AlertUtils.showToastSuccess(view.getContext(), R.drawable.ic_account_checked, R.string.registerSuccess);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String idHouse = ((LoginActivity) getActivity()).getIdHouse();
                if (idHouse != null) {
                    Intent intent = new Intent();
                    intent.putExtra(ApiConstant._ID_HOUSE, idHouse);
                    intent.putParcelableArrayListExtra(ApiConstant.LIST_BOARD, arrayList);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                } else {
                    getActivity().setResult(Activity.RESULT_OK);
                }
                getActivity().finish();
            }
        }, 2500);
    }
}
