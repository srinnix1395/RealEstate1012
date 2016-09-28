package com.qtd.realestate1012.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.ItemSavedSearch;
import com.qtd.realestate1012.viewholder.SearchesViewHolder;

import java.util.ArrayList;

/**
 * Created by DELL on 8/22/2016.
 */
public class SearchesAdapter extends RecyclerView.Adapter<SearchesViewHolder> {
    private ArrayList<ItemSavedSearch> arrayList;

    public SearchesAdapter(ArrayList<ItemSavedSearch> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public SearchesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_searches, parent, false);
        return new SearchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchesViewHolder holder, int position) {
        holder.setupDataViewHolder(arrayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
