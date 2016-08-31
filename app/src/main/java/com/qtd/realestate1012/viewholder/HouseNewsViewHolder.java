package com.qtd.realestate1012.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.HouseDetailActivity;
import com.qtd.realestate1012.callback.ViewHolderCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnHouse;
import com.qtd.realestate1012.model.BunchHouse;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

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

    @BindView(R.id.tvAddress12)
    TextView tvAddress12;

    @BindView(R.id.tvAddress22)
    TextView tvAddress22;

    @BindView(R.id.tvAddress32)
    TextView tvAddress32;

    @BindView(R.id.tvAddress42)
    TextView tvAddress42;

    @BindView(R.id.tvAddress52)
    TextView tvAddress52;

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
    private BunchHouse bunchHouse;
    private ViewHolderCallback callback;

    public HouseNewsViewHolder(View view) {
        this.view = view;
        ButterKnife.bind(this, view);
        callback = (ViewHolderCallback) view.getContext();
    }

    public void setupViewHolder(BunchHouse bunchHouse) {
        this.bunchHouse = bunchHouse;
        String url = ApiConstant.URL_WEB_SERVICE_GET_IMAGE;

        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(0).getFirstImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage1);
        tvAddress1.setText(bunchHouse.getCompactHouse(0).getSmallAddress());
        tvAddress12.setText(bunchHouse.getCompactHouse(0).getLargeAddress());
        tvPrice1.setText(String.format(Locale.getDefault(), "%,d %s", bunchHouse.getCompactHouse(0).getPrice(), view.getContext().getString(R.string.currency)));


        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(1).getFirstImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage2);
        tvAddress2.setText(bunchHouse.getCompactHouse(1).getSmallAddress());
        tvAddress22.setText(bunchHouse.getCompactHouse(1).getLargeAddress());
        tvPrice2.setText(String.format(Locale.getDefault(), "%,d %s", bunchHouse.getCompactHouse(1).getPrice(), view.getContext().getString(R.string.currency)));


        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(2).getFirstImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage3);
        tvAddress3.setText(bunchHouse.getCompactHouse(2).getSmallAddress());
        tvAddress32.setText(bunchHouse.getCompactHouse(2).getLargeAddress());
        tvPrice3.setText(String.format(Locale.getDefault(), "%,d %s", bunchHouse.getCompactHouse(2).getPrice(), view.getContext().getString(R.string.currency)));


        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(3).getFirstImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage4);
        tvAddress4.setText(bunchHouse.getCompactHouse(3).getSmallAddress());
        tvAddress42.setText(bunchHouse.getCompactHouse(3).getLargeAddress());
        tvPrice4.setText(String.format(Locale.getDefault(), "%,d %s", bunchHouse.getCompactHouse(3).getPrice(), view.getContext().getString(R.string.currency)));


        Glide.with(view.getContext())
                .load(url + bunchHouse.getCompactHouse(4).getFirstImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_apartment)
                .crossFade()
                .into(imvImage5);
        tvAddress5.setText(bunchHouse.getCompactHouse(4).getSmallAddress());
        tvAddress52.setText(bunchHouse.getCompactHouse(4).getLargeAddress());
        tvPrice5.setText(String.format(Locale.getDefault(), "%,d %s", bunchHouse.getCompactHouse(4).getPrice(), view.getContext().getString(R.string.currency)));


        tvSeeAll.setText(String.format("%s %s", view.getContext().getString(R.string.seeAll), bunchHouse.getType()));
    }

    @OnClick({R.id.imvHeart1, R.id.imvHeart2, R.id.imvHeart3, R.id.imvHeart4, R.id.imvHeart5, R.id.tvSeeAll
            , R.id.layoutHouse1, R.id.layoutHouse2, R.id.layoutHouse3, R.id.layoutHouse4, R.id.layoutHouse5})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSeeAll: {

                break;
            }
            case R.id.layoutHouse1: {
                startDetailHouseActivity(0);
                break;
            }
            case R.id.layoutHouse2: {
                startDetailHouseActivity(1);
                break;
            }
            case R.id.layoutHouse3: {
                startDetailHouseActivity(2);
                break;
            }
            case R.id.layoutHouse4: {
                startDetailHouseActivity(3);
                break;
            }
            case R.id.layoutHouse5: {
                startDetailHouseActivity(4);
                break;
            }
            case R.id.imvHeart1: {
                addHouseToFavorite(0);
                break;
            }
            case R.id.imvHeart2: {
                addHouseToFavorite(0);
                break;
            }
            case R.id.imvHeart3: {
                addHouseToFavorite(0);
                break;
            }
            case R.id.imvHeart4: {
                addHouseToFavorite(0);
                break;
            }
            case R.id.imvHeart5: {
                addHouseToFavorite(0);
                break;
            }
            default: {

                break;
            }
        }
    }

    private void addHouseToFavorite(int i) {
        String id = bunchHouse.getCompactHouse(i).getId();
        EventBus.getDefault().post(new MessageClickImvHeartOnHouse(id));
    }

    private void startDetailHouseActivity(int i) {
        Intent intent = new Intent(view.getContext(), HouseDetailActivity.class);
        intent.putExtra(ApiConstant._ID, bunchHouse.getCompactHouse(i).getId());
        view.getContext().startActivity(intent);
    }
}
