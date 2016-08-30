package com.qtd.realestate1012.custom;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtd.realestate1012.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 8/27/2016.
 */
public class MarkerInfoHouse extends RelativeLayout {
    @BindView(R.id.tvPrice)
    TextView tvPrice;

    public MarkerInfoHouse(Context context, int price) {
        super(context);
        inflate(context, R.layout.view_marker_info_house, this);
        ButterKnife.bind(this);
        tvPrice.setText(String.format(Locale.getDefault(), "%,d %s", price, getContext().getString(R.string.currency)));
    }

}
