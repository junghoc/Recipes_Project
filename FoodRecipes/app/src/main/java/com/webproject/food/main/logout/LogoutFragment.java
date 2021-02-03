package com.webproject.food.main.logout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.webproject.food.MainActivity;
import com.webproject.food.R;

public class LogoutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout, container, false);

        //쉐어드 삭제
        SharedPreferences pref = getActivity().getSharedPreferences("vo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        //editor.putInt("원래있는 키값", 0);//값 초기화

        //인텐트 이동
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);

        //이동하면서 페이지 지우기
        getActivity().finish();

        return root;
    }
}