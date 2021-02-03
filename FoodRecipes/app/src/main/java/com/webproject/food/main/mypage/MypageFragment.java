package com.webproject.food.main.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.webproject.food.MypageActivity;
import com.webproject.food.R;
import com.webproject.food.WithDrawalActivity;
import com.webproject.food.main.logout.LogoutFragment;

public class MypageFragment extends Fragment {

    LinearLayout mypage_edit, mypage_edit_pwd, mypage_logout, mypage_withdrawal;
    Intent i;
    Context ct;
    // SYNC NOW
    SweetAlertDialog sweetAlertDialog;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        ct = container.getContext();

        mypage_edit = root.findViewById(R.id.mypage_edit);
        mypage_edit_pwd = root.findViewById(R.id.mypage_edit_pwd);
        mypage_logout = root.findViewById(R.id.mypage_logout);
        mypage_withdrawal = root.findViewById(R.id.mypage_withdrawal);
        mypage_edit.setOnClickListener(click);
        mypage_edit_pwd.setOnClickListener(click);
        mypage_logout.setOnClickListener(click);
        mypage_withdrawal.setOnClickListener(click);
        
        return root;
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //쉐어드에서 정보를 넣어주기
            SharedPreferences login = getActivity().getSharedPreferences("vo", getContext().MODE_PRIVATE);
            //쉐어드안에있는 id를 검색해서, value에 값이 없는경우 뒤의""값으로 대체한다
            String id = login.getString("id","");
            if(id == ""){
                sweetAlertDialog = new SweetAlertDialog(ct,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("로그인 후 이용해주세요.");
                sweetAlertDialog.setConfirmText("확인");
                sweetAlertDialog.show();
            }else {
                switch (view.getId()) {

                    case R.id.mypage_edit:
                        i = new Intent(getActivity(), MypageActivity.class);
                        i.putExtra("mypageparam", "정보수정");
                        startActivity(i);
                        break;

                    case R.id.mypage_edit_pwd:
                        i = new Intent(getActivity(), MypageActivity.class);
                        i.putExtra("mypageparam", "비밀번호");
                        startActivity(i);
                        break;

                    case R.id.mypage_logout:
                        //로그아웃
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LogoutFragment()).commit();
                        break;

                    case R.id.mypage_withdrawal:
                        i = new Intent(getActivity(), WithDrawalActivity.class);
                        startActivity(i);
                        break;

                }
            }

        }
    };

}