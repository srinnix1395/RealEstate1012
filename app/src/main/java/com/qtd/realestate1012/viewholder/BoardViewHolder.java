package com.qtd.realestate1012.viewholder;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.Board;

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

    public BoardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setupViewHolder(Board board) {
        tvName.setText(board.getName());
        tvCount.setText(board.getCount() + itemView.getContext().getString(R.string.houses));
        if (board.getCount() == 0) {
            imvBoard.setImageBitmap(null);
            imvBoard.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorDarkGray));
        } else {
            Glide.with(itemView.getContext())
                    .load(board.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(null)
                    .into(imvBoard);
            imvBoard.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
