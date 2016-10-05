package com.qtd.realestate1012.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.HouseDetailActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.RippleView;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnHouse;
import com.qtd.realestate1012.messageevent.MessageRemoveHouseToBoard;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 8/17/2016.
 */
public class HouseHasHeartViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
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

    @BindView(R.id.imvHeart)
    ImageView imvHeart;

    private boolean flagClickHeart;
    private String idHouse;

    public HouseHasHeartViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        initViews();
    }

    private void initViews() {
        rippleView.setOnRippleCompleteListener(this);
    }

    public void setupDataViewHolder(CompactHouse house) {
        idHouse = house.getId();

        Glide.with(itemView.getContext())
                .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE_HOUSE + house.getFirstImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade(1000)
                .placeholder(R.drawable.ic_apartment)
                .into(imvImage);

        tvAddress.setText(house.getSmallAddress());
        tvAddress1.setText(house.getLargeAddress());
        tvPrice.setText(String.format(Locale.getDefault(), "%,d %s", house.getPrice(), itemView.getContext().getString(R.string.currency)));

        if (house.isLiked()) {
            imvHeart.setImageResource(R.drawable.ic_heart_pink_36dp);
        } else {
            imvHeart.setImageResource(R.drawable.ic_heart_outline);
        }
    }

    @OnClick(R.id.imvHeart)
    void onClickHeart(View v) {
        flagClickHeart = true;
        rippleView.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(itemView.getContext(), 10) + v.getHeight() / 2);
    }

    @OnClick(R.id.imvImage)
    void onClickHouse() {
        flagClickHeart = false;
    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (!flagClickHeart) {
            Intent intent = new Intent(itemView.getContext(), HouseDetailActivity.class);
            intent.putExtra(ApiConstant._ID, idHouse);
            itemView.getContext().startActivity(intent);
            return;
        }

        if (itemView.getContext().getClass().getName().equals(AppConstant.BOARD_DETAIL_ACTIVITY)) {
            EventBus.getDefault().post(new MessageRemoveHouseToBoard(idHouse));
            return;
        }

        if (itemView.getContext().getClass().getName().equals(AppConstant.RESULT_ACTIVITY)) {
            EventBus.getDefault().post(new MessageClickImvHeartOnHouse(idHouse));
        }
    }
}
