package heima.it.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima.it.safe.R;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

public class SetPage3Activity extends BaseActivity {
    @Bind(R.id.setpage3_relative)
    RelativeLayout setpage3_relative;
    private EditText setpage3_edt_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page3);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        setpage3_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SetPage3Activity.this,ContactActivity.class), Constant.FIND_CONTACT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 拿到选择的电话号码
         */
        if(requestCode == Constant.FIND_CONTACT && resultCode == Constant.FIND_CONTACT_RESULT){
            setpage3_edt_phone.setText(data.getStringExtra("address"));
        }
    }

    /**
     * 点击加载联系人
     */
    @OnClick(R.id.setpage3_relative)
    public void onClick() {
    }
    private void initView() {
        setpage3_edt_phone = (EditText) findViewById(R.id.setpage3_edt_phone);
        String safePhone = SpUtil.getString(this, Constant.SAFE_PHONE, "");
        if ("".equals(safePhone)) {
        } else {
            setpage3_edt_phone.setText(safePhone);
        }
    }

    @Override
    public void next() {
        String phone = setpage3_edt_phone.getText().toString().trim();

        if ("".equals(phone)) {
            Toast.makeText(this, "请选择安全号码!", Toast.LENGTH_SHORT).show();
        } else {
            SpUtil.putString(this, Constant.SAFE_PHONE, phone);
            super.next();
        }
    }

    @Override
    public Class<? extends Activity> getNextClass() {
        return SetPage4Activity.class;
    }

    @Override
    public Class<? extends Activity> getReturnClass() {
        return SetPage2Activity.class;
    }


}
