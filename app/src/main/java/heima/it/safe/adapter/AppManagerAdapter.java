package heima.it.safe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import heima.it.safe.R;
import heima.it.safe.bean.AppInfo;

/**
 * 作者：张琦 on 2016/5/21 22:02
 */
public class AppManagerAdapter extends BaseAdapter{
    private Context context;
    private List<AppInfo> systemApp;
    private List<AppInfo> userApp;

    public AppManagerAdapter(Context context,List<AppInfo> systemApp,List<AppInfo> userApp){
        this.context = context;
        this.systemApp = systemApp;
        this.userApp = userApp;
    }

    @Override
    public int getCount() {
        return systemApp.size()+userApp.size()+2;
    }

    @Override
    public AppInfo getItem(int position) {
        if(position == 0){
            return null;
        }else if(position == userApp.size()+1){
            return null;
        }
        AppInfo app =  new AppInfo();
        if(position < userApp.size()+1){
            app = userApp.get(position-1);
        }else{
            app = systemApp.get(position-(userApp.size()+2));
        }
        return app;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == userApp.size()+1){
            /**
             * 0代表特殊条目
             */
            return 0;
        }
        /**
         * 1代表普通条目
         */
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewTypeCount = getItemViewType(position);
        switch(viewTypeCount){
                case 0:
                    TextView tv = new TextView(context);
                    tv.setTextSize(25);
                    tv.setPadding(5,5,5,5);
                    tv.setBackgroundColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.BLACK);
                    if(position == 0){
                        tv.setText("用户程序"+"("+userApp.size()+")个");
                    }else if(position == userApp.size()+1){
                        tv.setText("系统程序"+"("+systemApp.size()+")个");
                    }
                    return tv;
            case 1:
                AppInfo app = getItem(position);
                ViewHolder holder = null;
                if(convertView != null && app instanceof AppInfo && convertView.getTag() != null){
                    holder = (ViewHolder) convertView.getTag();
                }else{
                    convertView = View.inflate(context, R.layout.listview_softmanager,null);
                    holder = new ViewHolder();
                    holder.iv_softicon = (ImageView) convertView.findViewById(R.id.iv_softicon);
                    holder.tv_soft_appname = (TextView) convertView.findViewById(R.id.tv_soft_appname);
                    holder.tv_soft_size = (TextView) convertView.findViewById(R.id.tv_soft_size);
                    holder.tv_soft_position = (TextView) convertView.findViewById(R.id.tv_soft_position);
                    convertView.setTag(holder);
                }
                holder.iv_softicon.setImageDrawable(app.getAppIcon());
                holder.tv_soft_appname.setText(app.getAppName());
                holder.tv_soft_size.setText(Formatter.formatFileSize(context,app.getAppSize()));
                holder.tv_soft_position.setText(app.isRom()?"手机内存":"SD卡");
                break;
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_softicon;
        TextView tv_soft_appname;
        TextView tv_soft_size;
        TextView tv_soft_position;
    }
}
