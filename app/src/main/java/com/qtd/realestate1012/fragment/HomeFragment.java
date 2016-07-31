package com.qtd.realestate1012.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.utils.ServiceUtils;
import com.qtd.realestate1012.utils.SnackbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 7/30/2016.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "homeFragment";
    private View view;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tvNoInternet)
    TextView tvNoInternet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        if (!ServiceUtils.isNetworkAvailable(view.getContext())) {
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setEnabled(true);
            tvNoInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!ServiceUtils.isNetworkAvailable(view.getContext()) && this.isVisible()) {
                SnackbarUtils.showSnackbar(view);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!ServiceUtils.isNetworkAvailable(view.getContext()) && this.isVisible()) {
            SnackbarUtils.showSnackbar(view);
        }
    }
}
