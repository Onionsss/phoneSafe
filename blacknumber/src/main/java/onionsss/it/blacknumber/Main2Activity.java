package onionsss.it.blacknumber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    public static final int RESULT = 105;
    public static final String INFO = "info";
    private EditText mAdd_black_edt_name;
    private EditText mAdd_black_edt_phone;
    private Button mAdd_black_btnno;
    private Button mAdd_black_btnok;
    private RadioGroup mAdd_black_group;
    private TextView mAdd_black_title;
    private int lastChoose = 3;
    private DBHelpDao mHelp;
    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_black_number);
        initView();
        initListener();
    }

    private void initListener() {
        mAdd_black_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.add_black_rb1:
                        lastChoose = 1;
                        break;
                    case R.id.add_black_rb2:
                        lastChoose = 2;
                        break;
                    case R.id.add_black_rb3:
                        lastChoose = 3;
                        break;
                }
            }
        });
        mAdd_black_btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdd_black_btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mAdd_black_edt_name.getText().toString().trim();
                String phone = mAdd_black_edt_phone.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                    Intent intent = Main2Activity.this.getIntent();
                    String action = intent.getAction();
                    if (action.equals(MainActivity.ACTION_ADD)) {
                        saveBlackNUmber(name, phone);
                    } else if (action.equals(MainActivity.ACTION_UPDATE)) {
                        mPosition = intent.getIntExtra(MainActivity.POSITION, -1);
                        updateBlackNumber(name, phone);
                    }
                } else {
                    Toast.makeText(Main2Activity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateBlackNumber(String name, String phone) {
        BlackNumber bn = new BlackNumber(name, lastChoose + "", phone);
        mHelp = new DBHelpDao(this);
        boolean insert = mHelp.update(bn);
        if (insert) {
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(INFO, bn);
            intent.putExtra(MainActivity.POSITION, mPosition);
            setResult(RESULT, intent);
            Log.d("TAG", "updateBlackNumber: " + bn.getMode());
            finish();
        } else {
            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBlackNUmber(String name, String phone) {
        BlackNumber bn = new BlackNumber(name, lastChoose + "", phone);
        mHelp = new DBHelpDao(this);
        boolean insert = mHelp.insert(bn);
        if (insert) {
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(MainActivity.POSITION, mPosition);
            intent.putExtra(INFO, bn);
            setResult(RESULT, intent);
            finish();
        } else {
            Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mAdd_black_edt_name = (EditText) findViewById(R.id.add_black_edt_name);
        mAdd_black_edt_phone = (EditText) findViewById(R.id.add_black_edt_phone);
        mAdd_black_btnno = (Button) findViewById(R.id.add_black_btnno);
        mAdd_black_btnok = (Button) findViewById(R.id.add_black_btnok);
        mAdd_black_group = (RadioGroup) findViewById(R.id.add_black_group);
        mAdd_black_title = (TextView) findViewById(R.id.add_black_title);

        update();
    }

    /**
     * 检查是否是update状态
     */
    private void update() {
        Intent intent = getIntent();
        String action = intent.getAction();
        BlackNumber bn = (BlackNumber) intent.getSerializableExtra(INFO);
        if (action.equals(MainActivity.ACTION_UPDATE)) {
            mAdd_black_title.setText("更新黑名单");
            mAdd_black_btnok.setText("更新");
            mAdd_black_edt_name.setEnabled(false);
            mAdd_black_edt_phone.setEnabled(false);
            mAdd_black_edt_name.setText(bn.getName());
            mAdd_black_edt_phone.setText(bn.getPhone());

            switch (bn.getMode()) {
                case "1":
                    mAdd_black_group.check(R.id.add_black_rb1);
                    break;
                case "2":
                    mAdd_black_group.check(R.id.add_black_rb2);
                    break;
                case "3":
                    mAdd_black_group.check(R.id.add_black_rb3);
                    break;
            }
        }
    }
}
