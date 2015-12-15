package com.julun.widgets.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * 自定义一个带下拉框可选的文本输入框.
 */
public class EditTextWithCombo extends EditText implements TextWatcher {
    private static final String TAG = EditTextWithCombo.class.getName();
    private WeakReference<Context> cxt;

    public EditTextWithCombo(Context context) {
        super(context);
        initThis(context, null);
    }

    public EditTextWithCombo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initThis(context, attrs);

    }

    private void initThis(Context context, AttributeSet attrs) {
        cxt = new WeakReference<Context>(context);
        initAttrs(context, attrs);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        // TODO: 2015-12-14  配置属性...
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged() called with: " + "s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged() called with: " + "s = [" + s + "], start = [" + start + "], count = [" + count + "], after = [" + after + "]");
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged() called with: " + "s = [" + s + "]");
    }
}
