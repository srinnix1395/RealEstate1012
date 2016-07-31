package com.qtd.realestate1012.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
 * Created by Dell on 7/28/2016.
 */
public class LoginUsernameFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.btnLoginFacebook)
    LoginButton btnFacebook;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private View view;
    private CallbackManager callback;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_username, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initView();
    }


    private void initView() {
        btnFacebook.setFragment(this);
        callback = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions("public_profile");
        btnFacebook.setReadPermissions("email");
        btnFacebook.registerCallback(callback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);


                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                showErrorDialog();
            }
        });

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {
            bundle.putInt(ApiConstant._ID_SOCIAL, object.getInt("id"));
            bundle.putString(ApiConstant.NAME, object.getString("first_name") + " " + object.getString("last_name"));
            bundle.putString(ApiConstant.EMAIL, object.getString("email"));
            bundle.putString(ApiConstant.GENDER, object.getString("gender"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    @OnClick({R.id.btnSubmit, R.id.imbFacebook, R.id.imbGoogle, R.id.tvTerm, R.id.tvClose})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit: {
                onClickSubmit();
                break;
            }
            case R.id.imbFacebook: {
                btnFacebook.performClick();
                break;
            }
            case R.id.imbGoogle: {
                signInGoogle();
                break;
            }
            case R.id.tvTerm: {

                break;
            }
            case R.id.tvClose:{
                getActivity().finish();
                break;
            }
        }
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, AppConstant.REQUEST_CODE_SIGN_IN_GOOGLE);
    }

    private void onClickSubmit() {
        if (etUsername.getText().toString().isEmpty()) {
            etUsername.setError(view.getContext().getString(R.string.emailCannotBeNull));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etUsername.getText().toString()).matches()) {
            etUsername.setError(view.getContext().getString(R.string.emailIsNotValid));
            return;
        }

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
        String url = "";
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
                error.printStackTrace();
                showErrorDialog();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(view.getContext())
                .setTitle(R.string.login)
                .setMessage(R.string.errorLogin)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    public CallbackManager getCallbackManger() {
        return callback;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //google sign in
        if (requestCode == AppConstant.REQUEST_CODE_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
            return;
        }

        //facebook sign in
        callback.onActivityResult(requestCode, resultCode, data);
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            // TODO: 7/29/2016 get information google account

        } else {

        }
    }
}
