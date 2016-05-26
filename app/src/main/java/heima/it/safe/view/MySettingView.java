package heima.it.safe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import heima.it.safe.R;

/**
 * 作者：张琦 on 2016/5/18 19:13
 */
public class MySettingView extends RelativeLayout{
    private boolean mCheck = true;
    private TextView mMysetting_tv;
    private ImageView mMysetting_iv;

    public MySettingView(Context context) {
        super(context);
    }

    public MySettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_mysetting, this);
        mMysetting_tv = (TextView) view.findViewById(R.id.mysetting_tv);
        mMysetting_iv = (ImageView) view.findViewById(R.id.mysetting_iv);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MySettingView);
        String title = a.getString(R.styleable.MySettingView_biaoti);
        boolean mybutton = a.getBoolean(R.styleable.MySettingView_mybutton, true);
        mMysetting_iv.setVisibility(mybutton?View.VISIBLE:View.GONE);
        mMysetting_tv.setText(title);
        int value = a.getInt(R.styleable.MySettingView_img,-1);
        if(value == -1){
            throw new RuntimeException("要设置背景");
        }
        switch(value){
            case 1:
                this.setBackgroundResource(R.drawable.setting_item_first);
            break;
            case 2:
                this.setBackgroundResource(R.drawable.setting_item_mid);
                break;
            case 3:
                this.setBackgroundResource(R.drawable.setting_item_three);
                break;
        }
        a.recycle();
    }
    public boolean isChecked(){
        return mCheck;
    }
    public void setChecked(boolean check) {
        if(check){
            mMysetting_iv.setImageResource(R.mipmap.on);
            mCheck = true;
        }else{
            mMysetting_iv.setImageResource(R.mipmap.off);
            mCheck = false;
        }
    }

    public void check() {
        setChecked(!mCheck);
    }
}
