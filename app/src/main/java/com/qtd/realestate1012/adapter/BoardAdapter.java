package com.qtd.realestate1012.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BoardHasHeart;
import com.qtd.realestate1012.viewholder.BoardHasHeartViewHolder;
import com.qtd.realestate1012.viewholder.BoardViewHolder;

import java.util.ArrayList;

/**
 * Created by Dell on 8/7/2016.
 */
public class BoardAdapter extends RecyclerView.Adapter {
    private ArrayList<Board> arrayList;
    private boolean hasHeart;

    public BoardAdapter(ArrayList<Board> arrayList, boolean hasHeart) {
        this.arrayList = arrayList;
        this.hasHeart = hasHeart;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (!hasHeart) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_board, parent, false);
            return new BoardViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_board_has_heart, parent, false);
            return new BoardHasHeartViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!hasHeart) {
            ((BoardViewHolder) holder).setupViewHolder(arrayList.get(position), position);
        } else {
            ((BoardHasHeartViewHolder) holder).setupViewHolder((BoardHasHeart) arrayList.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
