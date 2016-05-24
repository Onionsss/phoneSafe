package heima.it.safe.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import heima.it.safe.R;
import heima.it.safe.adapter.BlackNumberAdapter;
import heima.it.safe.bean.BlackNumber;
import heima.it.safe.dao.BlackNumberDaoImpl;

public class BlackNumberActivity extends AppCompatActivity {
    private static final String TAG = "BlackNumberActivity";
    private ListView blacknumber_listview;
    private ImageView mBlacknumber_iv;
    private ImageView blacknumber_iv_add;
    List<BlackNumber> list;
    private ObjectAnimator mAnimator;
    private BlackNumberAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        initView();
//        initAnim();
        initData();
        initListener();
    }

    private void initListener() {
        blacknumber_iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BlackNumberActivity.this,AddBlackNumberActivity.class));
            }
        });

    }

    private void initData() {
        new BlackNumberTask().execute();
    }

    /**
     * 初始化旋转动画
     */
    private void initAnim() {
        mAnimator = ObjectAnimator.ofFloat(mBlacknumber_iv, "rotation", 0,45,90,135,180,225,270,315,359);
        mAnimator.setDuration(1000);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(1000);
        mAnimator.start();
    }

    private void stopAnim() {
        mAnimator.cancel();
    }

    /**
     * 加载数据完成 动画结束
     */
    private void initView() {
        mBlacknumber_iv = (ImageView) findViewById(R.id.blacknumber_iv);
        blacknumber_listview = (ListView) findViewById(R.id.blacknumber_listview);
        blacknumber_iv_add = (ImageView) findViewById(R.id.blacknumber_iv_add);

    }


    class BlackNumberTask extends AsyncTask<Void,Void,List<BlackNumber>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initAnim();
        }

        @Override
        protected List<BlackNumber> doInBackground(Void... params) {
            SystemClock.sleep(2000);
            BlackNumberDaoImpl bndi = new BlackNumberDaoImpl(BlackNumberActivity.this);
            list = bndi.findAll();
            return list;
        }

        @Override
        protected void onPostExecute(List<BlackNumber> blackNumbers) {
            super.onPostExecute(blackNumbers);
            mAdapter = new BlackNumberAdapter(BlackNumberActivity.this, blackNumbers);
            blacknumber_listview.setAdapter(mAdapter);
            stopAnim();
            mBlacknumber_iv.setVisibility(View.GONE);
            blacknumber_listview.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 返回时自动刷新数据
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyDataSetChanged();
    }
}
