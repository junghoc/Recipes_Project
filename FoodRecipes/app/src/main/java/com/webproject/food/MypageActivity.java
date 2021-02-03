package com.webproject.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MypageActivity extends AppCompatActivity {

    Intent i;
    LinearLayout edit_user, edit_pwd;
    TextView mypage_tv_user, mypage_tv_pwd, mypage_tv_birth;
    ImageView img_back;
    DatePickerDialog.OnDateSetListener callbackMethod;
    Button user_btn_can, pwd_btn_can, user_btn_okay, pwd_btn_okay;
    TextView mypage_id, mypage_name;
    EditText mypage_phone, mypage_email, mypage_pwd, mypage_new_pwd, mypage_new_pwd_re;

    String id, name, birth, phone, email, pwd, new_pwd;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //아이디 검색
        edit_user = findViewById(R.id.edit_user);
        edit_pwd = findViewById(R.id.edit_pwd);
        mypage_tv_user = findViewById(R.id.mypage_tv_user);
        mypage_tv_pwd = findViewById(R.id.mypage_tv_pwd);
        img_back = findViewById(R.id.img_back);

        i = getIntent();
        String mypageparam = i.getStringExtra("mypageparam");

        if( mypageparam.equals("정보수정")){
            edit_user.setVisibility(View.VISIBLE);
            edit_pwd.setVisibility(View.GONE);
            mypage_tv_user.setBackground(ContextCompat.getDrawable(this, R.drawable.select_border));
            mypage_tv_pwd.setBackground(ContextCompat.getDrawable(this, R.drawable.non_select_border));
        }else {
            edit_user.setVisibility(View.GONE);
            edit_pwd.setVisibility(View.VISIBLE);
            mypage_tv_user.setBackground(ContextCompat.getDrawable(this, R.drawable.non_select_border));
            mypage_tv_pwd.setBackground(ContextCompat.getDrawable(this, R.drawable.select_border));
        }

        img_back.setOnClickListener(click);
        mypage_tv_user.setOnClickListener(click);
        mypage_tv_pwd.setOnClickListener(click);

        //달력
        mypage_tv_birth = findViewById(R.id.mypage_tv_birth);
        this.InitializeListener();

        //취소
        pwd_btn_can = findViewById(R.id.pwd_btn_can);
        user_btn_can = findViewById(R.id.user_btn_can);

        pwd_btn_can.setOnClickListener(click);
        user_btn_can.setOnClickListener(click);

        //회원정보 수정에 관한 아이디 검색
        mypage_id = findViewById(R.id.mypage_id);
        mypage_name = findViewById(R.id.mypage_name);
        mypage_phone = findViewById(R.id.mypage_phone);
        mypage_email = findViewById(R.id.mypage_email);
        user_btn_okay = findViewById(R.id.user_btn_okay);

        //쉐어드에서 정보를 넣어주기
        SharedPreferences login = getSharedPreferences("vo", MODE_PRIVATE);
        //쉐어드안에있는 id를 검색해서, value에 값이 없는경우 뒤의""값으로 대체한다
        id = login.getString("id","test");
        name = login.getString("name","테스트");
        birth = login.getString("birth","2021-01-04");
        if(birth.length() > 10){
            birth = birth.substring(0,10);
        }
        phone = login.getString("phone","0101234567");
        email = login.getString("email","test1234@test.com");
        pwd = login.getString("pwd", "1234");

        //데이터값 넣어주기
        mypage_id.setText(id);
        mypage_name.setText(name);
        mypage_tv_birth.setText(birth);
        mypage_phone.setText(phone);
        mypage_email.setText(email);

        //회원정보 수정 버튼 클릭이벤트
        user_btn_okay.setOnClickListener(send);

        //비밀번호 수정에 관한 아이디 검색
        mypage_pwd = findViewById(R.id.mypage_pwd);
        mypage_new_pwd = findViewById(R.id.mypage_new_pwd);
        mypage_new_pwd_re = findViewById(R.id.mypage_new_pwd_re);
        pwd_btn_okay = findViewById(R.id.pwd_btn_okay);

        //비밀번호 수정 버튼 클릭이벤트
        pwd_btn_okay.setOnClickListener(send);

    }

    //회원정보 수정 버튼 클릭이벤트
    View.OnClickListener send = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String result = "";
            switch (view.getId()){
                case R.id.user_btn_okay:
                    check = 1;
                    birth = mypage_tv_birth.getText().toString();
                    phone = mypage_phone.getText().toString();
                    email = mypage_email.getText().toString();
                    result = "id=" + id + "&birth=" + birth
                                    + "&phone=" + phone + "&email=" + email;
                    new Task().execute(result, "type_mypage_user");
                    break;

                case R.id.pwd_btn_okay:
                    check = 2;
                    String pwd_check = mypage_pwd.getText().toString();
                    new_pwd = mypage_new_pwd.getText().toString();
                    String new_pwd_re = mypage_new_pwd_re.getText().toString();
                    if(!pwd.equals(pwd_check) || !new_pwd_re.equals(new_pwd)){
                        Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        mypage_pwd.setText("");
                        mypage_new_pwd.setText("");
                        mypage_new_pwd_re.setText("");
                        break;
                    }
                    result = "id=" + id + "&pwd=" + new_pwd;
                    new Task().execute(result, "type_mypage_pwd");
                    break;
            }
        }
    };

    //회원가입용 Task클래스
    class Task extends AsyncTask<String, Void, String> {

        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/Recipe_Project/mypage/mypage_user.jsp";//연결할 jsp의 주소

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            try {

                String str = "";
                URL url = new URL(serverip);

                //url경로에 문제가 없다면 서버와 접속
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                //서버로 전달하고자 하는 내용
                //list.jsp?id=aaa&pwd=1111&type=type_regi
                sendMsg = strings[0] + "&type=" + strings[1];

                osw.write(sendMsg);
                osw.flush();

                //서버로 데이터의 전송이 완료되면
                //서버측(jsp)에서 처리한 결과값을 돌려받아야 한다

                if( conn.getResponseCode() == conn.HTTP_OK ){//200이면 정상, 404,500등은 비정상
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");

                    //reader가 서버로부터 결과데이터를 가지고 있으므로
                    //이를 BufferedReader를 통해 한줄 단위로 읽어들인다
                    BufferedReader reader = new BufferedReader(tmp);

                    //{res:[{'result':'%s'}]}
                    StringBuffer buffer = new StringBuffer();
                    while( (str = reader.readLine()) != null ){
                        buffer.append(str);
                    }

                    receiveMsg = buffer.toString();
                    Log.i("MY", "receiveMsg : " + receiveMsg);

                    //receiveMsg에 해당입력한 값이 포함되잇으면 receiveMsg의 값을 알맞게 바꾸어준다
                    if( receiveMsg.contains("{res:[{'result','fail'}]}") ){
                        result = "fail";
                    }else if(receiveMsg.contains("{res:[{'result','success'}]}")){
                        result = "success";
                    }

                }//if( conn.getResponseCode() == conn.HTTP_OK ){}

            } catch (Exception e) {

            }//catch

            return result;
        }//doInBackground

        @Override
        protected void onPostExecute(String s) {
            Log.i("MY", "s : " + s);
            if( s.equals("success")){
                //회원탈퇴 성공
                Log.i("MY", "로그아웃성공");
                //쉐어드 수정
                SharedPreferences sharedPreferences = getSharedPreferences("vo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(check == 1){
                    Log.i("MY", "birth : " + birth);
                    //비밀번호를 제외한 값을 수정
                    editor.putString("birth", birth);
                    editor.putString("phone", phone);
                    editor.putString("email", email);
                }else if(check == 2){
                    //비밀번호를 수정
                    editor.putString("pwd", new_pwd);
                }
                editor.commit();
                check = 0;
                //기존 main엑티비티 finish
                MainActivity MA = (MainActivity)MainActivity.main_activity;
                MA.finish();
                //인텐트 이동
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            }else{
                //토스트 띄우고 엑티비티 종료
                Toast.makeText(getApplicationContext(),"정상적으로 작동하지 않았습니다. 관리자에게 문의해 주세요.",Toast.LENGTH_SHORT).show();
                finish();
            }

        }

    }

    //클릭이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_back:
                    finish();
                    break;

                case R.id.mypage_tv_user:
                    edit_user.setVisibility(View.VISIBLE);
                    edit_pwd.setVisibility(View.GONE);
                    mypage_tv_user.setBackground(ContextCompat.getDrawable(MypageActivity.this, R.drawable.select_border));
                    mypage_tv_pwd.setBackground(ContextCompat.getDrawable(MypageActivity.this, R.drawable.non_select_border));
                    break;

                case R.id.mypage_tv_pwd:
                    edit_user.setVisibility(View.GONE);
                    edit_pwd.setVisibility(View.VISIBLE);
                    mypage_tv_user.setBackground(ContextCompat.getDrawable(MypageActivity.this, R.drawable.non_select_border));
                    mypage_tv_pwd.setBackground(ContextCompat.getDrawable(MypageActivity.this, R.drawable.select_border));
                    break;

                case R.id.pwd_btn_can:
                    finish();
                    break;

                case R.id.user_btn_can:
                    finish();
                    break;
            }
        }
    };

    //
    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;
                mypage_tv_birth.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        };
    }

    public void OnClickHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 0, 5);

        dialog.show();
    }

}