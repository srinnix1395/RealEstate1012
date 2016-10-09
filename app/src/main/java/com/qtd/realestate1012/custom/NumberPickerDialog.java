package com.qtd.realestate1012.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.callback.OnDismissDialogCallback;
import com.qtd.realestate1012.constant.AppConstant;

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
    private int type;
    private int result = 0;
    private int data;

    public NumberPickerDialog(Context context, int type, OnDismissDialogCallback callback) {
        super(context);
        this.type = type;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_number_picker);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        switch (type) {
            case AppConstant.TYPE_PICKER_AREA: {
                numberPicker.setMinValue(10);
                break;
            }
            case AppConstant.TYPE_PICKER_NUMBER_OF_ROOM: {
                numberPicker.setMinValue(1);
                break;
            }
        }
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
    public void setOnDismissListener(OnDismissListener listener) {
        callback.onDismissListener(result, data);
        super.setOnDismissListener(listener);
    }
}
