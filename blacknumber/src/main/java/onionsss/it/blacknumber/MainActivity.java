package onionsss.it.blacknumber;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_REQUEST = 101;
    public static final int  UPDATE_REQUEST = 102;
    public static final String ACTION_ADD = "action_add";
    public static final String ACTION_UPDATE = "action_update";
    public static final String POSITION = "position";

    private ImageView mBlacknumber_iv;
    private ObjectAnimator mMAnimator;
    private ListView mBlacknumber_listview;
    private List<BlackNumber> list;
    private DBHelpDao mHelp;
    private ImageView mIv_add;
    private MyBlackNumberAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        test();
//        initAnim();
        initData();
        initListener();
    }

    private void initListener() {
        mIv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.setAction(ACTION_ADD);
                startActivityForResult(intent,ADD_REQUEST);
            }
        });

        mBlacknumber_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra(Main2Activity.INFO,list.get(position));
                intent.putExtra(POSITION,position);
                intent.setAction(ACTION_UPDATE);
                startActivityForResult(intent,UPDATE_REQUEST);
            }
        });

        mBlacknumber_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                        mBlacknumber_listview.getLastVisiblePosition() == list.size()-1){
                    mBlacknumber_iv.setVisibility(View.VISIBLE);
                    initAnim();
                    new Thread(){
                        @Override
                        public void run() {
                            SystemClock.sleep(1200);
                            List<BlackNumber> partList = mHelp.queryPart(10, list.size());
                            if(partList.size() > 0){
                                list.addAll(partList);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopAnim();
                                        mBlacknumber_iv.setVisibility(View.GONE);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopAnim();
                                        mBlacknumber_iv.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this,"没有更多的数据", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }.start();
                }else{

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void initData() {
        new Mytask().execute();
    }


    private void initAnim() {
        mMAnimator = ObjectAnimator.ofFloat(mBlacknumber_iv, "rotation", 0, 45, 90, 135, 180, 225, 270, 315, 359);
        mMAnimator.setDuration(1000);
        mMAnimator.setRepeatMode(ValueAnimator.RESTART);
        mMAnimator.setRepeatCount(1000);
        mMAnimator.start();
    }

    private void stopAnim(){
        mMAnimator.cancel();
    }

    private void initView() {
        mBlacknumber_iv = (ImageView) findViewById(R.id.blacknumber_iv);
        mBlacknumber_listview = (ListView) findViewById(R.id.blacknumber_listview);
        mIv_add = (ImageView) findViewById(R.id.iv_add);
    }

    class Mytask extends AsyncTask<Void,Void,List<BlackNumber>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initAnim();
        }

        @Override
        protected List<BlackNumber> doInBackground(Void... params) {
            SystemClock.sleep(2000);
            mHelp = new DBHelpDao(MainActivity.this);
            list = mHelp.queryPart(10,0);
            return list;
        }

        @Override
        protected void onPostExecute(List<BlackNumber> blackNumbers) {
            super.onPostExecute(blackNumbers);
            mAdapter = new MyBlackNumberAdapter(blackNumbers, MainActivity.this);
            mBlacknumber_listview.setAdapter(mAdapter);
            mBlacknumber_iv.setVisibility(View.GONE);
            stopAnim();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_REQUEST){
            if(resultCode == Main2Activity.RESULT){
                BlackNumber bn = (BlackNumber) data.getSerializableExtra(Main2Activity.INFO);
                list.add(bn);
                mAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == UPDATE_REQUEST){
            if(resultCode == Main2Activity.RESULT){
                BlackNumber bn = (BlackNumber) data.getSerializableExtra(Main2Activity.INFO);
                int position = data.getIntExtra(POSITION, -1);
                BlackNumber bnTwo = list.get(position);
                bnTwo.setMode(bn.getMode());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void test() {
        new Thread(){
            @Override
            public void run() {
                DBHelpDao help = new DBHelpDao(MainActivity.this);
                for (int i = 0; i <= 50; i++) {
                    help.insert(new BlackNumber("zhangqi"+i,new Random().nextInt(3)+1+"","1327066570"+i));
                }
            }
        }.start();
    }

}
