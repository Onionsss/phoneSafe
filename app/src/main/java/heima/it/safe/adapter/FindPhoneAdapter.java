package heima.it.safe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import heima.it.safe.bean.FindPhoneChild;
import heima.it.safe.bean.FindPhoneGroup;

/**
 * 作者：张琦 on 2016/5/27 19:08
 */
public class FindPhoneAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<FindPhoneGroup> mList;
    public FindPhoneAdapter(Context context, List<FindPhoneGroup> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).list.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = new TextView(mContext);
            }
        TextView tv = (TextView) convertView;
        tv.setText(mList.get(groupPosition).getName());
        tv.setTextSize(25);
        tv.setPadding(5,5,5,5);
        tv.setBackgroundColor(Color.GRAY);
        return tv;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = new TextView(mContext);
        }
        TextView tv = (TextView) convertView;
        FindPhoneChild fpc = mList.get(groupPosition).list.get(childPosition);
        tv.setText(fpc.getName()+"/n"+fpc.getNumber());
        tv.setTextSize(21);
        tv.setPadding(8,8,8,8);
        tv.setBackgroundColor(Color.WHITE);
        return tv;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
