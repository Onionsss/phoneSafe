package heima.it.safe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import heima.it.safe.R;
import heima.it.safe.dao.SqliteDao;

public class AddressActivity extends AppCompatActivity {

    private EditText mAddress_edt;
    private Button mAddress_btn;
    private TextView mAddress_tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
        initListener();
    }

    private void initListener() {
        mAddress_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = mAddress_edt.getText().toString().trim();
                String find = SqliteDao.getAddress(AddressActivity.this, address);
                mAddress_tv_show.setText(find);
            }
        });

        mAddress_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                String address = SqliteDao.getAddress(AddressActivity.this, phone);
                mAddress_tv_show.setText(address);
            }
        });
    }

    private void initView() {
        mAddress_edt = (EditText) findViewById(R.id.address_edt);
        mAddress_btn = (Button) findViewById(R.id.address_btn);
        mAddress_tv_show = (TextView) findViewById(R.id.address_tv_show);
    }
}
