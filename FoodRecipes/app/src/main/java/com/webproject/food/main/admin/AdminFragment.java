package com.webproject.food.main.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.webproject.food.ManagerActivity;
import com.webproject.food.MypageActivity;
import com.webproject.food.R;
import com.webproject.food.main.home.HomeFragment;
import com.webproject.food.main.logout.LogoutFragment;

public class AdminFragment extends Fragment {

    Context ct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_admin, container, false);
        ct = container.getContext();

        //관리자페이지로 intent
        Intent i = new Intent(getActivity(), ManagerActivity.class);
        startActivity(i);

        //fragment 홈으로 이동
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();

        return root;
    }
}