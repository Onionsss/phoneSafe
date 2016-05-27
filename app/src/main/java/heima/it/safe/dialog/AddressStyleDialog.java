package heima.it.safe.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import heima.it.safe.R;
import heima.it.safe.adapter.AddressStyleAdapter;
import heima.it.safe.utils.SpUtil;

/**
 * 作者：张琦 on 2016/5/27 18:12
 */
public class AddressStyleDialog extends Dialog{
    public int[] shapes = {R. drawable.shape_address_toast_bg_normal,R.drawable.shape_address_toast_bg_orange,
            R.drawable.shape_address_toast_bg_green,R.drawable.shape_address_toast_bg_blue,R.drawable.shape_address_toast_bg_gray};
    public String[] shapesName = {"半透明","活力橙","苹果绿","卫士蓝","金属灰"};
    private ListView mAddress_dialog_lv;
    private AddressStyleAdapter mMsa;

    public AddressStyleDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public AddressStyleDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_style_dialog);
        mAddress_dialog_lv = (ListView) findViewById(R.id.address_dialog_lv);
        mMsa = new AddressStyleAdapter(getContext(), shapes, shapesName);
        mAddress_dialog_lv.setAdapter(mMsa);

        mAddress_dialog_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpUtil.putInt(getContext(),"addressstyle",shapes[position]);
                mMsa.notifyDataSetChanged();
            }
        });
    }
}
