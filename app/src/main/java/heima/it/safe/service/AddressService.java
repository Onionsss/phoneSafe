package heima.it.safe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

public class AddressService extends Service {

    private AddressReceiver mAr;
    private TelephonyManager mTm;
    private MyAddressPhoneState mMaps;
    private WindowManager mWm;
    private TextView mTv;

    public AddressService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 来电的归属地显示
         */
        mAr = new AddressReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mAr,filter);

        /**
         * 去电归属地的显示
         */
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mMaps = new MyAddressPhoneState();
        mTm.listen(mMaps,PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 展示归属地window
     */
    private void showWindow() {
        mWm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        /**
         * 属性
         */
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mTv = new TextView(this);
        mTv.setText("呵呵");
        mTv.setTextSize(25);
        mWm.addView(mTv,mParams);
    }

    /**
     * 销毁归属地window
     */
    private void destroyWindow() {
        if(mTv != null){
            if(mTv.getParent() != null){
                mWm.removeView(mTv);
                mWm = null;
            }
        }
    }

    class MyAddressPhoneState extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    destroyWindow();
                break;
                case TelephonyManager.CALL_STATE_RINGING:
                    showWindow();
                    break;
            }
        }
    }



    class AddressReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            showWindow();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /**
         * 解除注册
         */
        if(mAr != null){
            unregisterReceiver(mAr);
            mAr = null;
        }

        /**
         * 解除监听
         */
        if(mTm != null){
            mTm.listen(mMaps,PhoneStateListener.LISTEN_NONE);
            mTm = null;
        }
    }
}
