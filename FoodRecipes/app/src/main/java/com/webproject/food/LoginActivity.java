package com.webproject.food;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.webproject.food.vo.MemberVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    // 메인 객체를 담는다.
    MainActivity main_activity = (MainActivity) MainActivity.main_activity;

    // 로그인정보를 저장할 VO
    static MemberVO vo = new MemberVO();

    ImageView view, back_home;
    EditText input_id, input_pwd;
    Button btn_login;
    TextView find_info, regi_member;
    // SYNC NOW
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 검색
        back_home = findViewById(R.id.back_home);
        input_id = findViewById(R.id.input_id);
        input_pwd = findViewById(R.id.input_pwd);
        btn_login = findViewById(R.id.btn_login);
        find_info = findViewById(R.id.find_info);
        regi_member = findViewById(R.id.regi_member);

        // X 이미지 클릭 이벤트 감지
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인으로 되돌아가도록 액티비티 걷어냄
                finish();
            }
        });

        // 아이디/비밀번호 찾기 화면으로 이동
        find_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,FindInfoActivity.class);
                startActivity(i);
            }
        });

        // 아이디 회원가입
        regi_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });


        // 아이디 로그인
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Log.i("MY","inputid : "+input_id.getText());
                sweetAlertDialog = new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("로그인 실패");
                sweetAlertDialog.setConfirmText("확인");

                if(input_id.getText().toString().length()==0){
                    sweetAlertDialog.setContentText("아이디를 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }else if(input_pwd.getText().toString().length()==0){
                    sweetAlertDialog.setContentText("비밀번호를 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                // 서버에 값 전달한 뒤, 일치 여부에따라서 접속 혹은 실패 결과 반환
                String id = input_id.getText().toString();
                String pwd = input_pwd.getText().toString();

                String result = "id="+id+"&pwd="+pwd;

                new Login_Task().execute(result,"type_login");
            }
        });
    }

    // 로그인 TASK 클래스
    class Login_Task extends AsyncTask<String, Void, String>{

        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/login/list.jsp";

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            // 연결할 주소로 접근
            try {
                String str = "";

                // 전송할 서버 지정 및 준비
                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                // 서버로 내용 전송
                sendMsg = strings[0]+"&type="+strings[1];
                osw.write(sendMsg);
                osw.flush();

                // 서버로 전송이 완료되면 서버측(jsp)에서 처리한 결과를 되받아야한다.
                if(conn.getResponseCode()==conn.HTTP_OK){ // 200이면 정상, 404,500은 비정상

                    // TMP에 해당 내용을 받는다.
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                    // READER가 서버로부터 결과 데이터를 가지고있으므로 이를 BufferedReader를 통해서 한줄 단위로 가져온다.
                    BufferedReader reader = new BufferedReader(tmp);

                    // {'result':'%s'}}
                    StringBuffer buffer = new StringBuffer();
                    while ( (str=reader.readLine())!=null ){
                        buffer.append(str);
                    }

                    receiveMsg = buffer.toString();

                    // 일반 JSON타입은 JSONOBJECT클래스가 담당
                    // JSON 배열타입은 JSONARRAY클래스가 담당
                    // 배열안에 있는 데이터를 가져올떄는 JSONOBJECT, 배열을 가져올때는 JSONARRAY
                    JSONArray jarray = new JSONObject(receiveMsg).getJSONArray("res");
                    JSONObject jsonObject  = jarray.getJSONObject(0);

                    result = jsonObject.optString("result");

                    if(result.equalsIgnoreCase("yes")){
                        // VO객체에 다 담아주기
                        jsonObject  = jarray.getJSONObject(1);
                        vo.setMember_idx(jsonObject.optInt("member_idx"));

                        jsonObject  = jarray.getJSONObject(2);
                        vo.setId(jsonObject.optString("id"));

                        jsonObject  = jarray.getJSONObject(3);
                        vo.setPwd(jsonObject.optString("pwd"));

                        jsonObject  = jarray.getJSONObject(4);
                        vo.setName(jsonObject.optString("name"));

                        jsonObject  = jarray.getJSONObject(5);
                        vo.setGender(jsonObject.optString("gender"));

                        jsonObject  = jarray.getJSONObject(6);
                        vo.setBirth(jsonObject.optString("birth"));

                        jsonObject  = jarray.getJSONObject(7);
                        vo.setPhone(jsonObject.optString("phone"));

                        jsonObject  = jarray.getJSONObject(8);
                        vo.setEmail(jsonObject.optString("email"));

                        jsonObject  = jarray.getJSONObject(9);
                        vo.setReg_date(jsonObject.optString("reg_date"));

                        jsonObject  = jarray.getJSONObject(10);
                        vo.setMgr(jsonObject.optInt("mgr"));

                        jsonObject  = jarray.getJSONObject(11);
                        vo.setDel_info(jsonObject.optInt("del_info"));

                    }else{
                        result = "no";
                    }

                }

            }catch (Exception e){
                Log.e("MY",e.toString());
            }

            return result;
        }

        // 결과를 표현하는 ONPOSTEXECUTE
        @Override
        protected void onPostExecute(String s) {
            // 로그인 후에 처리할 작업
            // 추후 로그인시 정보 전부 다 가지고 메인이동, 로그인 실패시 제자리 대기 예정
            if(s.equalsIgnoreCase("yes")){

                // 정보를 VO에 전부 담았으니, 이 정보를 SHARED PREFERENCE에 전부 담자
                SharedPreferences sharedPreferences = getSharedPreferences("vo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("member_idx",vo.getMember_idx());
                editor.putString("id",vo.getId());
                editor.putString("pwd",vo.getPwd());
                editor.putString("name",vo.getName());
                editor.putString("gender",vo.getName());
                editor.putString("birth",vo.getBirth());
                editor.putString("phone",vo.getPhone());
                editor.putString("email",vo.getEmail());
                editor.putString("reg_date",vo.getReg_date());
                editor.putInt("mgr",vo.getMgr());
                editor.putInt("del_info",vo.getDel_info());
                editor.commit();

                // 기존에 있는 메인 액티비티 종료
                main_activity.finish();

                sweetAlertDialog = new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("로그인 성공");
                sweetAlertDialog.setConfirmText("메인으로 이동합니다.");
                sweetAlertDialog.setConfirmText("확인");

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        // 로그인 액티비티 종료
                        finish();

                        // 새로운 액티비티 시작
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        sweetAlertDialog.dismiss();
                    }
                });

                sweetAlertDialog.show();
            }else{
                sweetAlertDialog = new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("로그인 실패");
                sweetAlertDialog.setConfirmText("아이디와 비밀번호를 확인해주세요.");
                sweetAlertDialog.setConfirmText("확인");
                sweetAlertDialog.show();
            }
        }
    }

}