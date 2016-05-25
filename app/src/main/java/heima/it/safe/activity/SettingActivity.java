package heima.it.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.service.BlackNumberService;
import heima.it.safe.utils.SpUtil;
import heima.it.safe.view.MySettingView;
import heima.it.safe.view.ServiceRunning;

public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.mysetting_aotu)
    MySettingView mysetting_aotu;


    private MySettingView mysetting_lanjie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    /**
     * 在Resume 检测服务的状态
     * 因为用户可能用home键去关闭服务 并切换进来
     * 所以可能不走create生命周期
     */
    @Override
    protected void onResume() {
        super.onResume();
        boolean isRun = ServiceRunning.isServiceRunning(SettingActivity.this, BlackNumberService.class);
        mysetting_lanjie.setChecked(isRun);
    }

    private void initListener() {
        /**
         * 判断服务是否在开启状态 如果false 说明服务没有运行 就把服务开启
         * 反之
         */
        mysetting_lanjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isRun = ServiceRunning.isServiceRunning(SettingActivity.this, BlackNumberService.class);
                if(!isRun){
                    startService(new Intent(SettingActivity.this,BlackNumberService.class));
                }else{
                    stopService(new Intent(SettingActivity.this,BlackNumberService.class));
                }
                mysetting_lanjie.check();
            }
        });
    }

    private void initView() {
        mysetting_lanjie = (MySettingView) findViewById(R.id.mysetting_lanjie);
        boolean flag = SpUtil.getBoolean(this, Constant.AUTO_UPDATE, true);
        mysetting_aotu.setChecked(flag);
    }

    @OnClick({R.id.mysetting_aotu, R.id.mysetting_lanjie})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mysetting_aotu:
                /**
                 * 记录点击是否自动更新
                 */
                SpUtil.putBoolean(this,Constant.AUTO_UPDATE,!mysetting_aotu.isChecked());
                mysetting_aotu.check();
                break;
            case R.id.mysetting_lanjie:
                break;
        }
    }
}
