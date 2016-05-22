package heima.it.safe.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima.it.safe.R;
import heima.it.safe.adapter.ContactAdapter;
import heima.it.safe.bean.ContactInfo;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.ContactUtil;

public class ContactActivity extends AppCompatActivity {
    @Bind(R.id.contact_lv_find)
    ListView contact_lv_find;
    @Bind(R.id.contact_pro)
    ProgressBar contact_pro;
    private List<ContactInfo> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        initData();
        initListener();
    }


    private void initListener() {
        contact_lv_find.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent();
                    setResult(Constant.FIND_CONTACT_RESULT, intent);
                    finish();
                } else {
                    ContactInfo info = list.get(position - 1);
                    Intent intent = new Intent();
                    intent.putExtra("address", info.getAddress());
                    setResult(Constant.FIND_CONTACT_RESULT, intent);
                    finish();
                }
            }
        });
    }

    /**
     * LIstView设置适配器
     */
    private void initView() {
        contact_lv_find.setAdapter(new ContactAdapter(this, list));
    }

    /**
     * 拿到所有的联系人数据
     */
    private void initData() {
        new LoadData().execute();
    }

    class LoadData extends AsyncTask<Void, Void, List<ContactInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contact_pro.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<ContactInfo> doInBackground(Void... params) {
            SystemClock.sleep(2000);
            list = ContactUtil.getAllContacts(getApplicationContext());
            return list;
        }

        @Override
        protected void onPostExecute(List<ContactInfo> contactInfos) {
            contact_lv_find.setAdapter(new ContactAdapter(getApplicationContext(), contactInfos));
            super.onPostExecute(contactInfos);
            contact_pro.setVisibility(View.GONE);
            contact_lv_find.setVisibility(View.VISIBLE);
        }
    }
}
