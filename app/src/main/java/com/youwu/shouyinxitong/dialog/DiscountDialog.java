package com.youwu.shouyinxitong.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.AttachPopupView;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.view.MyCustKeybords;


public class DiscountDialog extends AttachPopupView {


    MyCustKeybords customKeyboard;
    private EditText editText;


    public DiscountDialog(@NonNull Context context, EditText editText) {
        super(context);
        this.editText = editText;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        customKeyboard=findViewById(R.id.custom_keyboard);
        customKeyboard.bindEditText(editText);
        customKeyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {
                dismiss();
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_dicount;
    }

    protected Drawable getPopupBackground() {
        return new GradientDrawable();
    }

}
