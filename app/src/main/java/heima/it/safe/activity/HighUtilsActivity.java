package heima.it.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import heima.it.safe.R;
import heima.it.safe.view.MySettingView;

public class HighUtilsActivity extends AppCompatActivity implements View.OnClickListener {

    private MySettingView mHighutils_address;
    private MySettingView mHighutils_findphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_utils);
        initView();
        initListener();
    }

    private void initView() {
        mHighutils_address = (MySettingView) findViewById(R.id.highutils_address);
        mHighutils_findphone = (MySettingView) findViewById(R.id.highutils_findphone);
    }

    private void initListener() {
        mHighutils_address.setOnClickListener(this);
        mHighutils_findphone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.highutils_address:
                startActivity(new Intent(this,AddressActivity.class));
            break;
        }
    }
}
