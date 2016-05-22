package heima.it.safe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;
import heima.it.safe.view.MySettingView;

public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.mysetting_aotu)
    MySettingView mysetting_aotu;
    @Bind(R.id.mysetting_lanjie)
    MySettingView mysetting_lanjie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
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
