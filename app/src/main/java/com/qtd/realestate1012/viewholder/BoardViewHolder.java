package com.qtd.realestate1012.viewholder;

import android.content.Intent;
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
import com.qtd.realestate1012.activity.BoardDetailActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.custom.BlurTransformation;
import com.qtd.realestate1012.custom.RippleView;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.utils.UiUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 8/7/2016.
 */
public class BoardViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
    @BindView(R.id.imvBoard)
    ImageView imvBoard;

    @BindView(R.id.tvCount)
    TextView tvCount;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.rippleView)
    RippleView rippleView;

    private String id;
    private String name;

    public BoardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        initViews();
    }

    private void initViews() {
        rippleView.setOnRippleCompleteListener(this);
    }

    private void initSize(int position) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((AppCompatActivity) itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((width / 2) * 0.85));

        int marginLow = (int) UiUtils.convertDpToPixel(itemView.getContext(), 8);
        int marginHigh = (int) UiUtils.convertDpToPixel(itemView.getContext(), 24);

        if (position == 0 || position == 1) {
            layoutParams.topMargin = (int) UiUtils.convertDpToPixel(itemView.getContext(), 20);
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
        id = board.getId();
        name = board.getName();

        initSize(position);

        tvName.setText(board.getName());
        tvCount.setText(String.format(Locale.getDefault(), "%d %s", board.getListHouse().size(), itemView.getContext().getString(R.string.houses)));
        Glide.with(itemView.getContext())
                .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE + board.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(itemView.getContext()))
                .placeholder(R.color.colorFacebookGray)
                .error(R.color.colorFacebookGray)
                .into(imvBoard);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        Intent intent = new Intent(itemView.getContext(), BoardDetailActivity.class);
        intent.putExtra(ApiConstant._ID, id);
        intent.putExtra(ApiConstant.NAME, name);
        itemView.getContext().startActivity(intent);
    }
}
