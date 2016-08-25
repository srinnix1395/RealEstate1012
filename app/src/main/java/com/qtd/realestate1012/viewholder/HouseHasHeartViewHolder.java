package com.qtd.realestate1012.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.CompactHouse;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 8/17/2016.
 */
public class HouseHasHeartViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imvImage)
    ImageView imvImage;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.imvHeart)
    ImageView imvHeart;

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

        tvAddress.setText(house.getAddress());
        tvPrice.setTextColor(house.getPrice() + R.string.currency);
    }
}
