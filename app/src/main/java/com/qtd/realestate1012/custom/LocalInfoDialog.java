package com.qtd.realestate1012.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.google.android.gms.maps.GoogleMap;
import com.qtd.realestate1012.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/31/2016.
 */
public class LocalInfoDialog extends Dialog {
    private Context context;

    @BindView(R.id.radioSatellite)
    RadioButton radioSatellite;

    private int mapType;
    private boolean satelliteChecked;

    public LocalInfoDialog(Context context, int mapType) {
        super(context);
        this.context = context;
        this.mapType = mapType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_local_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (mapType == GoogleMap.MAP_TYPE_SATELLITE) {
            radioSatellite.setChecked(true);
            satelliteChecked = true;
        }
    }

    @OnClick({R.id.radioAmenities, R.id.radioSchools, R.id.radioSatellite})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.radioSatellite: {
                changeSatelliteMode();
                dismiss();
                break;
            }
            case R.id.radioAmenities: {

                break;
            }
            case R.id.radioSchools: {

                break;
            }
        }
    }

    private void changeSatelliteMode() {
        if (satelliteChecked) {
            satelliteChecked = false;
        } else {
            satelliteChecked = true;
        }
        radioSatellite.setChecked(satelliteChecked);
    }

    public boolean getSatelliteModesStatus(){
        return radioSatellite.isChecked();
    }
}
