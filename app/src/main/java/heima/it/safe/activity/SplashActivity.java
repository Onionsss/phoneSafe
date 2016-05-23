package heima.it.safe.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima.it.safe.R;
import heima.it.safe.bean.BlackNumber;
import heima.it.safe.constant.Constant;
import heima.it.safe.dao.BlackNumberDaoImpl;
import heima.it.safe.utils.OkUtils;
import heima.it.safe.utils.PackageUtil;
import heima.it.safe.utils.SpUtil;
import heima.it.safe.utils.StreamJson;
import okhttp3.Response;

/**
 * Splash   App的启动页面
 * 用于加载数据
 * 显示公司log
 * 检查是否有更新
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * 常量
     */
    private static final int RESPON = 0;    //如果连接码不等于200的情况下
    private static final int NET_ERROR = 1; //网络错误的情况下
    private static final int UPDATE = 2;    //需要更新的情况
    private static final int NO_UPDATE = 3; //不需要更新的情况
    private static final int JSON_ERROR = 4; //JSON解析错误
    private static final int URL_ERROR = 5; //URL地址错误
    private static final int PROTOCCOL = 6; //礼仪错误
    private static final int ENTERHOME = 8; //进入主页
    private static final int MY = 7;  //我的错误
    private static final String TAG = "SplashActivity";
    public static final int SPLASH_INSTALL = 7;    //不需要更新
    @Bind(R.id.splash_tv_version)
    TextView splash_tv_version;
    private String mVersionName;
    private int mVersionCode;
    private String mDescription;
    private String mDownloadUrl;

    /**
     * Handler处理子线程发过来的消息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPON:
                    Toast.makeText(SplashActivity.this, "连接码不正确", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case UPDATE:
                    showUpdateDialog();
                    break;
                case NO_UPDATE:
                    Toast.makeText(SplashActivity.this, "不需要更新", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "JSON错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case URL_ERROR:
                    Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case PROTOCCOL:
                    Toast.makeText(SplashActivity.this, "PROTOCCOL错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case ENTERHOME:
                    enterHome();
                    break;
            }
        }
    };
    private ProgressDialog mProgressDialog;

    /**
     * 弹出提示用户安装的提示框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        final AlertDialog ad = ab.create();
        ab.setIcon(R.mipmap.ic_launcher)
                .setTitle("最新版本")
                .setMessage(mDescription)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                        ad.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        down();//TODO  按下确定开始下载
                    }
                })
                .show();
    }

    private void down() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();

            new Thread(new downAPK()).start();
        } else {
            /**
             * 没有挂载好
             */
            Toast.makeText(this, "没有SD卡,无法下载!", Toast.LENGTH_SHORT).show();
        }
    }
    class downAPK implements Runnable{
        @Override
        public void run() {

            InputStream is = null;
            OutputStream os = null;
            try {
                Response response = OkUtils.getResponse(mDownloadUrl);
                mProgressDialog.setMax((int) response.body().contentLength());
                is = response.body().byteStream();
                File rootFile = Environment.getExternalStorageDirectory();
                File file = new File(rootFile, "down.apk");
                os = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int len = 0;
                int totalDown = 0;
                while ((len = is.read(b)) != -1) {
                    os.write(b, 0, len);
                    totalDown+=len;
                    mProgressDialog.setProgress(totalDown);
                }
                os.flush();
                mProgressDialog.dismiss();
                /**
                 * 下载完成的提示
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this,"下载完成,等待安装!", Toast.LENGTH_SHORT).show();
                    }
                });
                installApk(file);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Closed(is);
                Closed(os);
            }
        }
    }
    /**
     * 关流的操作
     * @param close
     */
    private void Closed(Closeable close) {
        if (close != null) {
            try {
                close.close();
                close = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 安装Apk
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri data = Uri.fromFile(file);
        Log.d(TAG, "uri:" + data);
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivityForResult(intent, SPLASH_INSTALL);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test();
        initView();
    }

    /**
     * 测试数据库
     */
    private void test() {
        BlackNumberDaoImpl bndi = new BlackNumberDaoImpl(this);
        for (int i = 0; i <= 20; i++) {
            boolean seccess = bndi.insert(new BlackNumber("张琦"+i, "1327066570"+i, (new Random().nextInt(3)+1)+""));
        }
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        /**
         * 动态的更新版本
         */
        splash_tv_version.setText("版本号:" + PackageUtil.getVersionName(this));
        /**
         * 通过网络检查是否有更高的版本
         * 是否需要更新  通过getVerisonCode()方法和网络获取的版本号来判断
         */
        boolean flag = SpUtil.getBoolean(this, Constant.AUTO_UPDATE, true);
        if(flag){
            checkVersionCode();
        }else{
            handler.sendEmptyMessageDelayed(ENTERHOME,2000);
        }
    }

    /**
     * 检查是否需要更新
     * 因为是耗时操作 所以开启子线程来下载
     * 服务器上的文件  通过handler更新UI
     */
    private void checkVersionCode() {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                Message msg = Message.obtain();
                try {
                    /**
                     * 使用HttpUrl框架来访问服务器的资源
                     */
                    URL url = new URL("http://169.254.207.255:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");   //设置连接方式
                    conn.setConnectTimeout(5000);   //设置超时连接
                    conn.setReadTimeout(5000);      //设置访问超时
                    conn.connect();                 //连接
                    if (conn.getResponseCode() == 200) {  //如果响应码是200  就代表成功
                        String json = StreamJson.ReadJson(conn.getInputStream());
                        JSONObject jo = new JSONObject(json);
                        mVersionName = jo.getString("versionName");
                        mVersionCode = jo.getInt("versionCode");
                        mDescription = jo.getString("description");
                        mDownloadUrl = jo.getString("downloadUrl");
                        /**
                         * 如果版本大于现在的版本则提示更新
                         */
                        if (mVersionCode > PackageUtil.getVersionCode(SplashActivity.this)) {
                            msg.what = UPDATE;
                        } else {
                            msg.what = NO_UPDATE;
                        }
                    } else {
                        //TODO    响应码不是200的情况
                        msg.what = RESPON;
                    }
                } catch (ProtocolException e) {
                    msg.what = PROTOCCOL;
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    private void enterHome(){
        startActivity(new Intent(SplashActivity.this,HomeActivity.class));
        finish();
    }
}
