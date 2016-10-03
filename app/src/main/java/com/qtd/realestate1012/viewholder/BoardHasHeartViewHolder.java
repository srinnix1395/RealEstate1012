package com.qtd.realestate1012.viewholder;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.custom.BlurTransformation;
import com.qtd.realestate1012.custom.RippleView;
import com.qtd.realestate1012.messageevent.MessageClickImvHeartOnBoard;
import com.qtd.realestate1012.model.BoardHasHeart;
import com.qtd.realestate1012.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 8/29/2016.
 */
public class BoardHasHeartViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imvBoard)
    ImageView imvBoard;

    @BindView(R.id.tvCount)
    TextView tvCount;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.imvHeart)
    ImageView imvHeart;

    @BindView(R.id.rippleView)
    RippleView rippleView;

    private BoardHasHeart board;

    public BoardHasHeartViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickImvHeart(imvHeart);
            }
        });
    }


    protected void initSize(boolean lastItem) {
        DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
        int width = displaymetrics.widthPixels;

        CardView.LayoutParams layoutParams = new CardView.LayoutParams((int) ((width / 2) * 0.8), (int) ((width / 2) * 0.8));

        int marginLow = (int) UiUtils.convertDpToPixel(itemView.getContext(), 8);
        int marginHigh = (int) UiUtils.convertDpToPixel(itemView.getContext(), 16);

        layoutParams.topMargin = marginLow;
        layoutParams.bottomMargin = marginLow;
        layoutParams.leftMargin = marginLow;

        if (lastItem) {
            layoutParams.rightMargin = marginHigh;
        } else {
            layoutParams.rightMargin = marginLow;
        }
        cardView.setLayoutParams(layoutParams);
    }

    public void setupViewHolder(BoardHasHeart board, boolean lastItem) {
        this.board = board;

        initSize(lastItem);
        tvName.setText(board.getName());
        tvCount.setText(String.format(Locale.getDefault(), "%d %s", board.getListHouse().size(), itemView.getContext().getString(R.string.houses)));
        Glide.with(itemView.getContext())
                .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE_HOUSE + board.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(itemView.getContext()))
                .placeholder(R.color.colorFacebookGray)
                .error(R.color.colorFacebookGray)
                .into(imvBoard);
        if (board.isLiked()) {
            imvHeart.setImageResource(R.drawable.ic_heart_pink_small);
        } else {
            imvHeart.setImageResource(R.drawable.ic_heart_outline);
        }
    }

    @OnClick(R.id.imvHeart)
    void onClickImvHeart(View v) {
        //ripple effect
        rippleView.animateRipple(v.getLeft() + v.getWidth() / 2, v.getTop() + UiUtils.convertPixelsToDp(itemView.getContext(), 10) + v.getHeight() / 2);

        if (board.isLiked()) {
            EventBus.getDefault().post(new MessageClickImvHeartOnBoard(board, ApiConstant.ACTION_DELETE));
        } else {
            EventBus.getDefault().post(new MessageClickImvHeartOnBoard(board, ApiConstant.ACTION_ADD));
        }
    }
}
