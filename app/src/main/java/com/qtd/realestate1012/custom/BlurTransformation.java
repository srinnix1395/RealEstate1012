package com.qtd.realestate1012.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by DELL on 8/22/2016.
 */
public class BlurTransformation extends BitmapTransformation {
    private static final float BITMAP_SCALE = 0.01f;
    private static final float BLUR_RADIUS = 25f;

    private RenderScript rs;
    public BlurTransformation(Context context) {
        super(context);
        rs = RenderScript.create(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int width = Math.round(toTransform.getWidth() * BITMAP_SCALE);
        int height = Math.round(toTransform.getHeight() * BITMAP_SCALE);


        Bitmap inputBitmap = Bitmap.createScaledBitmap(toTransform, width, height, false);
//        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        Bitmap outputBitmap = inputBitmap.copy(Bitmap.Config.ARGB_8888, true);


        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    @Override
    public String getId() {
        return "blur";
    }
}
