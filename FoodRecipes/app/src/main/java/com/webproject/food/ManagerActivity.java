package com.webproject.food;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.webproject.food.manager.board.ManagerBoardFragment;
import com.webproject.food.manager.member.ManagerMemberFragment;
import com.webproject.food.manager.notice.ManagerNoticeFragment;
import com.webproject.food.manager.qna.ManagerQnaFragment;

public class ManagerActivity extends AppCompatActivity {

    FragmentManager fm;

    Button member_btn, board_btn, qna_btn, notice_btn;

    private static ManagerActivity activity;

    FragmentTransaction fragmentTransaction;

    int backPressWork = 0;

    public static ManagerActivity getInstance() {
        return activity;
    }

    // 아이피 주소
    private static final String IP = "192.168.219.101";
    public static String getIp() {
        return IP;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity);
        activity = this;

        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container,ManagerMemberFragment.newInstance());
        fragmentTransaction.commit();

        member_btn = findViewById(R.id.member_manage);
        board_btn = findViewById(R.id.board_manage);
        qna_btn = findViewById(R.id.qna_manage);
        notice_btn = findViewById(R.id.notice_manage);

        member_btn.setOnClickListener(click);
        board_btn.setOnClickListener(click);
        qna_btn.setOnClickListener(click);
        notice_btn.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            switch (v.getId()){
                case R.id.member_manage :
                    fragmentTransaction.replace(R.id.container,ManagerMemberFragment.newInstance());
                    break;
                case R.id.board_manage :
                    fragmentTransaction.replace(R.id.container,ManagerBoardFragment.newInstance());
                    break;
                case R.id.qna_manage :
                    fragmentTransaction.replace(R.id.container, ManagerQnaFragment.newInstance());
                    break;
                case R.id.notice_manage :
                    fragmentTransaction.replace(R.id.container, ManagerNoticeFragment.newInstance());
                    break;
            }

            backPressWork = 0;

            fragmentTransaction.commit();
        }
    };

    // 뒤로가기 키 설정
    @Override
    public void onBackPressed()  {
        if(backPressWork != 0){
            switch (backPressWork){
                case BACKPRESS_BOARD_INFO :
                    board_btn.callOnClick();
                    break;
                case BACKPRESS_MEMBER_INFO :
                    member_btn.callOnClick();
                    break;
                case BACKPRESS_NOTICE_INFO :
                    notice_btn.callOnClick();
                    break;
                case BACKPRESS_QNA_INFO :
                    qna_btn.callOnClick();
                    break;
                case BACKPRESS_BLOCK :
                    // 뒤로가기 키 BLOCK
                    break;
            }
        }else{
            // TODO : 뒤로가기를 눌렀을 때 기능 추가
            finish();
        }
    } // onBackPressed()

    // 내용 갱신
    public void replacefragment(Fragment fragment, int backPress_setting){
        fragmentTransaction = fm.beginTransaction();
        backPressWork = backPress_setting;
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    } // replacefragment()

    // backPress_setting
    public final int BACKPRESS_FINISH = 0;
    public final int BACKPRESS_BOARD_INFO = 1;
    public final int BACKPRESS_MEMBER_INFO = 2;
    public final int BACKPRESS_NOTICE_INFO = 3;
    public final int BACKPRESS_QNA_INFO = 4;
    public final int BACKPRESS_BLOCK = 5;

}