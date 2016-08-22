package com.qtd.realestate1012.viewholder;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.ImageUtils;

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
        setupSize();
    }

    private void setupSize() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((AppCompatActivity) itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((height / 3) * 0.9));
        layoutParams.topMargin = (int) ImageUtils.convertDpToPixel(itemView.getContext(), 4f);
        itemView.setLayoutParams(layoutParams);
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
        tvPrice.setText(String.format("%,d", house.getPrice()) + " " + itemView.getContext().getString(R.string.currency));
        Log.e("house", "setupDataViewHolder: " + house.getPrice());
    }
}
