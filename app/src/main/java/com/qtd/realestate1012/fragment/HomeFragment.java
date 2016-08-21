package com.qtd.realestate1012.fragment;

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
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.HouseNewsAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.custom.PinnedSectionListView;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.tvNoInternet)
    TextView tvError;

    private ArrayList<Object> arrayList;
    private HouseNewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        initView();
    }

    private void initData() {
        arrayList = new ArrayList<>();
        adapter = new HouseNewsAdapter(view.getContext(), 0, arrayList);
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            AlertUtils.showSnackBarNoInternet(view);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(ApiConstant.URL_WEB_SERVICE_GET_NEWS, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.getString(ApiConstant.RESULT).equals(ApiConstant.SUCCESS)) {
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText(R.string.errorConnection);

                        progressBar.setEnabled(false);
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }

                    arrayList.clear();
                    arrayList.addAll(ProcessJson.getArrayListHousesNew(response.getJSONArray(ApiConstant.LIST_HOUSE)));
                    adapter.notifyDataSetChanged();

                    tvError.setVisibility(View.INVISIBLE);

                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvError.setText(R.string.errorConnection);
                tvError.setVisibility(View.VISIBLE);
                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void initView() {
        listView.setAdapter(adapter);

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!ServiceUtils.isNetworkAvailable(view.getContext()) && this.isVisible()) {
                AlertUtils.showSnackBarNoInternet(view);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.isVisible()) {
            Log.e(TAG, "onResume: update");
            requestData();
        }
    }
}
