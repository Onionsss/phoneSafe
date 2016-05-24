package heima.it.safe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import heima.it.safe.constant.Constant;
import heima.it.safe.utils.OkUtils;
import heima.it.safe.utils.SpUtil;
import okhttp3.Response;

public class LocationService extends Service {
    private static final String TAG = "LocationService";

    private LocationManager mLm;
    private LocationListener mLl;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startLocation();
    }

    /**
     * 开启GPS定位 发送位置给安全号码
     */
    private void startLocation() {
        mLm = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLl = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();// 纬度
                double longitude = location.getLongitude();
                /**
                 * 访问接口 将经纬度转换为详细地址
                 */
                Log.d(TAG, "onLocationChanged: ");
                new Thread(new LocationImpl(latitude,longitude)).start();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLl);
    }

    /**
     * 子线程访问接口
     */
    class LocationImpl implements Runnable{
        private double latitude;

        public LocationImpl(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        private double longitude;
//        接口地址：http://lbs.juhe.cn/api/getaddressbylngb
//			支持格式：json/xml
//			请求方式：get
//			请求示例：http://lbs.juhe.cn/api/getaddressbylngb?lngx=116.407431&lngy=39.914492
//			调用样例及调试工具：API测试工具
//			请求参数说明：
//			名称	类型	必填	说明
//			 	lngx	String	Y	google地图经度 (如：119.9772857)
//			 	lngy	String	Y	google地图纬度 (如：27.327578)
//			 	dtype	String	N	返回数据格式：json或xml,默认json
        @Override
        public void run() {
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            Log.d(TAG, "onLocationChanged111: "+longitude+"jj"+latitude);
            String url = "http://lbs.juhe.cn/api/getaddressbylngb?lngx="+longitude+"&lngy="+latitude;
            try {
                Response response = OkUtils.getResponse(url);
                is = response.body().byteStream();
                baos = new ByteArrayOutputStream();

                byte[] b = new byte[1024];
                int len = 0;
                while((len = is.read(b))!=-1){
                    baos.write(b,0,len);
                }
                String json = baos.toString();
                JSONObject jo = new JSONObject(json);
                JSONObject row = jo.getJSONObject("row");
                JSONObject result = row.getJSONObject("result");
                String formatted_address = result.getString("formatted_address");
                SmsManager sm = SmsManager.getDefault();
                String safePhone = SpUtil.getString(LocationService.this, Constant.SAFE_PHONE, "");
                sm.sendTextMessage(safePhone, null, "latitude" + latitude + "longitude" + longitude+"地址为"+formatted_address, null, null);
                /**
                 * 发送完短信后 杀死这个服务
                 */
                stopSelf();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                try {
                    if(is!=null){
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLm.removeUpdates(mLl);
    }
}
