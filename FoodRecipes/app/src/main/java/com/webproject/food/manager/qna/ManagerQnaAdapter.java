package com.webproject.food.manager.qna;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.webproject.food.R;
import com.webproject.food.vo.QaVO;

import java.util.ArrayList;

public class ManagerQnaAdapter extends ArrayAdapter<QaVO> {
    Context context;
    ArrayList<QaVO> arr;
    int resource;
    ListView listView;

    public ManagerQnaAdapter(@NonNull Context context, int resource, ArrayList<QaVO> arr, ListView listView) {
        super(context, resource, arr);

        this.context = context;
        this.arr = arr;
        this.resource = resource;
        this.listView = listView;
        Log.i("json","new adapter");

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = linf.inflate(resource, null);

        QaVO vo = arr.get(position);

        TextView idx_form = convertView.findViewById(R.id.idx_form);
        TextView title_form = convertView.findViewById(R.id.title_form);
        TextView date_form = convertView.findViewById(R.id.date_form);
        //TextView status_form = convertView.findViewById(R.id.status_form);
        TextView id_form = convertView.findViewById(R.id.id_form);

        idx_form.setText(""+vo.getQa_idx());
        title_form.setText(vo.getQa_title());
        date_form.setText(vo.getQa_date());
        //status_form.setText(vo.getQa_answer_state());
        id_form.setText(vo.getQa_id());
        Log.i("json","adapter : " + position);

        return convertView;
    }
}
