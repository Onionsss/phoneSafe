package heima.it.safe.activity;

import android.app.Activity;
import android.os.Bundle;

import heima.it.safe.R;

public class SetPage1Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page1);
    }

    @Override
    public Class<? extends Activity> getNextClass() {
        return SetPage2Activity.class;
    }

    @Override
    public Class<? extends Activity> getReturnClass() {
       return null;
    }
}
