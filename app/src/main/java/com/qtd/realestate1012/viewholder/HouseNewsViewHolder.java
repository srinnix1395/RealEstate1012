package com.qtd.realestate1012.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.BunchHouse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 8/7/2016.
 */
public class HouseNewsViewHolder {
    @BindView(R.id.imvImage1)
    ImageView imvImage1;

    @BindView(R.id.imvImage2)
    ImageView imvImage2;

    @BindView(R.id.imvImage3)
    ImageView imvImage3;

    @BindView(R.id.imvImage4)
    ImageView imvImage4;

    @BindView(R.id.imvImage5)
    ImageView imvImage5;

    @BindView(R.id.tvAddress1)
    TextView tvAddress1;

    @BindView(R.id.tvAddress2)
    TextView tvAddress2;

    @BindView(R.id.tvAddress3)
    TextView tvAddress3;

    @BindView(R.id.tvAddress4)
    TextView tvAddress4;

    @BindView(R.id.tvAddress5)
    TextView tvAddress5;

    @BindView(R.id.tvPrice1)
    TextView tvPrice1;

    @BindView(R.id.tvPrice2)
    TextView tvPrice2;

    @BindView(R.id.tvPrice3)
    TextView tvPrice3;

    @BindView(R.id.tvPrice4)
    TextView tvPrice4;

    @BindView(R.id.tvPrice5)
    TextView tvPrice5;

    @BindView(R.id.tvSeeAll)
    TextView tvSeeAll;

    private View view;

    public HouseNewsViewHolder(View view) {
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public void setupViewHolder(BunchHouse bunchHouse) {
        String url = ApiConstant.URL_WEB_SERVICE_GET_IMAGE;

        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(0).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage1);
        tvAddress1.setText(bunchHouse.getCompactHouse(0).getAddress());
        tvPrice1.setText(bunchHouse.getCompactHouse(0).getPrice() + view.getContext().getString(R.string.currency));


        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(1).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage2);
        tvAddress2.setText(bunchHouse.getCompactHouse(1).getAddress());
        tvPrice2.setText(bunchHouse.getCompactHouse(1).getPrice() + view.getContext().getString(R.string.currency));


        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(2).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage3);
        tvAddress3.setText(bunchHouse.getCompactHouse(2).getAddress());
        tvPrice3.setText(bunchHouse.getCompactHouse(2).getPrice() + view.getContext().getString(R.string.currency));


        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(3).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage4);
        tvAddress4.setText(bunchHouse.getCompactHouse(3).getAddress());
        tvPrice4.setText(bunchHouse.getCompactHouse(3).getPrice() + view.getContext().getString(R.string.currency));


        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(4).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage5);
        tvAddress5.setText(bunchHouse.getCompactHouse(4).getAddress());
        tvPrice5.setText(bunchHouse.getCompactHouse(4).getPrice() + view.getContext().getString(R.string.currency));


        tvSeeAll.setText(view.getContext().getString(R.string.seeAll) + bunchHouse.getType());
    }

    @OnClick({R.id.imvHeart1, R.id.imvHeart2, R.id.imvHeart3, R.id.imvHeart4, R.id.imvHeart5, R.id.tvSeeAll
            , R.id.layoutHouse1, R.id.layoutHouse2, R.id.layoutHouse3, R.id.layoutHouse4, R.id.layoutHouse5})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSeeAll: {

                break;
            }
            case R.id.layoutHouse1:{

                break;
            }
            default: {

                break;
            }
        }
    }
}
