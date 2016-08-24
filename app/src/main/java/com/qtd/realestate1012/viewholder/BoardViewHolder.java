package com.qtd.realestate1012.viewholder;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.custom.BlurTransformation;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 8/7/2016.
 */
public class BoardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imvBoard)
    ImageView imvBoard;

    @BindView(R.id.tvCount)
    TextView tvCount;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.cardView)
    CardView cardView;

    public BoardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    private void initSize(int position) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((AppCompatActivity) itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((width / 2) * 0.85));

        int marginLow = (int) ImageUtils.convertDpToPixel(itemView.getContext(), 8);
        int marginHigh = (int) ImageUtils.convertDpToPixel(itemView.getContext(), 24);

        if (position == 0 || position == 1) {
            layoutParams.topMargin = (int) ImageUtils.convertDpToPixel(itemView.getContext(), 20);
        } else {
            layoutParams.topMargin = marginLow;
        }

        layoutParams.bottomMargin = marginLow;

        if (position % 2 == 0) {
            layoutParams.leftMargin = marginHigh;
            layoutParams.rightMargin = marginLow;
        } else {
            layoutParams.leftMargin = marginLow;
            layoutParams.rightMargin = marginHigh;
        }

        cardView.setLayoutParams(layoutParams);
    }

    public void setupViewHolder(Board board, int position) {
        initSize(position);
        tvName.setText(board.getName());
        tvCount.setText(board.getNumberOfHouse() + itemView.getContext().getString(R.string.houses));
        Glide.with(itemView.getContext())
                .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE + board.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(itemView.getContext()))
                .placeholder(R.color.colorFacebook)
                .error(R.color.colorFacebook)
                .into(imvBoard);

    }
}
