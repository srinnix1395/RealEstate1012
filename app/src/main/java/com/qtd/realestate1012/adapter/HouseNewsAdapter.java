package com.qtd.realestate1012.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.custom.PinnedSectionListView;
import com.qtd.realestate1012.model.BunchHouse;
import com.qtd.realestate1012.viewholder.HeaderViewHolder;
import com.qtd.realestate1012.viewholder.HouseNewsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 8/7/2016.
 */
public class HouseNewsAdapter extends ArrayAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_HOUSES = 1;

    private ArrayList<Object> arrayList;

    public HouseNewsAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        arrayList = (ArrayList<Object>) objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (getItemViewType(position) == VIEW_TYPE_HOUSES) {
            HouseNewsViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_house_news, parent, false);
                viewHolder = new HouseNewsViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (HouseNewsViewHolder) view.getTag();
            }
            viewHolder.setupViewHolder((BunchHouse) arrayList.get(position));
        } else {
            HeaderViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_header, parent, false);
                viewHolder = new HeaderViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (HeaderViewHolder) view.getTag();
            }
            viewHolder.setupViewHolder(arrayList.get(position).toString());
        }
        return view;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof String) {
            return VIEW_TYPE_HEADER;
        }

        return VIEW_TYPE_HOUSES;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == VIEW_TYPE_HEADER;
    }
}
