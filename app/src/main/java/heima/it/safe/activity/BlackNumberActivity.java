package heima.it.safe.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import heima.it.safe.R;
import heima.it.safe.adapter.BlackNumberAdapter;
import heima.it.safe.bean.BlackNumber;
import heima.it.safe.dao.BlackNumberDaoImpl;

public class BlackNumberActivity extends AppCompatActivity {
    public static final int CODE_UPDATE = 105;
    private static final int RESPONSECODE = 104;
    private static final String TAG = "BlackNumberActivity";
    public static final String ACTION_UPDATE = "update";
    /**
     * 代表传过去blackNunber对象
     */
    public static final String INFO = "info";
    public static final String POSITION = "position";

    private ListView blacknumber_listview;
    private ImageView mBlacknumber_iv;
    private ImageView blacknumber_iv_add;
    List<BlackNumber> list;
    private ObjectAnimator mAnimator;
    private BlackNumberAdapter mAdapter;
    private BlackNumberDaoImpl mBndi;


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
                startActivityForResult(new Intent(BlackNumberActivity.this, AddBlackNumberActivity.class), RESPONSECODE);
            }
        });
    }

    private void listViewItemListener() {
        blacknumber_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BlackNumberActivity.this, AddBlackNumberActivity.class);
                intent.setAction(ACTION_UPDATE);
                intent.putExtra(INFO, list.get(position));
                intent.putExtra(POSITION, position);
                startActivityForResult(intent, CODE_UPDATE);

            }
        });
        /**
         * ListView滚动监听
         * 滚动到最后一条item时加载数据
         */
        blacknumber_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                        blacknumber_listview.getLastVisiblePosition() == list.size() - 1) {
                    mBlacknumber_iv.setVisibility(View.VISIBLE);
                    initAnim();
                    new Thread() {
                        @Override
                        public void run() {
                            SystemClock.sleep(1000);
                            List<BlackNumber> partList = mBndi.findPart(10, list.size());
                            if (partList.size() > 0) {
                                list.addAll(partList);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopAnim();
                                        mBlacknumber_iv.setVisibility(View.GONE);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            } else {
                                //TODO没查到
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        mBlacknumber_iv.setVisibility(View.GONE);
                                        // 隐藏ProgressBar
                                        Toast.makeText(getApplicationContext(),
                                                "没有更多数据了", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }.start();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 开启子线程执行查询的任务
     */
    private void initData() {
        new BlackNumberTask().execute();
    }

    /**
     * 初始化旋转动画
     */
    private void initAnim() {
        mAnimator = ObjectAnimator.ofFloat(mBlacknumber_iv, "rotation", 0, 45, 90, 135, 180, 225, 270, 315, 360);
        mAnimator.setDuration(1000);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(1000);
        mAnimator.start();
    }

    /**
     * 加载数据完成 动画结束
     */
    private void stopAnim() {
        mAnimator.cancel();
    }


    private void initView() {
        mBlacknumber_iv = (ImageView) findViewById(R.id.blacknumber_iv);
        blacknumber_listview = (ListView) findViewById(R.id.blacknumber_listview);
        blacknumber_iv_add = (ImageView) findViewById(R.id.blacknumber_iv_add);

        ImageView emptyImage = new ImageView(this);
        emptyImage.setImageResource(R.mipmap.empty);
        blacknumber_listview.setEmptyView(emptyImage);
    }


    class BlackNumberTask extends AsyncTask<Void, Void, List<BlackNumber>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initAnim();
        }

        @Override
        protected List<BlackNumber> doInBackground(Void... params) {
            SystemClock.sleep(2000);
            mBndi = new BlackNumberDaoImpl(BlackNumberActivity.this);
            /**
             * 分页查找 一次10条数据
             */
            list = mBndi.findPart(10, 0);
            return list;
        }

        @Override
        protected void onPostExecute(List<BlackNumber> blackNumbers) {
            super.onPostExecute(blackNumbers);
            mAdapter = new BlackNumberAdapter(BlackNumberActivity.this, blackNumbers);
            blacknumber_listview.setAdapter(mAdapter);
            stopAnim();
            mBlacknumber_iv.setVisibility(View.GONE);
            /**
             * listView的条目点击
             */
            listViewItemListener();
        }
    }

    /**
     * 返回时自动刷新数据
     * 通过requestCode 来判断是更新返回还是添加返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESPONSECODE) {
            if (resultCode == AddBlackNumberActivity.RESULTCODE) {
                BlackNumber blacknumber = (BlackNumber) data.getSerializableExtra(AddBlackNumberActivity.RESULT);
                list.add(blacknumber);
                mAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == CODE_UPDATE) {
            if (resultCode == AddBlackNumberActivity.RESULTCODE) {
                int position = data.getIntExtra(POSITION, -1);
                BlackNumber info = (BlackNumber) data.getSerializableExtra(AddBlackNumberActivity.RESULT);
                BlackNumber blackNumber = list.get(position);
                Log.d(TAG, "onActivityResult: " + info.getMode());
                blackNumber.setMode(info.getMode());

                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
