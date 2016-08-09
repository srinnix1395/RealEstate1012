package com.qtd.realestate1012.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.qtd.realestate1012.R;

/**
 * Created by Dell on 7/31/2016.
 */
public class SnackbarUtils {
    public static void showSnackbar(View view) {
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
}
