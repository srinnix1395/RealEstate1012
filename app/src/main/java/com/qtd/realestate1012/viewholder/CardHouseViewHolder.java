package com.qtd.realestate1012.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.HouseDetailActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.custom.RippleView;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnCardHouseViewHolder;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 9/1/2016.
 */
public class CardHouseViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
    @BindView(R.id.imvImage)
    ImageView imvImage;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvAddress1)
    TextView tvAddress1;

    @BindView(R.id.imvHeart)
    ImageView imvHeart;

    @BindView(R.id.rippleView)
    RippleView rippleView;

    private String id;
    private boolean isClickHeart;

    public CardHouseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        initViews();
    }

    private void initViews() {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClickHeart = false;
            }
        });
        rippleView.setOnRippleCompleteListener(this);
    }

    public void setupDataViewHolder(CompactHouse house) {
        id = house.getId();
        Glide.with(itemView.getContext())
                .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE_HOUSE + house.getFirstImage())
                .placeholder(R.drawable.ic_apartment)
                .error(R.drawable.ic_apartment)
                .into(imvImage);

        tvPrice.setText(String.format(Locale.getDefault(), "%,d %s", house.getPrice(), itemView.getContext().getString(R.string.currency)));
        tvAddress.setText(String.format("%s, %s", house.getDetailAddress(), house.getStreet()));
        tvAddress1.setText(String.format("%s, %s, %s", house.getWard(), house.getDistrict(), house.getCity()));

        if (house.isLiked()) {
            imvHeart.setImageResource(R.drawable.ic_heart_pink_small);
        } else {
            imvHeart.setImageResource(R.drawable.ic_heart_outline);

        }
    }

    @OnClick(R.id.imvHeart)
    void onClick() {
        isClickHeart = true;
        rippleView.animateRipple(imvHeart.getLeft() + imvHeart.getWidth() / 2, imvHeart.getTop() + UiUtils.convertPixelsToDp(itemView.getContext(), 10) + imvHeart.getHeight() / 2);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (isClickHeart) {
            EventBus.getDefault().post(new MessageClickImvHeartOnCardHouseViewHolder(id));
            return;
        }

        Intent intent = new Intent(itemView.getContext(), HouseDetailActivity.class);
        intent.putExtra(ApiConstant._ID, id);
        itemView.getContext().startActivity(intent);
    }
}
