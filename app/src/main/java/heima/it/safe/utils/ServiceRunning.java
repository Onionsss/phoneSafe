package heima.it.safe.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * 作者：张琦 on 2016/5/24 18:53
 */
public class ServiceRunning {
    /**
     * 判断服务是否正在运行
     * @param context
     * @param serviceClazz
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClazz) {
        // ActivityManager 相当于 Windows里的任务管理器
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 参数表示最大值
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            ComponentName cmpName = runningServiceInfo.service;
            String className = cmpName.getClassName();
            if(TextUtils.equals(className, serviceClazz.getName())) {
                return true;
            }
        }
        return false;
    }
}
