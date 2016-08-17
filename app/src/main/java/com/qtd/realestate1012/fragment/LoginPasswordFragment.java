package com.qtd.realestate1012.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
                sendRequestToServer(ApiConstant.URL_WEB_SERVICE_LOGIN);
                break;
            }
            case ApiConstant.TYPE_REGISTER: {
                sendRequestToServer(ApiConstant.URL_WEB_SERVICE_REGISTER);
                break;
            }
        }
    }

    private void sendRequestToServer(String url) {
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
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            Toast.makeText(view.getContext(), R.string.errorLogin, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            HousieApplication.getInstance().getSharedPreUtils().putBoolean(AppConstant.USER_LOGGED_IN, true);
                            HousieApplication.getInstance().getSharedPreUtils().putString(ApiConstant._ID, response.getString(ApiConstant._ID));
                            HousieApplication.getInstance().getSharedPreUtils().putString(ApiConstant.EMAIL, response.getString(ApiConstant.EMAIL));
                            getActivity().finish();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
}
