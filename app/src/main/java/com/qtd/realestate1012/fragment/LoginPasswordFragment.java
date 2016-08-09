package com.qtd.realestate1012.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;

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
        switch (type) {
            case ApiConstant.TYPE_LOGIN:{
                requestLogin();
                break;
            }
        }
    }

    private void requestLogin() {

    }
}
