package com.qtd.realestate1012.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.model.BunchHouse;
import com.qtd.realestate1012.viewholder.HeaderViewHolder;
import com.qtd.realestate1012.viewholder.HouseNewsViewHolder;

import java.util.ArrayList;

import de.hamm.pinnedsectionlistview.PinnedSectionListView;

/**
 * Created by Dell on 8/7/2016.
 */
public class HouseNewsAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_HOUSES = 1;

    private ArrayList<Object> arrayList;

    public HouseNewsAdapter(ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
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
        } else if (getItemViewType(position) == VIEW_TYPE_HEADER) {
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
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
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
