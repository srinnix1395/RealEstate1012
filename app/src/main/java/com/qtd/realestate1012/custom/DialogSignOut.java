package com.qtd.realestate1012.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.messageevent.MessageSignOutResult;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by DELL on 9/17/2016.
 */
public class DialogSignOut extends AlertDialog implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private String mProvider;
    private String mResult;

    public DialogSignOut(@NonNull Context context, String mProvider) {
        super(context);
        mContext = context;
        this.mProvider = mProvider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mProvider.equals(ApiConstant.GOOGLE)) {
            initGoogleApi();
        } else if (mProvider.equals(ApiConstant.FACEBOOK)) {
            initFacebookSDK();
        }
    }

    private void initFacebookSDK() {
        FacebookSdk.sdkInitialize(getContext());
    }

    private void initGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(((FragmentActivity) mContext), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void logout() {
        switch (mProvider) {
            case ApiConstant.GOOGLE: {
                logoutGoogle();
                break;
            }
            case ApiConstant.FACEBOOK: {
                logoutFacebook();
                break;
            }
            case ApiConstant.HOUSIE: {
                logoutHousie();
                break;
            }
            default:
                break;
        }
    }

    private void logoutHousie() {
        EventBus.getDefault().post(new MessageSignOutResult(mResult));
    }

    private void logoutGoogle() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...

                            if (status.isSuccess()) {
                                mResult = ApiConstant.SUCCESS;
                            } else {
                                mResult = ApiConstant.FAILED;
                            }

                            EventBus.getDefault().post(new MessageSignOutResult(mResult));
                        }
                    });
        } else {
            Log.e(DialogSignOut.class.getName(), "logoutGoogle: error connection");
        }
    }

    private void logoutFacebook() {
        LoginManager.getInstance().logOut();
        mResult = ApiConstant.SUCCESS;
        EventBus.getDefault().post(new MessageSignOutResult(mResult));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(DialogSignOut.class.getName(), "logoutGoogle: error connection");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    public String getmResult() {
        return mResult;
    }
}
