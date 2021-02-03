package com.webproject.food.manager.member;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webproject.food.ManagerActivity;
import com.webproject.food.R;

public class ManagerMemberEditFragment extends Fragment {

    String idx; // member의 파라마터 idx

    View view;
    ManagerActivity activity;

    TextView idx_member, birth_member, regdate_member, status_member;
    EditText id_et, pwd_et, name_et, phone_et, email_et;
    Button id_btn, birth_btn, pre_btn, edit_btn;
    RadioGroup gender_group;
    RadioButton male_radio, female_radio;

    public ManagerMemberEditFragment(String idx){
        this.idx = idx;
    }

    public static ManagerMemberEditFragment newInstatnce(String idx){return new ManagerMemberEditFragment(idx);}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.manager_member_edit, container, false);

        activity = ManagerActivity.getInstance();

        // TextView
        idx_member = view.findViewById(R.id.idx_board);
        birth_member = view.findViewById(R.id.writer_board);
        regdate_member = view.findViewById(R.id.regdate_member);
        status_member = view.findViewById(R.id.status_member);

        // EditText
        id_et = view.findViewById(R.id.id_et);
        pwd_et = view.findViewById(R.id.pwd_et);
        name_et = view.findViewById(R.id.name_et);
        phone_et = view.findViewById(R.id.phone_et);
        email_et = view.findViewById(R.id.email_et);

        // Button
        id_btn = view.findViewById(R.id.id_btn);
        birth_btn = view.findViewById(R.id.birth_btn);
        pre_btn = view.findViewById(R.id.pre_btn);
        edit_btn = view.findViewById(R.id.edit_btn);

        // RadioGroup
        gender_group = view.findViewById(R.id.gender_group);

        // RadioButton
        male_radio = view.findViewById(R.id.male_radio);
        female_radio = view.findViewById(R.id.female_radio);

        return view;
    }

    MemberVO vo = null;

    int year, month, day;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO : 넘겨받은 idx를 통해서 db의 데이터를 vo에 담는다
        // vo 갱신
        // vo = ?

//        // vo 샘플데이터
//        vo = new MemberVO();
//        vo.setSeq_member_idx(idx);
//        vo.setId("test_id");
//        vo.setPwd("test0111");
//        vo.setName("테스트");
//        vo.setGender("male");
//        vo.setBirth("1998-12-04");
//        vo.setPhone("010-8938-1577");
//        vo.setEmail("test011@naver.com");
//        vo.setReg_date("2021-01-01");
//        vo.setDel_info("NORMAL");
//
//        // 날짜 샘플데이터
//        year = 1998;
//        month = 12;
//        day = 4;

        // 담아온 vo의 내용을 갱신
        idx_member.setText(vo.getIdx());
        id_et.setText(vo.getId());
        id_et.setEnabled(false);
        pwd_et.setText(vo.getPwd());
        name_et.setText(vo.getName());
        if(vo.getBirth().equalsIgnoreCase("male")){ // TODO : vo의 가져오는 값에 따라 추후 수정
            male_radio.callOnClick();
        }else{
            female_radio.callOnClick();
        }
        birth_member.setText(vo.getBirth());
        phone_et.setText(vo.getPhone());
        email_et.setText(vo.getEmail());
        regdate_member.setText(vo.getRegdate());
        //status_member.setText(vo.getDel_info());

        // 버튼 기능 만들기

        // id 수정 버튼 클릭
        id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_et.isEnabled() == false){
                    id_et.setEnabled(true);
                    id_btn.setText("중복체크");
                }else{
                    // TODO : 아이디 중복체크

                    id_et.setEnabled(false);
                    id_btn.setText("수정");
                }
            }
        });

        // BIRTH 선택 버튼 클릭
        birth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 달력 표시
                DatePickerDialog dialog = new DatePickerDialog(activity, callbackMethod, year, month, day);

                dialog.show();
            }
        });

        // 하단의 Button
        pre_btn.setOnClickListener(bottom_click); // 이전
        edit_btn.setOnClickListener(bottom_click); // 수정
    }

    // BIRTH의 날짜 선택 완료 후 메서드
    DatePickerDialog.OnDateSetListener callbackMethod = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            birth_member.setText(String.format("%d-%d-%d", year, month, day)); // TODO : BIRTH의 FORMATING 형식은 추후 방향에 따라 수정
        }
    };

    // 하단의 Button OnClickListener()
    View.OnClickListener bottom_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            switch (view.getId()){
                case R.id.pre_btn :
                    builder.setTitle("이전");
                    builder.setMessage("정말 되돌아가겠습니까? (현재까지 작성한 내용이 저장되지 않습니다.)");
                    builder.setNegativeButton("아니오",null);
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // MemberInfoFragment로 돌아감
                            activity.replacefragment(ManagerMemberInfoFragment.newInstance(idx), activity.BACKPRESS_MEMBER_INFO);
                        }
                    });
                    break;
                case R.id.edit_btn :
                    builder.setTitle("수정");
                    builder.setMessage("회원 정보를 수정하시겠습니까?");
                    builder.setNegativeButton("아니오",null);
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // TODO : 회원정보를 저장

                            // MemberInfoFragment로 돌아감
                            activity.replacefragment(ManagerMemberInfoFragment.newInstance(idx), activity.BACKPRESS_MEMBER_INFO);
                        }
                    });
                    break;
            }
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };

}
