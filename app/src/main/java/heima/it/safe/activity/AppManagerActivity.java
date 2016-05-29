package heima.it.safe.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima.it.safe.R;
import heima.it.safe.adapter.AppManagerAdapter;
import heima.it.safe.bean.AppInfo;
import heima.it.safe.utils.AppInfos;
import heima.it.safe.view.MyProgressView;

public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int UNINSTALL = 45;
    private static final String TAG = "AppManagerActivity";
    @Bind(R.id.app_lv)
    ListView app_lv;
    private List<AppInfo> appList;
    private List<AppInfo> systemApp = new ArrayList<>();
    private List<AppInfo> userApp = new ArrayList<>();
    private PopupWindow mPw;
    private int lastPosition;
    private AppInfo appInfo;
    private ObjectAnimator mAnimator;
    private AppManagerAdapter mAdapter;
    private ImageView mAppmanager_iv;
    private List<AppInfo> mAllApps;
    private TextView mAppmanager_tv_kind;
    private MyProgressView mAppmanager_mpv_rom;
    private MyProgressView mAppmanager_mpv_sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mAppmanager_tv_kind.setOnClickListener(this);
    }

    private void initView() {
        mAppmanager_iv = (ImageView) findViewById(R.id.appmanager_iv);
        mAppmanager_tv_kind = (TextView) findViewById(R.id.appmanager_tv_kind);
        mAppmanager_mpv_rom = (MyProgressView) findViewById(R.id.appmanager_mpv_rom);
        mAppmanager_mpv_sd = (MyProgressView) findViewById(R.id.appmanager_mpv_sd);
    }

    private void initData() {
        /**
         * rom可用空间
         */
        long romFree = Environment.getDataDirectory().getFreeSpace();
        long romUsable = Environment.getDataDirectory().getUsableSpace();
        long sdFree = Environment.getExternalStorageDirectory().getFreeSpace();
        long sdUsable = Environment.getExternalStorageDirectory().getUsableSpace();
        mAppmanager_mpv_rom.setWhere("内存:");
        mAppmanager_mpv_rom.setUse(Formatter.formatFileSize(this,romUsable));
        mAppmanager_mpv_rom.setHave(Formatter.formatFileSize(this,romFree));
        mAppmanager_mpv_rom.setProgress((int)((romUsable*100)/(romUsable+romFree)));

        mAppmanager_mpv_sd.setWhere("SD卡:");
        mAppmanager_mpv_sd.setUse(Formatter.formatFileSize(this,sdUsable));
        mAppmanager_mpv_sd.setHave(Formatter.formatFileSize(this,sdFree));
        mAppmanager_mpv_sd.setProgress((int)((sdUsable*100)/(sdUsable+sdFree)));
        new Task().execute();
    }

    /**
     * 初始化旋转动画
     */
    private void initAnim() {
        mAnimator = ObjectAnimator.ofFloat(mAppmanager_iv, "rotation", 0, 45, 90, 135, 180, 225, 270, 315, 360);
        mAnimator.setDuration(1000);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(1000);
        mAnimator.start();
    }

    /**
     * 加载数据完成 动画结束
     */
    private void stopAnim() {
        mAnimator.cancel();
    }

    class Task extends AsyncTask<Void, Void, List<AppInfo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initAnim();
        }

        @Override
        protected List<AppInfo> doInBackground(Void... params) {
            SystemClock.sleep(2000);
            appList = AppInfos.getAllApps(AppManagerActivity.this);
            /**
             * 将所有的app分层  分为用户App和系统App
             */
            for (AppInfo info : appList) {
                if (info.isSystem()) {
                    systemApp.add(info);
                } else {
                    userApp.add(info);
                }
            }
            return appList;
        }

        /**
         * 异步执行完之后 调用
         *
         * @param aVoid
         */
        @Override
        protected void onPostExecute(List<AppInfo> aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new AppManagerAdapter(AppManagerActivity.this, systemApp, userApp);
            app_lv.setAdapter(mAdapter);
            /**
             * 适配完数据 隐藏progress 显示listView
             */
            mAppmanager_iv.setVisibility(View.GONE);
            mAppmanager_tv_kind.setVisibility(View.VISIBLE);
            initScrollListener();
            initItemListener();
        }
    }

    /**
     * ListView的滑动事件
     */
    private void initScrollListener() {
        app_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userApp != null && systemApp != null) {
                    if (firstVisibleItem < userApp.size() + 1) {
                        mAppmanager_tv_kind.setText("用户程序(" + userApp.size() + ")个");
                    } else {
                        mAppmanager_tv_kind.setText("系统程序(" + systemApp.size() + ")个");
                    }
                }
            }
        });
    }

    /**
     * ListView的条目点击事件
     */
    private void initItemListener() {
        app_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                if (obj != null && obj instanceof AppInfo) {
                    appInfo = (AppInfo) obj;
                    /**
                     * 显示PopupWindow
                     */
                    View popupView = View.inflate(AppManagerActivity.this, R.layout.appmanager_popup, null);
                    /**
                     * 查找控件设置点击事件
                     */
                if(mPw == null){
                    popupView.findViewById(R.id.popup_ll_uninstall).setOnClickListener(AppManagerActivity.this);
                    popupView.findViewById(R.id.popup_ll_open).setOnClickListener(AppManagerActivity.this);
                    popupView.findViewById(R.id.popup_ll_share).setOnClickListener(AppManagerActivity.this);
                    popupView.findViewById(R.id.popup_ll_info).setOnClickListener(AppManagerActivity.this);
                    mPw = new PopupWindow(popupView, -2, -2);
                    /**
                     * 按别处时pw消失
                     */
                    mPw.setFocusable(true);
                    mPw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mPw.setAnimationStyle(R.style.PopupAnimation);
                }
                    int[] location = new int[2];
                    view.getLocationInWindow(location);
                    mPw.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 80, location[1]);
                }
