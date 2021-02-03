package com.webproject.food.manager.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webproject.food.ManagerActivity;
import com.webproject.food.R;
import com.webproject.food.vo.BoardVO;

import java.util.ArrayList;

public class ManagerBoardAdapter extends ArrayAdapter<BoardVO> {
    ManagerActivity context;
    ArrayList<BoardVO> arr;
    int resource;
    ListView listView;

    public ManagerBoardAdapter(@NonNull ManagerActivity context, int resource, ArrayList<BoardVO> arr, ListView listView) {
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

        BoardVO vo = arr.get(position);

        TextView idx_form = convertView.findViewById(R.id.idx_form);
        TextView title_form = convertView.findViewById(R.id.title_form);
        TextView member_form = convertView.findViewById(R.id.member_form);
        TextView date_form = convertView.findViewById(R.id.date_form);

        idx_form.setText(vo.getIdx());
        title_form.setText(vo.getTitle());
        member_form.setText(vo.getM_name());
        date_form.setText(vo.getDate());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.replacefragment(ManagerBoardInfoFragment.newInstance(vo.getIdx()),context.BACKPRESS_BOARD_INFO);
            }
        });

        return convertView;

    }
}
