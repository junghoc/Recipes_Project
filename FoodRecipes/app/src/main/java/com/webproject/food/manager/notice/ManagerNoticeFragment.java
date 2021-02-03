package com.webproject.food.manager.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webproject.food.ManagerActivity;
import com.webproject.food.R;

import java.util.ArrayList;

public class ManagerNoticeFragment extends Fragment {

    RadioGroup notice_radiogroup;
    Button add_btn;
    ListView listview;

    ManagerNoticeAdapter adapter = null;
    ArrayList<NoticeVO> arr = null;

    ManagerActivity activity = ManagerActivity.getInstance();

    public static ManagerNoticeFragment newInstance() {
        return new ManagerNoticeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_notice_fragment, container, false);

        notice_radiogroup = view.findViewById(R.id.notice_radiogroup);
        add_btn = view.findViewById(R.id.add_btn);
        listview = view.findViewById(R.id.notice_listview);

        return view;

    } // onCreateView

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notice_radiogroup.setOnCheckedChangeListener(listener);

        listener.onCheckedChanged(notice_radiogroup, R.id.now_notice);

        // TODO : 새글작성

    } // onActivityCreated

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            arr = null;
            switch (i){
                case R.id.now_notice :
                    // TODO
                    //TEST
                    arr = new ArrayList<>();
                    NoticeVO vo = new NoticeVO();
                    vo.setIdx("0");
                    vo.setDate("date");
                    vo.setTitle("now_notice : 클릭");
                    vo.setStatus("");
                    arr.add(vo);

                    break;
                case R.id.all_notice :
                    // TODO
                    //TEST
                    arr = new ArrayList<>();
                    NoticeVO vo2 = new NoticeVO();
                    vo2.setIdx("0");
                    vo2.setDate("date");
                    vo2.setTitle("all_notice : 클릭");
                    vo2.setStatus("");
                    arr.add(vo2);

                    break;
            }

            adapter = new ManagerNoticeAdapter(activity, R.layout.manager_notice_form,arr,listview);

            listview.setAdapter(adapter);
        }
    }; // OnCheckedChangeListener

}