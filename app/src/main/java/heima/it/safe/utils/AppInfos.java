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
        /**
         * 包管理器
         */
        PackageManager pm = context.getPackageManager();
        /**
         * 通过包管理器拿到已下载的App包信息
         * 代表manifest节点
         */
        List<PackageInfo> install = pm.getInstalledPackages(0);
        for(PackageInfo info : install){
            AppInfo app = new AppInfo();
            /**
             * 通过manifest节点拿到application节点
             * 通过这个节点可以load出application节点的下的属性
             */
            ApplicationInfo application = info.applicationInfo;
            /**
             * 拿到app的icon
             */
            Drawable drawable = application.loadIcon(pm);
            app.setAppIcon(drawable);
            /**
             * 拿到app的名字
             */
            String appName = (String) application.loadLabel(pm);
            app.setAppName(appName);
            /**
             * 拿到app的size
             */
            String sourceDir = application.sourceDir;
            long appSize = new File(sourceDir).length();
            app.setAppSize(appSize);
            /**
             * app的位置信息
             */
            int flags = application.flags;
            if((flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                app.setSystem(true);
            }
            if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                app.setRom(false);
            }else{
                app.setRom(true);
            }
            String packageName = application.packageName;
            app.setPackageName(packageName);
            /**
             * 将查询到的所有信息添加到list集合中  并且返回
             */
            list.add(app);
        }
        return list;
    }
}
