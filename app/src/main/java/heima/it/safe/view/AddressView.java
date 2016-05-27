package heima.it.safe.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import heima.it.safe.R;
import heima.it.safe.utils.SpUtil;

/**
 * 作者：张琦 on 2016/5/27 12:48
 */
public class AddressView implements View.OnTouchListener {
    private WindowManager mWm;
    private View mTv;
    private Context mContext;
    private int mStartX;
    private int mStartY;
    private WindowManager.LayoutParams mMParams;


    public AddressView(Context context){
        this.mContext = context;
        mWm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        /**
         * 属性
         */
        mMParams = new WindowManager.LayoutParams();
        mMParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mMParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mMParams.format = PixelFormat.TRANSLUCENT;
        mMParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        mMParams.setTitle("Toast");
        mMParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }


    /**
     * 展示归属地window
     */
    public void showAddress(String location){
        int styleRes = SpUtil.getInt(mContext, "addressstyle", R.drawable.shape_address_toast_bg_gray);
        mTv = View.inflate(mContext, R.layout.address_view,null);
        mTv.setBackgroundResource(styleRes);
        /**
         * 给View设置触摸事件
         */
        mTv.setOnTouchListener(this);
        /**
         * 拿到归属地
         */
        TextView address_view_tv = (TextView) mTv.findViewById(R.id.address_view_tv);
        address_view_tv.setText(location);
        mWm.addView(mTv, mMParams);
    }

    /**
     * 销毁归属地window
     */
    public void destroyWindow() {
        if(mTv != null){
            if(mTv.getParent() != null){
                mWm.removeView(mTv);
                mWm = null;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getRawX();
                mStartY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getRawX();
                int currentY = (int) event.getRawY();

                int moveX = currentX - mStartX;
                int moveY = currentY - mStartY;

                mMParams.x += moveX;
                mMParams.y += moveY;

                mWm.updateViewLayout(mTv,mMParams);

                mStartX = currentX;
                mStartY = currentY;
                break;
        }
        return true;
    }
}
