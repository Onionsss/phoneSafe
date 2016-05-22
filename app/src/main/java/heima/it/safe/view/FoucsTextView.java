package heima.it.safe.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 作者：张琦 on 2016/5/18 10:58
 */
public class FoucsTextView extends TextView{

    @Override
    public boolean isFocused() {
        return true;
    }
    /**
     * 当另一个控件获得焦点时  能让跑马灯继续
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused){
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        /**
         * 当另一个窗体获得焦点时  能让跑马灯继续
         */
        if(hasWindowFocus){
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }

    public FoucsTextView(Context context) {
        super(context);
    }

    public FoucsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public FoucsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
