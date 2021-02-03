package com.webproject.food.manager.notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webproject.food.R;

import java.util.ArrayList;

public class ManagerNoticeAdapter extends ArrayAdapter<NoticeVO> {
    Context context;
    ArrayList<NoticeVO> arr;
    int resource;
    ListView listView;

    public ManagerNoticeAdapter(@NonNull Context context, int resource, ArrayList<NoticeVO> arr, ListView listView) {
        super(context, resource, arr);

        this.context = context;
        this.resource = resource;
        this.arr = arr;
        this.listView = listView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = linf.inflate(resource, null);

        NoticeVO vo = arr.get(position);

        TextView idx_form = convertView.findViewById(R.id.idx_form);
        TextView title_form = convertView.findViewById(R.id.title_form);
        TextView date_form = convertView.findViewById(R.id.date_form);
        TextView status_form = convertView.findViewById(R.id.status_form);

        idx_form.setText(vo.getIdx());
        title_form.setText(vo.getTitle());
        date_form.setText(vo.getDate());
        status_form.setText(vo.getStatus());

        return convertView;

    }
}
