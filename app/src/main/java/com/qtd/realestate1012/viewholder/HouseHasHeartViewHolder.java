package com.qtd.realestate1012.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.custom.RippleView;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 8/17/2016.
 */
public class HouseHasHeartViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imvImage)
    ImageView imvImage;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvAddress1)
    TextView tvAddress1;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.rippleView)
    RippleView rippleView;

    public HouseHasHeartViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setupDataViewHolder(CompactHouse house) {
        Glide.with(itemView.getContext())
                .load(house.getFirstImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade(1000)
                .placeholder(R.drawable.ic_apartment)
                .into(imvImage);

        tvAddress.setText(house.getSmallAddress());
        tvAddress1.setText(house.getLargeAddress());
        tvPrice.setTextColor(house.getPrice() + R.string.currency);
    }

    @OnClick(R.id.imvHeart)
    void onClick(View v) {
        rippleView.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(itemView.getContext(), 10) + v.getHeight() / 2);
    }
}
