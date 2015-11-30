package com.julun.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2015-10-15.
 */
public class FontManager {
    public static final String ROOT = "fonts/",
    FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    /**
     * 如果你想在项目中使用其他的字体，把字体放在helper 类里面就可以了。类似于：
     *  yourTextView.setTypeface(FontManager.getTypeface(FontManager.YOURFONT));
     *
     *  我们需要做的就这么多，但是我们可以做的更好。
     *  使用上面的方法的话，我们需要为每个想当成图标来使用的TextView创建一个变量。
     *  但作为一个程序员，我们都很懒，对吧？
     *  图标一般都是包含在一个ViewGroup，比如一个RelativeLayout或者LinearLayout中。
     *  我们可以写一个方法，爬遍指定xml parent 并且递归的覆盖每个TextView的字体。
     *
     * @param v
     * @param typeface
     */
    public static void markAsIconContainer(View v, Typeface typeface) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                markAsIconContainer(child,typeface);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(typeface);
        }
    }
}
