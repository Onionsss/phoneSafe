package heima.it.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

public class SetPage4Activity extends BaseActivity {


    private static final int REQUEST_CODE_ENABLE_ADMIN = 102;
    @Bind(R.id.setpage4_iv_dun)
    ImageView setpage4_iv_dun;
    @Bind(R.id.setpage4_relative)
    RelativeLayout setpage4_relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page4);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick(R.id.setpage4_relative)
    public void onClick() {
        boolean flag = SpUtil.getBoolean(this, Constant.ADMIN, false);
        if (flag) {
            setpage4_iv_dun.setImageResource(R.mipmap.admin_inactivated);
            SpUtil.putBoolean(this, Constant.ADMIN, false);
        } else {
            setpage4_iv_dun.setImageResource(R.mipmap.admin_activated);
            SpUtil.putBoolean(this, Constant.ADMIN, true);
//            startAdmin();
        }
    }

    /**
     * 激活设备管理器
     */
    private void startAdmin() {
//        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
//        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//                mActivity.getString(R.string.add_admin_extra_app_text));
//        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    private void initView() {
        Boolean flag = SpUtil.getBoolean(this, Constant.ADMIN, false);
        if (flag) {
            setpage4_iv_dun.setImageResource(R.mipmap.admin_activated);
        } else {
            setpage4_iv_dun.setImageResource(R.mipmap.admin_inactivated);
        }
    }

    @Override
    public void next() {
        boolean flag = SpUtil.getBoolean(this, Constant.ADMIN, false);
        if (!flag) {
            Toast.makeText(this, "请激活设备管理器", Toast.LENGTH_SHORT).show();
        } else {
            super.next();
        }
    }

    @Override
    public Class<? extends Activity> getNextClass() {
        return SetPage5Activity.class;
    }

    @Override
    public Class<? extends Activity> getReturnClass() {
        return SetPage3Activity.class;
    }


}
