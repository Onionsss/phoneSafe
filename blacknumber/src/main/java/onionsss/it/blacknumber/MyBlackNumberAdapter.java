package onionsss.it.blacknumber;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 作者：张琦 on 2016/5/24 20:06
 */
public class MyBlackNumberAdapter extends BaseAdapter{
    private List<BlackNumber> mList;
    private Context mContext;
    public MyBlackNumberAdapter(List<BlackNumber> list,Context context){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BlackNumber getItem(int position) {
        return mList.get(position);
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
                convertView = View.inflate(mContext,R.layout.listview_blacknumber,null);
                holder = new ViewHolder();
                holder.tv_bn_name = (TextView) convertView.findViewById(R.id.tv_bn_name);
                holder.tv_bn_phone = (TextView) convertView.findViewById(R.id.tv_bn_number);
                holder.tv_bn_address = (TextView) convertView.findViewById(R.id.tv_bn_address);
                holder.tv_bn_mode = (TextView) convertView.findViewById(R.id.tv_bn_mode);
                holder.tv_bn_image = (ImageView) convertView.findViewById(R.id.tv_bn_image);
                convertView.setTag(holder);
            }
        final BlackNumber bn = mList.get(position);
        holder.tv_bn_name.setText(bn.getName());
        holder.tv_bn_phone.setText(bn.getPhone());
        holder.tv_bn_address.setText("(江苏南通联通)");
        if(bn.getMode().equals("1")){
            holder.tv_bn_mode.setText("短信拦截");
        }else if(bn.getMode().equals("2")){
            holder.tv_bn_mode.setText("电话拦截");
        }else if(bn.getMode().equals("3")){
            holder.tv_bn_mode.setText("短信+电话拦截");
        }

        holder.tv_bn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(bn);
                MyBlackNumberAdapter.this.notifyDataSetChanged();
                Toast.makeText(mContext,"删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
    static class ViewHolder{
        TextView tv_bn_name;
        TextView tv_bn_phone;
        TextView tv_bn_address;
        TextView tv_bn_mode;
        ImageView tv_bn_image;

    }
}
