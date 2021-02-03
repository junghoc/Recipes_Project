package com.webproject.food.main.notice;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.webproject.food.R;
import com.webproject.food.vo.QaVO;

import java.util.ArrayList;

public class Qa_Adapters extends ArrayAdapter<QaVO> {
    Context context;
    ArrayList<QaVO> arr;
    int resource;
    ListView listView;
    public Qa_Adapters(Context context, int resource, ArrayList<QaVO> arr, ListView listView){
        super(context,resource,arr);
        this.context = context;
        this.resource = resource;
        this.arr = arr;
        this.listView = listView;
        Log.i("json","new adapter");
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.qa_list_item, null);

        QaVO vo = arr.get(position);

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView numTextView = (TextView) convertView.findViewById(R.id.number) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title) ;
        TextView idTextView = (TextView) convertView.findViewById(R.id.id) ;
        TextView viewTextView = (TextView) convertView.findViewById(R.id.view) ;
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date) ;
        LinearLayout qa_list_item  = (LinearLayout)convertView.findViewById(R.id.qa_list_item);
        // 아이템 내 각 위젯에 데이터 반영

        titleTextView.setText(vo.getQa_title());
        numTextView.setText(Integer.toString(vo.getQa_idx()));
        idTextView.setText(vo.getQa_id());
        viewTextView.setText(Integer.toString(vo.getQa_view()));
        dateTextView.setText(vo.getQa_date());
        Log.i("json","adapter : " + position);

        return convertView;
    }


}
