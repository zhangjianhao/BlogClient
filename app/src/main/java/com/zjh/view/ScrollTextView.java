package com.zjh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 张建浩 on 2015/11/12.
 */
public class ScrollTextView extends TextView {
    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
