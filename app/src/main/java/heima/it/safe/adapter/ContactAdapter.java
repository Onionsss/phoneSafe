package heima.it.safe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import heima.it.safe.R;
import heima.it.safe.bean.ContactInfo;

/**
 * 作者：张琦 on 2016/5/21 09:58
 */
public class ContactAdapter extends BaseAdapter{
    private Context context;
    private List<ContactInfo> list;
    public ContactAdapter(Context context,List<ContactInfo> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if(position == 0){
            return null;
        }else{
            return list.get(position-1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0){
            TextView tv = new TextView(context);
            tv.setText("不选择电话号码!");
            tv.setTextColor(Color.RED);
            tv.setPadding(10,10,10,10);
            tv.setTextSize(25);
            tv.setGravity(Gravity.CENTER);
            return tv;
        }
        ContactInfo info = list.get(position-1);
        ViewHolder holder = new ViewHolder();

        if(convertView != null && info instanceof ContactInfo){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = View.inflate(context, R.layout.item_contact, null);
            holder.contactitem_name = (TextView) convertView.findViewById(R.id.contactitem_name);
            holder.contactitem_address = (TextView) convertView.findViewById(R.id.contactitem_address);
            holder.contactitem_image = (ImageView) convertView.findViewById(R.id.contactitem_image);

            convertView.setTag(holder);
        }
        holder.contactitem_name.setText(info.getName());
        holder.contactitem_address.setText(info.getAddress());
        holder.contactitem_image.setImageBitmap(info.getHead());

        return convertView;
    }
    static class ViewHolder{
        TextView contactitem_name;
        TextView contactitem_address;
        ImageView contactitem_image;
    }
}
