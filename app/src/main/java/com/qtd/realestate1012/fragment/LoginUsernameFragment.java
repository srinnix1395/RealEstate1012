package com.qtd.realestate1012.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
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
import com.qtd.realestate1012.activity.LoginActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.FavoriteHouse;
import com.qtd.realestate1012.model.ItemSavedSearch;
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initView();
    }


    private void initView() {
        etUsername.setText(HousieApplication.getInstance().getSharedPreUtils().getString(AppConstant.LAST_EMAIL_AT_LOGIN_ACTIVITY, ""));

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
                        sendLoginInfoToServer(ProcessJson.processDataToServer(object));
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, picture, email");
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

    @OnClick({R.id.btnSubmit, R.id.imbFacebook, R.id.imbGoogle, R.id.tvTerm, R.id.tvClose})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit: {
                onClickSubmit();
                break;
            }
            case R.id.imbFacebook: {
                if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
                    Toast.makeText(view.getContext(), R.string.pleaseCheckYourConnection, Toast.LENGTH_SHORT).show();
                    break;
                }
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
            case R.id.tvClose: {
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
        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            AlertUtils.showSnackBarNoInternet(view);
            return;
        }

        if (etUsername.getText().toString().isEmpty()) {
            etUsername.setError(view.getContext().getString(R.string.emailCannotBeNull));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etUsername.getText().toString()).matches()) {
            etUsername.setError(view.getContext().getString(R.string.emailIsNotValid));
            return;
        }

        View et = getActivity().getCurrentFocus();
        if (et != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant.EMAIL, etUsername.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_IS_EMAIL_EXISTED, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    handleResponseLoginHousie(response);
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
                showErrorDialog();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseLoginHousie(JSONObject response) throws JSONException {
        String result = "";
        try {
            result = response.getString(ApiConstant.RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (result) {
            case ApiConstant.FAILED: {
                Toast.makeText(getActivity(), R.string.login, Toast.LENGTH_SHORT).show();
                break;
            }
            case ApiConstant.FALSE: {
                ((LoginActivity) getActivity()).showFragmentPassword(etUsername.getText().toString(), ApiConstant.TYPE_REGISTER);
                return;
            }
            case ApiConstant.TRUE: {
                if (!response.has(ApiConstant.TYPE)) {
                    ((LoginActivity) getActivity()).showFragmentPassword(etUsername.getText().toString(), ApiConstant.TYPE_LOGIN);
                    return;
                }

                if (response.getString(ApiConstant.PROVIDER).equals(ApiConstant.FACEBOOK)) {
                    btnFacebook.performClick();
                    return;
                }

                if (response.getString(ApiConstant.PROVIDER).equals(ApiConstant.GOOGLE)) {
                    signInGoogle();
                }
                break;
            }
        }


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

    @Override
    public void onStart() {
        super.onStart();
        //add this to connect Google Client
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Stop the Google Client when activity is stopped
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("login user fragment", "onConnectionFailed: " + connectionResult.getErrorMessage());
        Toast.makeText(getContext(), R.string.errorLogin, Toast.LENGTH_SHORT).show();
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

            JSONObject object = new JSONObject();

            try {
                object.put(ApiConstant._ID_SOCIAL, account.getId());
                object.put(ApiConstant.EMAIL, account.getEmail());
                object.put(ApiConstant.NAME, account.getDisplayName());
                object.put(ApiConstant.AVATAR, account.getPhotoUrl().toString());
                object.put(ApiConstant.PROVIDER, ApiConstant.GOOGLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendLoginInfoToServer(object);
        } else {
            showErrorDialog();
        }
    }

    private void sendLoginInfoToServer(JSONObject jsonObject) {
        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        String url = ApiConstant.URL_WEB_SERVICE_LOGIN_SOCIAL;
        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            handleResponseLoginSocialFailed(response);
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleResponseLoginSocialSuccess(response);
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
                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(view.getContext(), R.string.errorLogin, Toast.LENGTH_SHORT).show();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseLoginSocialSuccess(final JSONObject response) throws JSONException {
        HousieApplication.getInstance().getSharedPreUtils().putUserData(
                true,
                response.getString(ApiConstant._ID),
                response.getString(ApiConstant.EMAIL),
                response.getString(ApiConstant.PROVIDER));

        if (response.has(ApiConstant.BOARD)) {
            Single.fromCallable(new Callable<ArrayList<Board>>() {
                @Override
                public ArrayList<Board> call() throws Exception {
                    ArrayList<Board> boardArrayList = ProcessJson.getListBoard(response);
                    ArrayList<FavoriteHouse> houseArrayList = ProcessJson.getListFavoriteHouse(response);
                    ArrayList<ItemSavedSearch> searchArrayList = ProcessJson.getListItemSearch(response);

                    DatabaseHelper database = DatabaseHelper.getInstance(getContext());
                    database.insertData(boardArrayList,houseArrayList,searchArrayList);
                    return boardArrayList;
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
        AlertUtils.showToastSuccess(view.getContext(), R.drawable.ic_account_checked, R.string.loginSuccess);
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

    private void handleResponseLoginSocialFailed(JSONObject response) {
        if (!response.has(ApiConstant.TYPE)) {
            Toast.makeText(view.getContext(), R.string.errorLogin, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.emailHasBeenRegisteredHousieAccount, Toast.LENGTH_SHORT).show();
            //// TODO: 9/12/2016 Sign out account social
        }
    }

    public String getEmail() {
        return etUsername.getText().toString();
    }
}
