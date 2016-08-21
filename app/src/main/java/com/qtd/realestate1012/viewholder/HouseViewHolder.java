package com.qtd.realestate1012.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.CompactHouse;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 8/18/2016.
 */
public class HouseViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imvImage)
    ImageView imvImage;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    public HouseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setupDataViewHolder(CompactHouse house) {
        Glide.with(itemView.getContext())
                .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE + house.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.2f)
                .placeholder(R.drawable.ic_apartment)
                .error(R.drawable.ic_apartment)
                .into(imvImage);

        tvAddress.setText(house.getAddress());
        tvPrice.setTextColor(house.getPrice() + R.string.currency);
    }
}
