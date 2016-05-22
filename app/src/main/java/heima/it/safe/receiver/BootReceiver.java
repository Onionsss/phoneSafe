package heima.it.safe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean project = SpUtil.getBoolean(context, Constant.PROJECT, false);
        if(project){
            String localSim = SpUtil.getString(context, Constant.BINDSIM, "");
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String nowSim = tm.getSimSerialNumber();
            if(localSim.equals(nowSim)){
                String safePhone = SpUtil.getString(context, Constant.SAFE_PHONE, "");
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(safePhone,null,"hey man,you's phone is lost",null,null);
            }
        }
    }
}