//                lastPosition = position;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_ll_uninstall:
                unInstall();
                break;
            case R.id.popup_ll_open:
                open();
                break;
            case R.id.popup_ll_share:
                share();
                break;
            case R.id.popup_ll_info:
                info();
                break;
            case R.id.appmanager_tv_kind:
                returnTop();
                break;
        }
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "发现一个好玩的应用: " + appInfo.getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void info() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:"+appInfo.getPackageName()));
        startActivity(intent);
        pwClose();
    }

    /**
     * 如果点击时 将当前第一个显示的条目移动到开始位置
     */
    private void returnTop() {
        String flags = mAppmanager_tv_kind.getText().toString();
        if (flags.contains("用户")) {
            app_lv.setSelection(0);
        } else if (flags.contains("系统")) {
            app_lv.setSelection(userApp.size() + 1);
        }
    }

    /**
     * 打开应用
     */
    private void open() {
        Intent launchIntentForPackage = this.getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName());
        this.startActivity(launchIntentForPackage);
        pwClose();  
    }

    /**
     * 卸载item程序
     */
    private void unInstall() {
        Intent intent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + appInfo.getPackageName()));
        this.startActivityForResult(intent, UNINSTALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNINSTALL) {
            if (resultCode == Activity.RESULT_CANCELED) {
                new Thread() {
                    @Override
                    public void run() {
                        /**
                         * 重新拿到所有的APP在分包
                         * 因为adapter里的和现在分包的是同一个对象 指向同一个地址
                         * 所以这里重新分包 adapter里的也重新分包了
                         * 直接更新适配器就好
                         */
                        mAllApps = AppInfos.getAllApps(AppManagerActivity.this);
                        systemApp.clear();
                        userApp.clear();
                        for (AppInfo info : mAllApps) {
                            if (info.isSystem()) {
                                systemApp.add(info);
                            } else {
                                userApp.add(info);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }.start();
                pwClose();
            }
        }

    }

    /**
     * 回调
     */

    public void pwClose() {
        lastPosition = -1;
        if (mPw != null) {
            mPw.dismiss();
            mPw = null;
        }
    }

    /**
     * 退出时 销毁pw  防止漏气
     */
    @Override
    protected void onDestroy() {
//        pwClose();
        super.onDestroy();
    }
}
