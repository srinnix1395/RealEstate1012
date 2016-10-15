package com.qtd.realestate1012.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.callback.OnDismissDialogCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 9/5/2016.
 */
public class NumberPickerDialog extends Dialog {
    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    private OnDismissDialogCallback callback;
    private int result = 0;
    private int data;

    public NumberPickerDialog(Context context, OnDismissDialogCallback callback) {
        super(context);
        this.callback = callback;
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_number_picker);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initViews() {
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void initData() {
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
    }

    @OnClick(R.id.tvCancel)
    void onClickCancel() {
        result = Activity.RESULT_CANCELED;
        dismiss();
    }

    @OnClick(R.id.tvOK)
    void onClickOK() {
        result = Activity.RESULT_OK;
        data = numberPicker.getValue();
        dismiss();
    }

    @Override
    public void dismiss() {
        callback.onDismiss(result,data);
        super.dismiss();
    }
}
