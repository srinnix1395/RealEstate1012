package com.qtd.realestate1012.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.GoogleMap;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;

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

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private int mapType;
    private boolean satelliteChecked;
    private String placeType;

    public LocalInfoDialog(Context context, int mapType, String placeType) {
        super(context);
        this.context = context;
        this.mapType = mapType;
        this.placeType = placeType;
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
        switch (placeType) {
            case ApiConstant.API_PLACE_TYPE_SCHOOL: {
                radioGroup.check(R.id.radioSchools);
                break;
            }
            case ApiConstant.API_PLACE_TYPE_HOSPITAL: {
                radioGroup.check(R.id.radioHospital);
                break;
            }
        }
    }

    @OnClick({R.id.radioAmenities, R.id.radioSchools, R.id.radioSatellite, R.id.radioHospital})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.radioSatellite: {
                changeMapType();
                dismiss();
                break;
            }
            default: {
                dismiss();
                break;
            }
        }
    }

    private void changeMapType() {
        if (mapType == GoogleMap.MAP_TYPE_SATELLITE) {
            mapType = GoogleMap.MAP_TYPE_NORMAL;
            return;
        }
        mapType = GoogleMap.MAP_TYPE_SATELLITE;
    }

    public int getMapType() {
        return mapType;
    }

    public int getRadioButtonIdChecked() {
        return radioGroup.getCheckedRadioButtonId();
    }
}
