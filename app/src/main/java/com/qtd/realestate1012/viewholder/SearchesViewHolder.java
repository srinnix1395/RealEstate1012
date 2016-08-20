package com.qtd.realestate1012.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.callback.ViewHolderFavoriteCallback;
import com.qtd.realestate1012.model.ItemSearch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 8/18/2016.
 */
public class SearchesViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvArea)
    TextView tvArea;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.imvHeart)
    ImageView imvHeart;

    private ViewHolderFavoriteCallback callback;
    private int position;

    public SearchesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void setupDataViewHolder(ItemSearch itemSearch, int position) {
        this.position = position;
    }

    @OnClick(R.id.imvHeart)
    void onClick() {
        callback.onClickImvHeart(position);
    }
}
