package com.webproject.food.manager.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webproject.food.ManagerActivity;
import com.webproject.food.R;

public class ManagerNoticeInfoFragment extends Fragment {

    String idx;
    ManagerActivity activity;
    View view;
    NoticeVO vo;

    TextView idx_board, title_board, content_board, date_board, status_board;
    Button pre_btn, move_btn, del_btn;

    public static ManagerNoticeInfoFragment newInstance(String idx) { return new ManagerNoticeInfoFragment(idx); }

    public ManagerNoticeInfoFragment(String idx){
        this.idx = idx;
        activity = ManagerActivity.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manager_notice_info, container, false);

        idx_board = view.findViewById(R.id.idx_board);
        title_board = view.findViewById(R.id.title_board);
        content_board = view.findViewById(R.id.content_board);
        date_board = view.findViewById(R.id.date_board);
        status_board = view.findViewById(R.id.status_board);
        pre_btn = view.findViewById(R.id.pre_btn);
        move_btn = view.findViewById(R.id.move_btn);
        del_btn = view.findViewById(R.id.del_btn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO : IDX를 이용하여 DB에서 NoticeVO에 대이터를 담는다

        // 샘플데이터
        vo = new NoticeVO();
        vo.setIdx(idx);
        vo.setTitle("제목");
        vo.setContent("내용입니다");
        vo.setDate("2021-01-01");
        vo.setStatus("NORMAL");

        // 데이터를 view에 담는다
        idx_board.setText(vo.getIdx());
        title_board.setText(vo.getTitle());
        content_board.setText(vo.getContent());
        date_board.setText(vo.getDate());
        status_board.setText(vo.getStatus());

    }


}
