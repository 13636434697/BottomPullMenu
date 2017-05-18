package com.xu.bottompullmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener{

    private ListView listView;
    private EditText et_input;
    private ArrayList<String> datas;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.ib_dropdown).setOnClickListener(this);

        et_input = (EditText) findViewById(R.id.et_input);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        //点击下键之后弹出下拉窗口
        showPopupWindow();
    }

    //点击空旷旁边的下拉键之后弹出下拉窗口
    private void showPopupWindow() {
        initListView();

        // 显示下拉选择框，（指定内容和宽高）
        popupWindow = new PopupWindow(listView, et_input.getWidth(), 300);

        // 设置点击外部区域, 自动隐藏下拉列表
        popupWindow.setOutsideTouchable(true); // 外部可触摸
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件

        //做好这个点击事件还是没有反应（因为button会抢listView的焦点），会在这里设置子孙后代获取焦点的方式，这里设置了控件自身的大小，
        //但是还不够，还需要在代码里面设置，默认是没有焦点的，设置true是可以获取焦点
        popupWindow.setFocusable(true); //设置可获取焦点

        // 显示在指定控件下（下拉框）参数：显示在谁下面，偏移量
        popupWindow.showAsDropDown(et_input, 0, -5);
    }

    // 初始化要在下拉窗口的显示的内容
    private void initListView() {
        listView = new ListView(this);
        //把listView的分割线取消
        listView.setDividerHeight(0);
        //listView设置背景
        listView.setBackgroundResource(R.drawable.listview_background);
        //给listView条目添加点击事件，在上面实现了方法
        listView.setOnItemClickListener(this);

        datas = new ArrayList<String>();
        // 模拟创建一些数据
        for (int i = 0; i < 30; i++) {
            datas.add((10000 + i) + "");
        }

        listView.setAdapter(new MyAdapter());
    }

    //给listView条目添加点击事件，在上面实现了方法，之后就是这个方法
    //做好这个点击事件还是没有反应（因为button会抢listView的焦点）
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        System.out.println("onItemClick: " + position);
        String string = datas.get(position);
        et_input.setText(string); // 设置文本

        popupWindow.dismiss(); // 消失了
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView == null){
                view = View.inflate(parent.getContext(), R.layout.item_number, null);
            }else {
                view = convertView;
            }

            //设置文本内容
            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
            tv_number.setText(datas.get(position));

            //给下拉菜单的删除按钮设置一个点击事件
            view.findViewById(R.id.ib_delete).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //移除相应的位置
                    datas.remove(position);
                    //通知listView更新界面
                    notifyDataSetChanged();

                    //如果删除完最后一条信息的时候
                    if(datas.size() == 0){
                        // 如果删除的是最后一条, 隐藏popupwindow
                        popupWindow.dismiss();
                    }
                }
            });
            return view;
        }

    }



}
