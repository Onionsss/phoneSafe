package heima.it.safe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import heima.it.safe.R;

/**
 * 作者：张琦 on 2016/5/29 18:53
 */
public class MyProgressView extends LinearLayout{

    private TextView mMyprogress_tv_where;
    private TextView mMyprogress_tv_use;
    private TextView mMyprogress_tv_have;
    private ProgressBar mMyprogress_pb_use;

    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.myprogressbar, this);
        mMyprogress_tv_where = (TextView) view.findViewById(R.id.myprogress_tv_where);
        mMyprogress_tv_use = (TextView) view.findViewById(R.id.myprogress_tv_use);
        mMyprogress_tv_have = (TextView) view.findViewById(R.id.myprogress_tv_have);
        mMyprogress_pb_use = (ProgressBar) view.findViewById(R.id.myprogress_pb_use);
    }

    public MyProgressView(Context context) {
        super(context);

    }

    public void setWhere(String where){
        mMyprogress_tv_where.setText(where);
    }
    public void setUse(String use){
        mMyprogress_tv_use.setText(use+"已用");
    }
    public void setHave(String have){
        mMyprogress_tv_have.setText(have+"可用");
    }
    public void setProgress(int progress){
        mMyprogress_pb_use.setProgress(progress);
    }
}
