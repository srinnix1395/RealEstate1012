package com.qtd.realestate1012.custom;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 8/27/2016.
 */
public class MarkerInfoHouse extends RelativeLayout {
    @BindView(R.id.tvPrice)
    TextView tvPrice;

    public MarkerInfoHouse(Context context, String status,int price) {
        super(context);
        if (status.equals(ApiConstant.SALE)) {
            inflate(context, R.layout.view_marker_info_house_sale, this);
        } else {
            inflate(context, R.layout.view_marker_info_house_rent, this);
        }
        ButterKnife.bind(this);
        tvPrice.setText(String.format(Locale.getDefault(), "%,d %s", price, getContext().getString(R.string.currency)));
    }

}
