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
import com.qtd.realestate1012.model.BoardHasHeart;
import com.qtd.realestate1012.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public BoardHasHeartViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    protected void initSize() {
        DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
        int width = displaymetrics.widthPixels;

        CardView.LayoutParams layoutParams = new CardView.LayoutParams((int) ((width / 2) * 0.8), (int) ((width / 2) * 0.8));

        int marginLow = (int) UiUtils.convertDpToPixel(itemView.getContext(), 8);

        layoutParams.topMargin = marginLow;
        layoutParams.bottomMargin = marginLow;
        layoutParams.leftMargin = marginLow;
        layoutParams.rightMargin = marginLow;

        cardView.setLayoutParams(layoutParams);
    }

    public void setupViewHolder(BoardHasHeart board) {
        initSize();
        tvName.setText(board.getName());
        tvCount.setText(board.getNumberOfHouse() + itemView.getContext().getString(R.string.houses));
        Glide.with(itemView.getContext())
                .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE + board.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(itemView.getContext()))
                .placeholder(R.color.colorFacebookGray)
                .error(R.color.colorFacebookGray)
                .into(imvBoard);
        if (!board.isLiked()) {
            imvHeart.setImageResource(R.drawable.ic_heart_outline_24dp);
        } else {
            imvHeart.setImageResource(R.drawable.ic_heart_pink_small);
        }
    }
}