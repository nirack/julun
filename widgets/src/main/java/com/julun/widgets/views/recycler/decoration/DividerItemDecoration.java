package com.julun.widgets.views.recycler.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2015-10-22.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };
    public static final int  HORIZONTAL_LIST  = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable divider;
    private int orientation;

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if(orientation != VERTICAL_LIST && orientation != HORIZONTAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if(orientation == HORIZONTAL_LIST){
            drawHorizontal(c,parent);
        }else{
            drawVertical(c, parent);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int index = 0; index < childCount; index++) {
            final View child = parent.getChildAt(index);
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + divider.getIntrinsicHeight();
            divider.setBounds(left,right,top,bottom);
            divider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int index = 0; index < childCount; index++) {
            final View child = parent.getChildAt(index);
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left,right,top,bottom);
            divider.draw(c);

        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        if(orientation == VERTICAL_LIST){
            outRect.set(0,0,0,divider.getIntrinsicHeight());
        }else {
            outRect.set(0,0,divider.getIntrinsicWidth(),0);
        }
    }

}
