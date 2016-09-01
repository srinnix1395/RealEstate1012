package com.qtd.realestate1012.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.viewholder.CardHouseViewHolder;

import java.util.ArrayList;

/**
 * Created by DELL on 9/1/2016.
 */
public class HouseCardViewAdapter extends RecyclerView.Adapter<CardHouseViewHolder> {
    private ArrayList<CompactHouse> arrayList;

    public HouseCardViewAdapter(ArrayList<CompactHouse> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public CardHouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_house_card_view, parent, false);
        return new CardHouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardHouseViewHolder holder, int position) {
        holder.setupDataViewHolder(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
