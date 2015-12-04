package com.julun.widgets.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2015-12-01.
 */
public class AutoStartSearchEditText extends EditText implements TextWatcher {


    public AutoStartSearchEditText(Context context) {
        super(context);
    }

    public AutoStartSearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void beforeTextChanged(CharSequence s, int start, int count, int after){

    }
    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     */
    public void onTextChanged(CharSequence s, int start, int before, int count){

    }

    public void afterTextChanged(Editable s){

    }
}
