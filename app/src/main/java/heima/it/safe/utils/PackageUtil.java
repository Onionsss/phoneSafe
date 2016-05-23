package heima.it.safe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 作者：张琦 on 2016/5/23 08:28
 */
public class PackageUtil {
    /**
     * 拿到版本名
     *
     * @return 版本名VersionName
     */
    public static String getVersionName(Context context) {

        try {
            //拿到包管理者  并且获得信息API
            PackageInfo packgeInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = packgeInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拿到版本号  用于判断是否需要更新
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        try {
            //拿到包管理者  并且获得信息API
            PackageInfo packgeInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int versionCode = packgeInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
