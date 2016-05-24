package heima.it.safe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import heima.it.safe.R;
import heima.it.safe.bean.BlackNumber;
import heima.it.safe.dao.BlackNumberDaoImpl;

/**
 * 默认是短信+电话拦截
 */
public class AddBlackNumberActivity extends AppCompatActivity {
    private EditText add_black_edt_phone;
    private EditText add_black_edt_name;
    private RadioGroup add_black_group;
    private Button add_black_btnok;
    private Button add_black_btnno;

    private String lastChecked = "3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_black_number);
        initView();
        initListener();
    }

    private void initListener() {
        add_black_btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_black_btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = add_black_edt_phone.getText().toString().trim();
                String name = add_black_edt_name.getText().toString().trim();
                if(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(name)){
                    saveBalckNumber(name,phone);
                }else{
                    Toast.makeText(AddBlackNumberActivity.this,"黑名单不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        add_black_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.add_black_rb1:
                        lastChecked = "1";
                    break;
                    case R.id.add_black_rb2:
                        lastChecked = "2";
                        break;
                    case R.id.add_black_rb3:
                        lastChecked = "3";
                        break;
                }
            }
        });
    }

    /**
     * 保存黑名单
     */
    private void saveBalckNumber(String name,String phone) {
        BlackNumberDaoImpl bndi = new BlackNumberDaoImpl(this);
        boolean insert = bndi.insert(new BlackNumber(name, phone, lastChecked));
        if(insert){
            Toast.makeText(this,"插入成功", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"插入失败", Toast.LENGTH_SHORT).show();
        }
    }


    private void initView() {
        add_black_edt_phone = (EditText) findViewById(R.id.add_black_edt_phone);
        add_black_edt_name = (EditText) findViewById(R.id.add_black_edt_phone);
        add_black_group = (RadioGroup) findViewById(R.id.add_black_group);
        add_black_btnok = (Button) findViewById(R.id.add_black_btnok);
        add_black_btnno = (Button) findViewById(R.id.add_black_btnno);
    }
}
