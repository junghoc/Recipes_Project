package com.webproject.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WithDrawalActivity extends AppCompatActivity {

    ImageView img_back;
    CheckBox withdrawal_check;
    Button withdrawal_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_drawal);

        img_back = findViewById(R.id.img_back);
        withdrawal_check = findViewById(R.id.withdrawal_check);
        withdrawal_send = findViewById(R.id.withdrawal_send);

        //클릭이벤트
        img_back.setOnClickListener(click);
        withdrawal_send.setOnClickListener(click);

        //체크박스 체크이벤트
        withdrawal_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                withdrawal_send.setEnabled(true);
            }
        });


    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_back:
                    finish();
                    break;

                case R.id.withdrawal_send:
                    //로그인 되있지는 sharedpreference로 확인
                    SharedPreferences login = getSharedPreferences("login", MODE_PRIVATE);
                    //쉐어드안에있는 id를 검색해서, value에 값이 없는경우 뒤의""값으로 대체한다
                    String id = login.getString("id","test");
                    String result = "id=" + id;

                    new Task().execute(result, "type_withdrawal");
                    break;

            }
        }
    };

    //회원가입용 Task클래스
    class Task extends AsyncTask<String, Void, String> {

        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/Recipe_Project/mypage/withdrawal.jsp";//연결할 jsp의 주소

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
                Log.i("MY", "회원탈퇴성공");
                //기존 main엑티비티 finish
                MainActivity MA = (MainActivity)MainActivity.main_activity;
                MA.finish();
                //쉐어드 삭제
                SharedPreferences pref = getSharedPreferences("vo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
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
}