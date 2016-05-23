package heima.it.safe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import heima.it.safe.R;
import heima.it.safe.bean.BlackNumber;

/**
 * 作者：张琦 on 2016/5/23 20:16
 */
public class BlackNumberAdapter extends BaseAdapter{
    private List<BlackNumber> list;
    private Context context;

    public BlackNumberAdapter(Context context,List<BlackNumber> list){
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BlackNumber getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView != null && convertView.getTag() != null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = View.inflate(context, R.layout.listview_blacknumber,null);
            holder = new ViewHolder();
            holder.tv_bn_name = (TextView) convertView.findViewById(R.id.tv_bn_name);
            holder.tv_bn_address = (TextView) convertView.findViewById(R.id.tv_bn_address);
            holder.tv_bn_number = (TextView) convertView.findViewById(R.id.tv_bn_number);
            holder.tv_bn_mode = (TextView) convertView.findViewById(R.id.tv_bn_mode);

            convertView.setTag(holder);
        }

        BlackNumber item = getItem(position);
        holder.tv_bn_name.setText(item.getName());
        holder.tv_bn_number.setText(item.getPhone());
        if("1".equals(item.getMode())){
            holder.tv_bn_mode.setText("短信+电话拦截");
        }else if("2".equals(item.getMode())){
            holder.tv_bn_mode.setText("短信拦截");
        }else if("3".equals(item.getMode())){
            holder.tv_bn_mode.setText("电话拦截");
        }
        holder.tv_bn_address.setText("江苏南通联通");
        return convertView;
    }
    static class ViewHolder{
        TextView tv_bn_name;
        TextView tv_bn_address;
        TextView tv_bn_mode;
        TextView tv_bn_number;
    }
}
