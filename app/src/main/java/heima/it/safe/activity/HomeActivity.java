package heima.it.safe.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima.it.safe.R;
import heima.it.safe.adapter.HomeAdapter;
import heima.it.safe.bean.HomeItem;
import heima.it.safe.constant.Constant;
import heima.it.safe.utils.SpUtil;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.home_iv_logo)
    ImageView home_iv_logo;
    @Bind(R.id.home_gv)
    GridView home_gv;
    @Bind(R.id.home_iv_setting)
    ImageView home_iv_setting;

    private final static String[] TITLES = new String[]{"手机防盗", "骚扰拦截",
            "软件管家", "进程管理", "流量统计", "手机杀毒", "缓存清理", "常用工具"};
    private final static String[] DESCS = new String[]{"远程定位手机", "全面拦截骚扰",
            "管理您的软件", "管理运行进程", "流量一目了然", "病毒无处藏身", "系统快如火箭", "工具大全"};

    private final static int[] ICONS = new int[]{R.mipmap.sjfd,
            R.mipmap.srlj, R.mipmap.rjgj, R.mipmap.jcgl, R.mipmap.lltj,
            R.mipmap.sjsd, R.mipmap.hcql, R.mipmap.cygj};
    private List<HomeItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    private void initListener() {
        home_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        checkPassword();
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this,BlackNumberActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this,HighUtilsActivity.class));
                        break;
                }
            }
        });
    }

    /**
     * 校验密码
     */
    private void checkPassword() {
        if (SpUtil.getString(this, Constant.CHECKPASSWORD, "").equals("")) {
            /**
             * 没有设置
             */
            setPassword();
        } else {
            /**
             * 设置完成
             */
            loginPassword();
        }
    }

    private void loginPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog dialog = alert.create();
        View view = View.inflate(this, R.layout.dialog_checkpassword, null);
        final EditText safe_edt_checkmm = (EditText) view.findViewById(R.id.safe_edt_checkmm);
        TextView safe_tv_checkno = (TextView) view.findViewById(R.id.safe_tv_checkno);
        TextView safe_tv_checkok = (TextView) view.findViewById(R.id.safe_tv_checkok);
        safe_tv_checkno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        safe_tv_checkok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = safe_edt_checkmm.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(HomeActivity.this,"密码不能为空!", Toast.LENGTH_SHORT).show();
                }
                if(pwd.equals(SpUtil.getString(HomeActivity.this,Constant.CHECKPASSWORD,""))){
                    if(SpUtil.getBoolean(HomeActivity.this,Constant.SAFE_SET_OK,false)) {
                        startActivity(new Intent(HomeActivity.this, SetActivity.class));
                    }else{
                        startActivity(new Intent(HomeActivity.this, SetPage1Activity.class));
                    }
                    dialog.dismiss();
                }
            }
        });

        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void setPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog dialog = alert.create();
        View view = View.inflate(this, R.layout.dialog_setpassword, null);
        /**
         * 拿到子控件
         */
        final EditText safe_edt_mm = (EditText) view.findViewById(R.id.safe_edt_mm);
        final EditText safe_edt_mmagain = (EditText) view.findViewById(R.id.safe_edt_mmagain);
        TextView safe_tv_no = (TextView) view.findViewById(R.id.safe_tv_no);
        TextView safe_tv_ok = (TextView) view.findViewById(R.id.safe_tv_ok);

        safe_tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        safe_tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 检查用户输入的密码
                 */
                String pwd = safe_edt_mm.getText().toString().trim();
                String pwdagain = safe_edt_mmagain.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(HomeActivity.this,"密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pwd.equals(pwdagain)){
                    Toast.makeText(HomeActivity.this,"两次输入密码不一致!", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * 代表输入的密码一致
                 */
                SpUtil.putString(HomeActivity.this,Constant.CHECKPASSWORD,pwd);
                Toast.makeText(HomeActivity.this,"设置成功!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();
    }




    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < ICONS.length; i++) {
            list.add(new HomeItem(TITLES[i], ICONS[i], DESCS[i]));
        }
    }

    private void initView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(home_iv_logo, "rotationY", 45, 90, 135, 270, 360);
        animator.setDuration(3000);//时间
        animator.setRepeatCount(ObjectAnimator.INFINITE);//无限执行
        animator.setRepeatMode(ObjectAnimator.REVERSE);//反复
        animator.start();

        home_gv.setAdapter(new HomeAdapter(this, list));
    }

    @OnClick(R.id.home_iv_setting)
    public void click(View view) {
        switch (view.getId()) {
            case R.id.home_iv_setting:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;
        }
    }
}
