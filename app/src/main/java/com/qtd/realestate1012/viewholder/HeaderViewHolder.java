package com.qtd.realestate1012.viewholder;

import android.view.View;
import android.widget.TextView;

import com.qtd.realestate1012.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 8/8/2016.
 */
public class HeaderViewHolder {
    @BindView(R.id.tvHeader)
    TextView tvHeader;

    public HeaderViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void setupViewHolder(String header) {
        tvHeader.setText(header);
    }
}
