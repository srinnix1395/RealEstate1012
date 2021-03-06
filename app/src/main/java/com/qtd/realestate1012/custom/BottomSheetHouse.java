package com.qtd.realestate1012.custom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.HouseDetailActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.utils.ProcessJson;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 9/1/2016.
 */
public class BottomSheetHouse extends BottomSheetDialogFragment implements RippleView.OnRippleCompleteListener {
    private CompactHouse house;

    @BindView(R.id.imvImage)
    ImageView imvImage;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvAddress1)
    TextView tvAddress1;

    @BindView(R.id.rippleView)
    RippleView rippleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_dialog_house, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initViews();
    }

    private void initViews() {
        rippleView.setOnRippleCompleteListener(this);
    }

    private void initData() {
        Bundle bundle = getArguments();
        String json = bundle.getString(ApiConstant.HOUSE);
        house = ProcessJson.getInfoHouse(json);

        if (house != null) {
            Glide.with(getContext())
                    .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE_HOUSE + house.getFirstImage())
                    .placeholder(R.drawable.ic_apartment)
                    .error(R.drawable.ic_apartment)
                    .into(imvImage);

            tvPrice.setText(String.format(Locale.getDefault(), "%,d %s", house.getPrice(), getContext().getString(R.string.currency)));
            tvAddress.setText(String.format("%s, %s", house.getDetailAddress(), house.getStreet()));
            tvAddress1.setText(String.format("%s, %s, %s", house.getWard(), house.getDistrict(), house.getCity()));
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
        intent.putExtra(ApiConstant._ID, house.getId());
        startActivity(intent);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                BottomSheetHouse.this.dismiss();
            }
        });
    }
}
