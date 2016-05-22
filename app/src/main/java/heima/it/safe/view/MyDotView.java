package heima.it.safe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import heima.it.safe.R;

/**
 * 作者：张琦 on 2016/5/20 14:34
 */
public class MyDotView extends LinearLayout{
    public MyDotView(Context context) {
        this(context,null);
    }

    public MyDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyDotView);

        int color = a.getInteger(R.styleable.MyDotView_dotcolor, -1);
        int count = a.getInteger(R.styleable.MyDotView_dotcount, -1);

        a.recycle();
        for (int i = 1; i <= count; i++) {
            ImageView imageView = new ImageView(context);
            if(i == color){
                imageView.setImageResource(android.R.drawable.presence_online);
            }else{
                imageView.setImageResource(android.R.drawable.presence_offline);
            }
            addView(imageView);
        }

    }
}
