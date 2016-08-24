package com.qtd.realestate1012.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.viewholder.BoardViewHolder;

import java.util.ArrayList;

/**
 * Created by Dell on 8/7/2016.
 */
public class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> {
    private ArrayList<Board> arrayList;

    public BoardAdapter(ArrayList<Board> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_board, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, int position) {
        holder.setupViewHolder(arrayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
