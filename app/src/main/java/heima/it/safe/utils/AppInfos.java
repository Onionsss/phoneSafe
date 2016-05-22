package heima.it.safe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import heima.it.safe.bean.AppInfo;

/**
 * 作者：张琦 on 2016/5/21 21:33
 * 返回所有APP的数据
 */
public class AppInfos {
    public static List<AppInfo> getAllApps(Context context){
        List<AppInfo> list = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> install = pm.getInstalledPackages(0);
        for(PackageInfo info : install){
            AppInfo app = new AppInfo();
            /**
             * 拿到app的icon
             */
            Drawable drawable = info.applicationInfo.loadIcon(pm);
            app.setAppIcon(drawable);
            /**
             * 拿到app的名字
             */
            String appName = (String) info.applicationInfo.loadLabel(pm);
            app.setAppName(appName);
            /**
             * 拿到app的size
             */
            String sourceDir = info.applicationInfo.sourceDir;
            long appSize = new File(sourceDir).length();
            app.setAppSize(appSize);
            /**
             * app的位置信息
             */
            int flags = info.applicationInfo.flags;
            if((flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                app.setSystem(true);
            }
            if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                app.setRom(false);
            }else{
                app.setRom(true);
            }
            String packageName = info.applicationInfo.packageName;
            app.setPackageName(packageName);
            /**
             * 将查询到的所有信息添加到list集合中  并且返回
             */
            list.add(app);
        }
        return list;
    }
}
