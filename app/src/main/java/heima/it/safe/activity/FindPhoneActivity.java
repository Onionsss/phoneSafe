package heima.it.safe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

import heima.it.safe.R;
import heima.it.safe.adapter.FindPhoneAdapter;
import heima.it.safe.bean.FindPhoneGroup;
import heima.it.safe.dao.FindPhoneDao;

public class FindPhoneActivity extends AppCompatActivity {
    private List<FindPhoneGroup> list;
    private ExpandableListView mFindphone_elv;
    private int lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_phone);
        initView();
        initData();
        initAdapter();
        initListener();
    }

    private void initListener() {
        mFindphone_elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if(lastPosition != groupPosition){
                    mFindphone_elv.collapseGroup(lastPosition);
                }

                if(mFindphone_elv.isGroupExpanded(groupPosition)){
                    /**
                     * 折叠
                     */
                    mFindphone_elv.collapseGroup(groupPosition);
                }else{
                    /**
                     * 打开
                     */
                    mFindphone_elv.expandGroup(groupPosition);
                    /**
                     * 设置到当前最顶层
                     */
                    mFindphone_elv.setSelectedGroup(groupPosition);
                }
                lastPosition = groupPosition;
                return true;
            }
        });
    }

    private void initData() {
        list = FindPhoneDao.findAll(this);
    }

    private void initAdapter() {
        mFindphone_elv.setAdapter(new FindPhoneAdapter(this,list));
    }

    private void initView() {
        mFindphone_elv = (ExpandableListView) findViewById(R.id.findphone_elv);
    }
}
