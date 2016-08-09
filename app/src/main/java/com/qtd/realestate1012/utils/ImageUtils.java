package com.qtd.realestate1012.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

/**
 * Created by Dell on 7/31/2016.
 */
public class ImageUtils {
    public static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static Bitmap getBitmapFromDrawable(Context context, int resID) {
        Drawable d = ContextCompat.getDrawable(context, resID);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        d.draw(new Canvas(bitmap));

        return bitmap;
    }
}
