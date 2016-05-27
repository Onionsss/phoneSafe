package heima.it.safe.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import heima.it.safe.R;
import heima.it.safe.service.RocketService;

/**
 * 作者：张琦 on 2016/5/27 13:18
 */
public class RocketView implements View.OnTouchListener {
    private boolean isShot;
    private WindowManager mWm;
    private WindowManager.LayoutParams mMParams;
    private WindowManager.LayoutParams mTParams;
    private Context mContext;
    private ImageView mIv;
    private int mStartX;
    private int mStartY;
    private final WindowManager mTipWm;
    private ImageView mMTips;

    private int[] rocket;
    private int[] tips;
    private AnimationDrawable mBackground;

    public RocketView(Context context){
        this.mContext = context;

        mWm = (WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE);
        /**
         * 属性
         */
        mMParams = new WindowManager.LayoutParams();
        mMParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mMParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mMParams.format = PixelFormat.TRANSLUCENT;
        mMParams.gravity = Gravity.LEFT+Gravity.TOP;
        mMParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        mMParams.setTitle("Toast");
        mMParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;


        mTipWm = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        mTParams = new WindowManager.LayoutParams();
        mTParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mTParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mTParams.format = PixelFormat.TRANSLUCENT;
        mTParams.gravity = Gravity.BOTTOM+Gravity.CENTER_HORIZONTAL;
        mTParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mTParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        rocket = new int[2];
        tips = new int[2];
    }

    /**
     * 展示火箭
     */
    public void showRocket(){
        mIv = new ImageView(mContext);
        mIv.setBackgroundResource(R.drawable.rocket_mode);
        AnimationDrawable background = (AnimationDrawable) mIv.getBackground();
        background.start();
        mIv.setOnTouchListener(this);
        mWm.addView(mIv,mMParams);
    }

    /**
     * 销毁火箭
     */
    public void stopRocket(){
        if(mIv != null){
            if(mIv.getParent() != null){
                mWm.removeView(mIv);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getRawX();
                mStartY = (int) event.getRawY();
                showTips();
                break;

            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getRawX();
                int currentY = (int) event.getRawY();

                int moveX = currentX - mStartX;
                int moveY = currentY - mStartY;

                mMParams.x += moveX;
                mMParams.y += moveY;

                mWm.updateViewLayout(mIv,mMParams);

                mStartX = currentX;
                mStartY = currentY;


                /**
                 * 火箭在屏幕的X,Y方向
                 * 拿到这个View离屏幕的距离
                 */
                mIv.getLocationOnScreen(rocket);
                int rocketX = rocket[0];
                int rocketY = rocket[1];
                /**
                 * tips在屏幕的方向
                 */
                mMTips.getLocationOnScreen(tips);
                int tipsX = tips[0];
                int tipsY = tips[1];
                /**
                 *  && rocketX+mIv.getWidth() > tipsX
                 && rocketX > tipsX+mMTips.getWidth()
                 */
                if(rocketY + mIv.getHeight() > tipsY & rocketX+mIv.getWidth() > tipsX
                        & rocketX < tipsX+mMTips.getWidth()) {
                    /**
                     * 停止动画 记录坐标
                     */
                    stopTipsAnim();
                    isShot = true;


                }else{
                    isShot = false;
                    startTipsAnim();
                }
                break;
            case MotionEvent.ACTION_UP:
                stopTips();
                if(isShot){
                    shotRocket();
                }else{

                }
                break;
        }
        return true;
    }

    private void shotRocket() {
        ValueAnimator va = ValueAnimator.ofInt(mMParams.y,0);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMParams.y = (int) animation.getAnimatedValue();
                mWm.updateViewLayout(mIv,mMParams);
            }
        });
        va.setDuration(500);
        va.start();
        /**
         * 在动画执行完后
         * 注销服务
         */
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                mContext.stopService(new Intent(mContext, RocketService.class));
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

    }

    /**
     * 展示提示  初始化
     */
    private void showTips() {
        mMTips = new ImageView(mContext);
        mTipWm.addView(mMTips,mTParams);
        /**
         * 展示提示
         */
        startTipsAnim();
        mMTips.setOnTouchListener(this);

    }

    private void stopTips(){
        if(mMTips != null){
            if(mMTips.getParent() != null){
                mTipWm.removeView(mMTips);
            }
        }
    }
    private void startTipsAnim(){
        mMTips.setBackgroundResource(R.drawable.rocket_tishi);
        mBackground = (AnimationDrawable) mMTips.getBackground();
        mBackground.start();
    }
    private void stopTipsAnim(){
        mBackground.stop();
        mMTips.setBackgroundResource(R.drawable.desktop_bg_tips_3);
    }
}
