package heima.it.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

public class SetActivity extends AppCompatActivity {

    @Bind(R.id.safe_set_phone)
    TextView safe_set_phone;
    @Bind(R.id.safe_set_lock)
    ImageView safe_set_lock;
    @Bind(R.id.safe_set_ll_project)
    LinearLayout safe_set_ll_project;
    @Bind(R.id.safe_set_ll_again)
    LinearLayout safe_set_ll_again;
    @Bind(R.id.safe_set_exit)
    Button safe_set_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 设置安全号码和锁
     */
    private void initView() {
        safe_set_phone.setText(SpUtil.getString(this, Constant.SAFE_PHONE, ""));
        boolean project = SpUtil.getBoolean(this, Constant.PROJECT, false);
        if (project) {
            safe_set_lock.setImageResource(R.mipmap.lock);
        } else {
            safe_set_lock.setImageResource(R.mipmap.unlock);
        }

    }

    @OnClick({R.id.safe_set_ll_project, R.id.safe_set_ll_again,R.id.safe_set_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.safe_set_ll_project:
                changeLock();
                break;
            case R.id.safe_set_ll_again:
                /**
                 * 重新进入设置向导
                 */
                startActivity(new Intent(this, SetPage1Activity.class));
                finish();
                break;
            case R.id.safe_set_exit:
                /**
                 * 退出手机防盗 设置完成
                 * 所以下次不会在设置
                 */
                SpUtil.putBoolean(this,Constant.SAFE_SET_OK,true);
                finish();
        }
    }

    /**
     * 根据用户点击改变锁的状态
     */
    private void changeLock() {
        boolean project = SpUtil.getBoolean(this, Constant.PROJECT, false);
        if (project) {
            safe_set_lock.setImageResource(R.mipmap.unlock);
            SpUtil.putBoolean(this, Constant.PROJECT, false);
        } else {
            safe_set_lock.setImageResource(R.mipmap.lock);
            SpUtil.putBoolean(this, Constant.PROJECT, true);
        }
    }


}
