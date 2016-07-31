package com.qtd.realestate1012.fragment;

import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.manager.MapManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 7/30/2016.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private View view;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.tvFilter)
    TextView tvFilter;

    @BindView(R.id.tvListResult)
    TextView tvListResult;

    @BindView(R.id.tvLocalInfo)
    TextView tvLocalInfo;

    @BindView(R.id.tvSaveSearch)
    TextView tvSaveSearch;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private SupportMapFragment supportMapFragment;
    private MapManager mapManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        progressBar.setEnabled(true);

        supportMapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.layoutSearch, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapManager = new MapManager(view.getContext(), googleMap);
        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mapManager.setEnabledMyLocation();
        }
    }
}
