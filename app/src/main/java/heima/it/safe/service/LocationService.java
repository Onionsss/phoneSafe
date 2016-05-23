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

import java.io.IOException;

import heima.it.safe.constant.Constant;
import heima.it.safe.utils.OkUtils;
import heima.it.safe.utils.SpUtil;

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
                double altitude = location.getAltitude();
                double longitude = location.getLongitude();
                /**
                 * 访问接口 将经纬度转换为详细地址
                 */
                SmsManager sm = SmsManager.getDefault();
                String safePhone = SpUtil.getString(LocationService.this, Constant.SAFE_PHONE, "");
                sm.sendTextMessage(safePhone, null, "altitude" + altitude + "longitude" + longitude, null, null);
                /**
                 * 发送完短信后 杀死这个服务
                 */
                stopSelf();
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
        private double altitude;

        public LocationImpl(double altitude, double longitude) {
            this.altitude = altitude;
            this.longitude = longitude;
        }

        private double longitude;
        @Override
        public void run() {
            try {
                OkUtils.getResponse("");
            } catch (IOException e) {
                e.printStackTrace();
            }finally{

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLm.removeUpdates(mLl);
    }
}
