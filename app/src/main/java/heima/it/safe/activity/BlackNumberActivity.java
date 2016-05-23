package heima.it.safe.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
    List<BlackNumber> list;
    private ObjectAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        initView();
//        initAnim();
        initData();
    }

    private void initData() {
        new BlackNumberTask().execute();
    }

    private void initAnim() {
        mAnimator = ObjectAnimator.ofFloat(mBlacknumber_iv, "rotation", 0,45,90,135,180,225,270,315,360);
        mAnimator.setDuration(1000);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(1000);
        mAnimator.start();
    }
    private void stopAnim() {

    }

    private void initView() {
        mBlacknumber_iv = (ImageView) findViewById(R.id.blacknumber_iv);
        blacknumber_listview = (ListView) findViewById(R.id.blacknumber_listview);

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
            blacknumber_listview.setAdapter(new BlackNumberAdapter(BlackNumberActivity.this,blackNumbers));
//            stopAnim();
            mBlacknumber_iv.setVisibility(View.GONE);
            blacknumber_listview.setVisibility(View.VISIBLE);
        }
    }


}
