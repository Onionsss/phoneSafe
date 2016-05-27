package heima.it.safe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import heima.it.safe.view.RocketView;

public class RocketService extends Service {

    private RocketView mRv;

    public RocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRv = new RocketView(this);
        mRv.showRocket();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRv.stopRocket();
    }
}
