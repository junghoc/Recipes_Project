package com.webproject.food.manager.member;

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

import com.webproject.food.ManagerActivity;
import com.webproject.food.R;

import java.util.ArrayList;

public class ManagerMemberAdapter extends ArrayAdapter<MemberVO> {
    ManagerActivity context;
    ArrayList<MemberVO> arr;
    int resource;
    ListView listView;

    public ManagerMemberAdapter(@NonNull ManagerActivity context, int resource, ArrayList<MemberVO> arr, ListView listView) {
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

        MemberVO vo = arr.get(position);

        TextView idx_form = convertView.findViewById(R.id.idx_form);
        TextView id_form = convertView.findViewById(R.id.id_form);
        TextView name_form = convertView.findViewById(R.id.name_form);
        TextView regdate_form = convertView.findViewById(R.id.regdate_form);
        //TextView status_form = convertView.findViewById(R.id.status_form);

        idx_form.setText(vo.getIdx());
        id_form.setText(vo.getId());
        name_form.setText(vo.getName());
        regdate_form.setText(vo.getRegdate());
        //status_form.setText(vo.getDel_info());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MY","MY:"+vo.getIdx());
                context.replacefragment(ManagerMemberInfoFragment.newInstance(vo.getIdx()),context.BACKPRESS_MEMBER_INFO);

            }
        });

        return convertView;

    }
}
