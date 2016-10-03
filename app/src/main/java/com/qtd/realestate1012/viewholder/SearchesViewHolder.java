package com.qtd.realestate1012.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.ResultActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.custom.RippleView;
import com.qtd.realestate1012.messageevent.MessageRemoveSavedSearch;
import com.qtd.realestate1012.model.ItemSavedSearch;
import com.qtd.realestate1012.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 8/18/2016.
 */
public class SearchesViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener, View.OnClickListener {
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.imvHeart)
    ImageView imvHeart;

    @BindView(R.id.rippleView)
    RippleView rippleView;

    private ItemSavedSearch mItem;
    private int mPosition;
    private boolean isClickHeart;

    public SearchesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        rippleView.setOnRippleCompleteListener(this);
    }

    public void setupDataViewHolder(ItemSavedSearch itemSearch, int position) {
        mPosition = position;
        mItem = itemSearch;

        tvStatus.setText(itemSearch.getStatus() + (itemSearch.getPropertyType().equals(ApiConstant.ANY) ? "" : " - " + itemSearch.getPropertyType()));
        tvDescription.setText(String.format(Locale.getDefault(), "%,d %s - %,d %s, %,d m2 - %,d m2", itemSearch.getPriceFrom(),
                itemView.getContext().getString(R.string.currency), itemSearch.getPriceTo(), itemView.getContext().getString(R.string.currency),
                itemSearch.getAreaFrom(), itemSearch.getAreaTo()));
    }

    @OnClick(R.id.imvHeart)
    void onClickHeart(View v) {
        isClickHeart = true;
        rippleView.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(itemView.getContext(), 10) + v.getHeight() / 2);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (isClickHeart) {
            EventBus.getDefault().post(new MessageRemoveSavedSearch(mPosition));
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiConstant.STATUS, mItem.getStatus());
            jsonObject.put(ApiConstant.PRICE_FROM, mItem.getPriceFrom());
            jsonObject.put(ApiConstant.PRICE_TO, mItem.getPriceTo());
            jsonObject.put(ApiConstant.NUMBER_OF_ROOMS, mItem.getNumberOfRooms());
            jsonObject.put(ApiConstant.AREA_FROM, mItem.getAreaFrom());
            jsonObject.put(ApiConstant.AREA_TO, mItem.getAreaTo());
            jsonObject.put(ApiConstant.PROPERTY_TYPE, mItem.getPropertyType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(itemView.getContext(), ResultActivity.class);
        intent.putExtra(ApiConstant.CRITERIA, jsonObject.toString());
        itemView.getContext().startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        isClickHeart = false;
    }
}
