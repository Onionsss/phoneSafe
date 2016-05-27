package heima.it.safe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import heima.it.safe.R;
import heima.it.safe.utils.SpUtil;

/**
 * 作者：张琦 on 2016/5/27 18:27
 */
public class AddressStyleAdapter extends BaseAdapter{
    private Context mContext;
    public int[] shapes;
    public String[] shapesName;
    public AddressStyleAdapter(Context context,int[] shapes,String[] shapesName){
        this.mContext = context;
        this.shapes = shapes;
        this.shapesName = shapesName;
    }
    @Override
    public int getCount() {
        return shapes.length;
    }

    @Override
    public Object getItem(int position) {
        return shapes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(mContext,R.layout.dialog_style_item,null);
        }
        ImageView address_style_iv = (ImageView) convertView.findViewById(R.id.address_style_iv);
        TextView address_style_tv = (TextView) convertView.findViewById(R.id.address_style_tv);
        ImageView address_style_select = (ImageView) convertView.findViewById(R.id.address_style_select);
        int styleRes = SpUtil.getInt(mContext, "addressstyle", R.drawable.shape_address_toast_bg_gray);

        address_style_select.setVisibility(shapes[position] == styleRes?View.VISIBLE:View.GONE);
        address_style_iv.setImageResource(shapes[position]);
        address_style_tv.setText(shapesName[position]);
        return convertView;
    }
}
