package heima.it.safe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import heima.it.safe.service.LocationService;
import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

/**
 * 监听发送来的短信的广播 主要用于号码被盗后的处理
 * 1.播放报警音乐
 * 2.远程发送位置
 * 3.清除手机数据
 * 4.远程锁屏并重置锁屏密码
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean project = SpUtil.getBoolean(context, Constant.PROJECT, false);
        if(project){
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for(Object obj : pdus){
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                String body = sms.getMessageBody();
                String phone = sms.getOriginatingAddress();
                /**
                 * 根据拿到的body的不同来决定如何操作
                 */
                if(phone.equals(SpUtil.getString(context,Constant.SAFE_PHONE,""))){
                    if("#*location*#".equals(body)){
                        startSer(context);
                        abortBroadcast();
                    }else if("#*alarm*#".equals(body)){
                        alarm(context);
                        abortBroadcast();
                    }else if("#*wipedata*#".equals(body)){
                        wipeData(context);
                        abortBroadcast();
                    }else if(body.startsWith("#*lockscreen*#")){
                        String substring = body.substring("#*lockscreen*#".length(), body.length());
                        lockSrceen(context,substring);
                        abortBroadcast();
                    }
                }
            }
        }
    }

    /**
     * 清除数据
     */
    private void wipeData(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName who = new ComponentName(context, MyAdminReceiver.class);
        boolean adminActive = dpm.isAdminActive(who);
        if (adminActive) {
            //清除数据
            //0代表恢复出厂设置
            dpm.wipeData(0);
        }

    }
    /**
     * 锁屏页面
     * @param context
     * @param substring
     */
    private void lockSrceen(Context context,String substring) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
        ComponentName who = new ComponentName(context, MyAdminReceiver.class);
        if (dpm.isAdminActive(who)){
            dpm.lockNow();
            if(substring.equals("")){
                dpm.resetPassword("19940407",0);
            }else{
                dpm.resetPassword(substring,0);
            }

        }else{

        }
    }

    /**
     * 开启一个服务 监听地址的变化
     * 当发送完地址时 将这个服务杀死
     * @param context
     */
    private void startSer(Context context) {
        context.startService(new Intent(context,LocationService.class));
    }

    /**
     * 播放音乐
     * @param context
     */
    private void alarm(Context context) {
        Log.d(TAG, "alarm: "+111);
        MediaPlayer mp = MediaPlayer.create(context, R.raw.alarm);
        mp.start();
    }
}
