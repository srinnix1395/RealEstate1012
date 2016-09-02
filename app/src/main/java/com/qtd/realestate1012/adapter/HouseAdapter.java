package com.qtd.realestate1012.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.viewholder.HouseViewHolder;

import java.util.ArrayList;

/**
 * Created by DELL on 8/18/2016.
 */
public class HouseAdapter extends RecyclerView.Adapter<HouseViewHolder> {
    private ArrayList<CompactHouse> arrayList;

    public HouseAdapter(ArrayList<CompactHouse> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_house, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HouseViewHolder holder, int position) {
        holder.setupDataViewHolder(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
