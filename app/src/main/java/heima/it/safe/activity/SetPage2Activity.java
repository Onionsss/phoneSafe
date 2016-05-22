package heima.it.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

public class SetPage2Activity extends BaseActivity {

    @Bind(R.id.setpage2_iv_lock)
    ImageView setpage2_iv_lock;
    @Bind(R.id.setpage2_relative)
    RelativeLayout setpage2_relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page2);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String sim = SpUtil.getString(this, Constant.BINDSIM, "");
        if(sim.equals("")){
            setpage2_iv_lock.setImageResource(R.mipmap.unlock);
        }else{
            setpage2_iv_lock.setImageResource(R.mipmap.lock);
        }
    }

    @OnClick(R.id.setpage2_relative)
    public void bind() {
        String sim = SpUtil.getString(this, Constant.BINDSIM, "");
        if(sim.equals("")){
            setpage2_iv_lock.setImageResource(R.mipmap.lock);
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            SpUtil.putString(this,Constant.BINDSIM,tm.getSimSerialNumber());
        }else{
            setpage2_iv_lock.setImageResource(R.mipmap.unlock);
            SpUtil.putString(this,Constant.BINDSIM,"");
        }
    }

    @Override
    public void next() {
        String sim = SpUtil.getString(this, Constant.BINDSIM, "");
        if(sim.equals("")){
            Toast.makeText(this,"sim卡必须绑定", Toast.LENGTH_SHORT).show();
        }else{
            super.next();
        }
    }

    @Override
    public Class<? extends Activity> getNextClass() {
        return SetPage3Activity.class;
    }

    @Override
    public Class<? extends Activity> getReturnClass() {
        return SetPage1Activity.class;
    }
}
