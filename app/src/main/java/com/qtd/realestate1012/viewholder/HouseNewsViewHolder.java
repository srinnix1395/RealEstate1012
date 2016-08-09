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
        String url = "http://protectedcedar-31067.rhcloud.com/image/";
        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(0).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage1);
        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(1).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage2);
        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(2).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage3);
        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(3).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage4);
        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(4).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage5);

        tvAddress1.setText(bunchHouse.getCompactHouse(0).getAddress());
        tvAddress2.setText(bunchHouse.getCompactHouse(1).getAddress());
        tvAddress3.setText(bunchHouse.getCompactHouse(2).getAddress());
        tvAddress4.setText(bunchHouse.getCompactHouse(3).getAddress());
        tvAddress5.setText(bunchHouse.getCompactHouse(4).getAddress());

        tvPrice1.setText(bunchHouse.getCompactHouse(0).getPrice() + view.getContext().getString(R.string.currency));
        tvPrice2.setText(bunchHouse.getCompactHouse(1).getPrice() + view.getContext().getString(R.string.currency));
        tvPrice3.setText(bunchHouse.getCompactHouse(2).getPrice() + view.getContext().getString(R.string.currency));
        tvPrice4.setText(bunchHouse.getCompactHouse(3).getPrice() + view.getContext().getString(R.string.currency));
        tvPrice5.setText(bunchHouse.getCompactHouse(4).getPrice() + view.getContext().getString(R.string.currency));
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
