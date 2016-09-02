package com.qtd.realestate1012.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.viewholder.HouseHasHeartViewHolder;

import java.util.ArrayList;

/**
 * Created by DELL on 8/17/2016.
 */
public class HouseHasHeartAdapter extends RecyclerView.Adapter<HouseHasHeartViewHolder> {
    private ArrayList<CompactHouse> arrayList;

    public HouseHasHeartAdapter(ArrayList<CompactHouse> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public HouseHasHeartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_house_has_heart, parent, false);
        return new HouseHasHeartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HouseHasHeartViewHolder holder, int position) {
        holder.setupDataViewHolder(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
