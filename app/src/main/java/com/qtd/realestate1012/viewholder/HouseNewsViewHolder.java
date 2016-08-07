package com.qtd.realestate1012.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
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

    private View view;

    public HouseNewsViewHolder(View view) {
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public void setupViewHolder(BunchHouse bunchHouse) {
        Glide.with(view.getContext())
                .load(bunchHouse.getCompactHouse1().getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .fitCenter()
                .crossFade()
                .into(imvImage1);
        Glide.with(view.getContext())
                .load(bunchHouse.getCompactHouse2().getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .fitCenter()
                .crossFade()
                .into(imvImage2);
        Glide.with(view.getContext())
                .load(bunchHouse.getCompactHouse3().getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .fitCenter()
                .crossFade()
                .into(imvImage3);
        Glide.with(view.getContext())
                .load(bunchHouse.getCompactHouse4().getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .fitCenter()
                .crossFade()
                .into(imvImage4);
        Glide.with(view.getContext())
                .load(bunchHouse.getCompactHouse5().getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .fitCenter()
                .crossFade()
                .into(imvImage5);

        tvAddress1.setText(bunchHouse.getCompactHouse1().getAddress());
        tvAddress2.setText(bunchHouse.getCompactHouse2().getAddress());
        tvAddress3.setText(bunchHouse.getCompactHouse3().getAddress());
        tvAddress4.setText(bunchHouse.getCompactHouse4().getAddress());
        tvAddress5.setText(bunchHouse.getCompactHouse5().getAddress());

        tvPrice1.setText(bunchHouse.getCompactHouse1().getPrice() + view.getContext().getString(R.string.currency));
        tvPrice2.setText(bunchHouse.getCompactHouse2().getPrice() + view.getContext().getString(R.string.currency));
        tvPrice3.setText(bunchHouse.getCompactHouse3().getPrice() + view.getContext().getString(R.string.currency));
        tvPrice4.setText(bunchHouse.getCompactHouse4().getPrice() + view.getContext().getString(R.string.currency));
        tvPrice5.setText(bunchHouse.getCompactHouse5().getPrice() + view.getContext().getString(R.string.currency));
    }

    @OnClick({R.id.imvHeart1, R.id.imvHeart2, R.id.imvHeart3, R.id.imvHeart4, R.id.imvHeart5, R.id.btnSeeAll})
    void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSeeAll:{

                break;
            }
            default:{

                break;
            }
        }
    }
}
