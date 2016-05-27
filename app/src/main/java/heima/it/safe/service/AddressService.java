package heima.it.safe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import heima.it.safe.dao.SqliteDao;
import heima.it.safe.view.AddressView;

public class AddressService extends Service {

    private AddressReceiver mAr;
    private TelephonyManager mTm;
    private MyAddressPhoneState mMaps;
    private AddressView mAv;


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
         * 创建窗口
         */
        mAv = new AddressView(this);
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

    class MyAddressPhoneState extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    mAv.destroyWindow();
                break;
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = SqliteDao.getAddress(getApplicationContext(), incomingNumber);
                    mAv.showAddress(address);
                    break;
            }
        }
    }

    class AddressReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String address = SqliteDao.getAddress(getApplicationContext(), number);
            mAv.showAddress(address);
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
