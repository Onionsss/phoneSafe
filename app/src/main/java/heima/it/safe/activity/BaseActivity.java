package heima.it.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import heima.it.safe.R;

public abstract class BaseActivity extends AppCompatActivity {
    GestureDetector gesture;
    private int mWidthPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mWidthPixels = displayMetrics.widthPixels/4;

        gesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                /**
                 * 滑动Y超过屏幕1/4  则忽略
                 */
                if(Math.abs(e1.getY()-e2.getY()) > mWidthPixels){
                    return false;
                }
                /**
                 * 滑动X超过屏幕1/4 则滑动
                 */
                if(e2.getX() - e1.getX() > mWidthPixels){
                    return1();
                }else if(e1.getX()  - e2.getX() > mWidthPixels){
                    next();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void nextClick(View view){
        next();
    }
    public void returnClick(View view){
        return1();
    }
    public void next(){
        startActivity(new Intent(this,getNextClass()));
        finish();
        overridePendingTransition(R.anim.activity_right_enter,R.anim.activity_left_exit);
    }
    public void return1(){
        if(getReturnClass() == null){
            return;
        }
        startActivity(new Intent(this,getReturnClass()));
        finish();
        overridePendingTransition(R.anim.activity_left_enter,R.anim.activity_right_exit);
    }
    /**
     * 必须需要子类继承
     * @return
     */
    public abstract Class<? extends Activity> getNextClass();

    public abstract Class<? extends Activity> getReturnClass();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gesture.onTouchEvent(event);
    }
}
