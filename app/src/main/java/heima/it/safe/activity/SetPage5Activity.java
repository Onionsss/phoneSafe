package heima.it.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

public class SetPage5Activity extends BaseActivity {

    @Bind(R.id.setpage5_cb_project)
    CheckBox setpage5_cb_project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page5);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        boolean flag = SpUtil.getBoolean(this, Constant.PROJECT, false);
        if(flag) {
            setpage5_cb_project.setChecked(true);
        }else{
            setpage5_cb_project.setChecked(false);
        }
    }

    @OnClick(R.id.setpage5_cb_project)
    public void onClick() {
        boolean checked = setpage5_cb_project.isChecked();
        if(checked){
            SpUtil.putBoolean(this,Constant.PROJECT,true);
            Log.d("TAG", "true");
        }else{
            SpUtil.putBoolean(this,Constant.PROJECT,false);
        }
    }

    @Override
    public void next() {
        boolean flag = SpUtil.getBoolean(this, Constant.PROJECT, false);
        if (flag) {
            super.next();
        } else {
            Toast.makeText(this, "请开启防盗保护", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Class<? extends Activity> getNextClass() {
        return SetActivity.class;
    }

    @Override
    public Class<? extends Activity> getReturnClass() {
        return SetPage4Activity.class;
    }
}
