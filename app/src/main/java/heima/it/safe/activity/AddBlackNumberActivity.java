package heima.it.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import heima.it.safe.R;
import heima.it.safe.bean.BlackNumber;
import heima.it.safe.dao.BlackNumberDaoImpl;

/**
 * 默认是短信+电话拦截
 */
public class AddBlackNumberActivity extends AppCompatActivity {

    public static final int RESULTCODE = 103;
    public static final String RESULT = "new";
    private int mPosition;

    private EditText add_black_edt_phone;
    private EditText add_black_edt_name;
    private RadioGroup add_black_group;
    private Button add_black_btnok;
    private Button add_black_btnno;
    private TextView add_black_title;

    private String lastChecked = "3";
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_black_number);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mAction = intent.getAction();
        if(BlackNumberActivity.ACTION_UPDATE.equals(mAction)){
            /**
             *  代表更新界面
             */
            add_black_title.setText("更新黑名单");
            add_black_edt_phone.setEnabled(false);
            add_black_edt_name.setEnabled(false);
            add_black_btnok.setText("更新");
            mPosition = intent.getIntExtra(BlackNumberActivity.POSITION,-1);
            BlackNumber info = (BlackNumber) intent.getSerializableExtra(BlackNumberActivity.INFO);
            add_black_edt_phone.setText(info.getPhone());
            add_black_edt_name.setText(info.getName());
            String mode = info.getMode();
            switch(mode){
                case "3":
                    add_black_group.check(R.id.add_black_rb3);
                break;
                case "2":
                    add_black_group.check(R.id.add_black_rb2);
                    break;
                case "1":
                    add_black_group.check(R.id.add_black_rb1);
                    break;
            }
        }
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
                    if(BlackNumberActivity.ACTION_UPDATE.equals(mAction)){
                        updateBlackNumber(name,phone);
                    }else{
                        saveBalckNumber(name,phone);
                    }
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
     * 更新黑名单
     * @param name
     * @param phone
     */
    private void updateBlackNumber(String name, String phone) {
        BlackNumberDaoImpl bndi = new BlackNumberDaoImpl(this);
        BlackNumber blackNumber = new BlackNumber(name, phone, lastChecked);
        boolean update = bndi.update(blackNumber);
        if(update){
            Toast.makeText(this,"更新成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(RESULT,blackNumber);
            intent.putExtra(BlackNumberActivity.POSITION,mPosition);
            setResult(RESULTCODE,intent);
            finish();
        }else{
            Toast.makeText(this,"更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存黑名单
     */
    private void saveBalckNumber(String name,String phone) {
        BlackNumberDaoImpl bndi = new BlackNumberDaoImpl(this);
        BlackNumber blackNumber = new BlackNumber(name, phone, lastChecked);
        boolean insert = bndi.insert(blackNumber);
        if(insert){
            Toast.makeText(this,"插入成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(RESULT,blackNumber);
            setResult(RESULTCODE,intent);
            finish();
        }else{
            Toast.makeText(this,"插入失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        add_black_edt_phone = (EditText) findViewById(R.id.add_black_edt_phone);
        add_black_edt_name = (EditText) findViewById(R.id.add_black_edt_name);
        add_black_group = (RadioGroup) findViewById(R.id.add_black_group);
        add_black_btnok = (Button) findViewById(R.id.add_black_btnok);
        add_black_btnno = (Button) findViewById(R.id.add_black_btnno);
        add_black_title = (TextView) findViewById(R.id.add_black_title);
    }
}
