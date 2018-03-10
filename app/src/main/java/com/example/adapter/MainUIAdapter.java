package com.example.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunzhaohenan.phonemanager.R;

/**
 * Created by sunzhaohenan on 2018/3/10.
 */

public class MainUIAdapter extends BaseAdapter {
    private static final String[] NAMES = new String[] {"手机防盗", "通讯卫士", "软件管理", "流量管理", "任务管理", "手机杀毒",
            "系统优化", "高级工具", "设置中心"};

    private static final int[] ICONS = new int[] {R.drawable.widget, R.drawable.widget, R.drawable.widget,
            R.drawable.widget, R.drawable.widget, R.drawable.widget, R.drawable.widget,
            R.drawable.widget, R.drawable.widget};

    //声明成静态，起到一定的优化作用，关于adapter还有别的优化方法的，有机会我们再说
    private static ImageView imageView;
    private static TextView textView;

    private Context context;
    private LayoutInflater inflater;
    private SharedPreferences sp;

    public MainUIAdapter(Context context)
    {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount()
    {
        return NAMES.length;
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.main_item, null);
        imageView = (ImageView) view.findViewById(R.id.iv_main_icon);
        textView = (TextView) view.findViewById(R.id.tv_main_name);
        imageView.setImageResource(ICONS[position]);
        textView.setText(NAMES[position]);

        if(position == 0)
        {
            String name = sp.getString("lostName", "");
            if(!name.equals(""))
            {
                textView.setText(name);
            }
        }

        return view;
    }
}
