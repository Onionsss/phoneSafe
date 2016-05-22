package heima.it.safe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

public class SmsReceiver extends BroadcastReceiver {
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
                if(phone.equals(SpUtil.getString(context,Constant.SAFE_PHONE,""))){
                    if("#*location*#".equals(body)){

                    }else if("#*alarm*#".equals(body)){
                        alarm(context);
                    }else if("#*wipedata*#".equals(body)){

                    }else if("#*lockscreen*#".equals(body)){

                    }
                }
            }
        }
    }

    /**
     * 播放音乐
     * @param context
     */
    private void alarm(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.alarm);
        mp.start();
    }
}
