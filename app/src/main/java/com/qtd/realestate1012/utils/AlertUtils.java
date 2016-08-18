package com.qtd.realestate1012.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qtd.realestate1012.R;

/**
 * Created by Dell on 7/31/2016.
 */
public class AlertUtils {
    public static void showSnackBarNoInternet(View view) {
        final Snackbar snackbar = Snackbar.make(view, R.string.noInternetConnection, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        snackbar.show();
    }

    public static void showToastSuccess(Context context, int resIcon, int resString) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_board_created, null);

        ImageView imvIcon = (ImageView) view.findViewById(R.id.imvIcon);
        imvIcon.setImageResource(resIcon);

        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setText(resString);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);

        toast.show();
    }
}
