package heima.it.safe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import heima.it.safe.R;
import heima.it.safe.bean.HomeItem;

/**
 * 作者：张琦 on 2016/5/18 17:05
 */
public class HomeAdapter extends BaseAdapter{
    private Context context;
    private List<HomeItem> list;
    public HomeAdapter(Context context,List<HomeItem> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_home_menu,null);
        ImageView home_item_icon = (ImageView) view.findViewById(R.id.home_item_icon);
        TextView home_item_title = (TextView) view.findViewById(R.id.home_item_title);
        TextView home_item_desc = (TextView) view.findViewById(R.id.home_item_desc);
        HomeItem hi = list.get(position);
        if(hi != null){
            home_item_icon.setImageResource(hi.getImage());
            home_item_title.setText(hi.getTitle());
            home_item_desc.setText(hi.getDesc());
        }

        return view;
    }
}
