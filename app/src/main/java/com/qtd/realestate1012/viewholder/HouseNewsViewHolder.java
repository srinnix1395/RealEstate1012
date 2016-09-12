package com.qtd.realestate1012.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.HouseDetailActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.custom.RippleView;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnHouse;
import com.qtd.realestate1012.messageevent.MessageEventClickSeeAllViewHolder;
import com.qtd.realestate1012.model.BunchHouse;
import com.qtd.realestate1012.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 8/7/2016.
 */
public class HouseNewsViewHolder implements RippleView.OnRippleCompleteListener {
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

    @BindView(R.id.rippleView1)
    RippleView rippleView1;

    @BindView(R.id.rippleView2)
    RippleView rippleView2;

    @BindView(R.id.rippleView3)
    RippleView rippleView3;

    @BindView(R.id.rippleView4)
    RippleView rippleView4;

    @BindView(R.id.rippleView5)
    RippleView rippleView5;

    private View view;
    private BunchHouse bunchHouse;
    private boolean flagClickHeart;

    public HouseNewsViewHolder(View view) {
        this.view = view;
        ButterKnife.bind(this, view);
        initRippleViews();
    }

    private void initRippleViews() {
        rippleView1.setOnRippleCompleteListener(this);
        rippleView2.setOnRippleCompleteListener(this);
        rippleView3.setOnRippleCompleteListener(this);
        rippleView4.setOnRippleCompleteListener(this);
        rippleView5.setOnRippleCompleteListener(this);
    }

    public void setupViewHolder(BunchHouse bunchHouse) {
        this.bunchHouse = bunchHouse;
        String url = ApiConstant.URL_WEB_SERVICE_GET_IMAGE_HOUSE;

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

    @OnClick({R.id.imvHeart1, R.id.imvHeart2, R.id.imvHeart3, R.id.imvHeart4, R.id.imvHeart5, R.id.tvSeeAll,
            R.id.imvImage1, R.id.imvImage2, R.id.imvImage3, R.id.imvImage4, R.id.imvImage5})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSeeAll: {
                onClickDetail();
                break;
            }
            case R.id.imvHeart1: {
                addHouseToFavorite(0, v);
                break;
            }
            case R.id.imvHeart2: {
                addHouseToFavorite(1, v);
                break;
            }
            case R.id.imvHeart3: {
                addHouseToFavorite(2, v);
                break;
            }
            case R.id.imvHeart4: {
                addHouseToFavorite(3, v);
                break;
            }
            case R.id.imvHeart5: {
                addHouseToFavorite(4, v);
                break;
            }
            default: {
                flagClickHeart = false;
            }
        }
    }

    private void onClickDetail() {
        EventBus.getDefault().post(new MessageEventClickSeeAllViewHolder(bunchHouse.getType()));
    }


    private void addHouseToFavorite(int i, View v) {
        flagClickHeart = true;
        //ripple effect
        switch (i) {
            case 0: {
                rippleView1.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(view.getContext(), 10) + v.getHeight() / 2);
                break;
            }
            case 1: {
                rippleView2.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(view.getContext(), 10) + v.getHeight() / 2);
                break;
            }
            case 2: {
                rippleView3.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(view.getContext(), 10) + v.getHeight() / 2);
                break;
            }
            case 3: {
                rippleView4.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(view.getContext(), 10) + v.getHeight() / 2);
                break;
            }
            case 4: {
                rippleView5.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(view.getContext(), 10) + v.getHeight() / 2);
                break;
            }
        }
    }

    private void startDetailHouseActivity(int i) {
        Intent intent = new Intent(view.getContext(), HouseDetailActivity.class);
        intent.putExtra(ApiConstant._ID, bunchHouse.getCompactHouse(i).getId());
        view.getContext().startActivity(intent);
    }


    @Override
    public void onComplete(RippleView rippleView) {
        if (!flagClickHeart) {
            switch (rippleView.getId()) {
                case R.id.rippleView1: {
                    startDetailHouseActivity(0);
                    break;
                }
                case R.id.rippleView2: {
                    startDetailHouseActivity(1);
                    break;
                }
                case R.id.rippleView3: {
                    startDetailHouseActivity(2);
                    break;
                }
                case R.id.rippleView4: {
                    startDetailHouseActivity(3);
                    break;
                }
                case R.id.rippleView5: {
                    startDetailHouseActivity(4);
                    break;
                }
            }
        } else {
            switch (rippleView.getId()) {
                case R.id.rippleView1: {
                    String id = bunchHouse.getCompactHouse(0).getId();
                    EventBus.getDefault().post(new MessageClickImvHeartOnHouse(id));
                    break;
                }
                case R.id.rippleView2: {
                    String id = bunchHouse.getCompactHouse(1).getId();
                    EventBus.getDefault().post(new MessageClickImvHeartOnHouse(id));
                    break;
                }
                case R.id.rippleView3: {
                    String id = bunchHouse.getCompactHouse(2).getId();
                    EventBus.getDefault().post(new MessageClickImvHeartOnHouse(id));
                    break;
                }
                case R.id.rippleView4: {
                    String id = bunchHouse.getCompactHouse(3).getId();
                    EventBus.getDefault().post(new MessageClickImvHeartOnHouse(id));
                    break;
                }
                case R.id.rippleView5: {
                    String id = bunchHouse.getCompactHouse(4).getId();
                    EventBus.getDefault().post(new MessageClickImvHeartOnHouse(id));
                    break;
                }
            }
        }
    }
}
