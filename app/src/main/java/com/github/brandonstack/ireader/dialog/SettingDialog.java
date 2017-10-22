package com.github.brandonstack.ireader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.github.brandonstack.ireader.R;

/**
 * Created by admin on 2017/10/21.
 */

public class SettingDialog extends Dialog {
    public SettingDialog(Context context) {
        this(context, R.layout.dialog_setting);
    }

    public SettingDialog(Context context, int themResId) {
        super(context, themResId);
    }


}
