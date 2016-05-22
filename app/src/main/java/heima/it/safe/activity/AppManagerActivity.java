package heima.it.safe.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima.it.safe.R;
import heima.it.safe.adapter.AppManagerAdapter;
import heima.it.safe.bean.AppInfo;
import heima.it.safe.utils.AppInfos;

public class AppManagerActivity extends AppCompatActivity {
    @Bind(R.id.app_lv)
    ListView app_lv;
    private List<AppInfo> appList;
    private List<AppInfo> systemApp = new ArrayList<>();
    private List<AppInfo> userApp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        new Task().execute();
    }

    class Task extends AsyncTask<Void, Void, List<AppInfo>> {

        @Override
        protected List<AppInfo> doInBackground(Void... params) {
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
            app_lv.setAdapter(new AppManagerAdapter(AppManagerActivity.this,appList,systemApp,userApp));
        }
    }
}
