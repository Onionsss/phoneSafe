package heima.it.safe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import heima.it.safe.dao.BlackNumberDaoImpl;
import heima.it.safe.db.BlackListConstants;

/**
 * 骚扰拦截服务
 */
public class BlackNumberService extends Service {

    private BlackNumberReceiver mBnr;
    private BlackNumberDaoImpl mBndi;
    private TelephonyManager mTm;
    private MyPhoneStateListener mMpsl;

    public BlackNumberService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 监听短信
         */
        mBndi = new BlackNumberDaoImpl(this);
        mBnr = new BlackNumberReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mBnr, filter);

        /**
         * 监听电话
         */
        mMpsl = new MyPhoneStateListener();
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mTm.listen(mMpsl, PhoneStateListener.LISTEN_CALL_STATE);

    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    /**
                     * 来电响铃状态
                     */
                    String mode = mBndi.findMode(incomingNumber);
                    if(mode.equals(BlackListConstants.MODE_NONE)){
                        /**
                         * 不是黑名单中的号码
                         */
                        return;
                    }
                    if(mode.equals(BlackListConstants.MODE_1) || mode.equals(BlackListConstants.MODE_3)){
                        /**
                         * 需要拦截
                         */
//                        // 4. ServiceManager被系统隐藏了, 通过反射调用它的方法
//                        try {
//                            // 获得字节码
//                            Class<?> clazz = Class.forName("android.os.ServiceManager");
//                            // 通过字节码对象获取方法
//                            Method method = clazz.getDeclaredMethod("getService", String.class);
//                            // 执行方法
//                            IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
//                            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
//                            iTelephony.endCall();
//                            // 删除通话记录
//                            deleteCalllog(incomingNumber);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
            }
        }
    }

    /**
     * 删除通信录里的数据
     * @param number
     */
    private void deleteCalllog(final String number) {
        // 访问别人的数据库, 要使用ContentResolver
        final ContentResolver resolver = getContentResolver();
        final Uri url = CallLog.Calls.CONTENT_URI;
        // 注册内容观察者, 当指定的uri数据发生变化时, ContentObserver 里的 onChange 方法会被调用
        resolver.registerContentObserver(url, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                String where = CallLog.Calls.NUMBER + " = ?";
                String[] selectionArgs = new String[]{number};
                resolver.delete(url, where, selectionArgs);
            }
        });
    };

    /**
     * 用于拦截短信和电话
     */
    class BlackNumberReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : pdus) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                String phone = sms.getOriginatingAddress();

                String mode = mBndi.findMode(phone);
                if (mode.equals(BlackListConstants.MODE_NONE)) {
                    /**
                     * 不是黑名单的内容
                     */
                    return;
                }

                if (mode.equals(BlackListConstants.MODE_2) || mode.equals(BlackListConstants.MODE_3)) {
                    /**
                     * 需要拦截
                     */
                    abortBroadcast();
                    Log.d("TAG", "onReceive: 已经拦截");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * 反注册服务
         */
        if (mBnr != null) {
            unregisterReceiver(mBnr);
            mBnr = null;
        }
        /**
         * 解除电话监听
         */
        mTm.listen(mMpsl, PhoneStateListener.LISTEN_NONE);
    }
}
