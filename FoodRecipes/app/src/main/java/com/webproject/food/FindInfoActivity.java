package com.webproject.food;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class FindInfoActivity extends AppCompatActivity {

    RelativeLayout layout_id, layout_pwd;
    EditText id_phone, id_check, pwd_id, pwd_phone, pwd_check;
    Button btn_id, btn_pwd;
    ImageView img_back;
    TextView txt_id, txt_pwd;
    SweetAlertDialog sweetAlertDialog;

    // 아이디나 비번 정보 담을 임시 객체
    MemberVO vo = new MemberVO();

    String id, phone, name, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_info);

        layout_id = findViewById(R.id.layout_id);
        layout_pwd = findViewById(R.id.layout_pwd);
        id_phone = findViewById(R.id.id_phone);
        id_check = findViewById(R.id.id_check);
        pwd_id = findViewById(R.id.pwd_id);
        pwd_phone = findViewById(R.id.pwd_phone);
        pwd_check = findViewById(R.id.pwd_check);
        btn_id = findViewById(R.id.btn_id);
        btn_pwd = findViewById(R.id.btn_pwd);
        img_back = findViewById(R.id.img_back);
        txt_id = findViewById(R.id.txt_id);
        txt_pwd = findViewById(R.id.txt_pwd);

        // 아이디 찾기와 비번찾기 누른 경우
        txt_id.setOnClickListener(layout_click);
        txt_pwd.setOnClickListener(layout_click);

        // 뒤로가기
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 아이디 찾기 버튼
        btn_id.setOnClickListener(find_click);
        // 비번 찾기 버튼
        btn_pwd.setOnClickListener(find_click);
    }

    // 아이디 찾기 , 비번찾기 화면 전환
    View.OnClickListener layout_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                // 레이아웃에 따라서 보여지는 화면 다르게 동작
                case R.id.txt_id:
                    layout_id.setVisibility(View.VISIBLE);
                    layout_pwd.setVisibility(View.GONE);
                    txt_id.setTypeface(txt_id.getTypeface(), Typeface.BOLD);
                    break;

                case R.id.txt_pwd:
                    layout_id.setVisibility(View.GONE);
                    layout_pwd.setVisibility(View.VISIBLE);
                    txt_pwd.setTypeface(txt_pwd.getTypeface(), Typeface.BOLD);
                    break;
            }
        }
    };

    // 아이디 찾기, 비번찾기 버튼 감지자
    View.OnClickListener find_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 아이디 찾기, 비번찾기 버튼 클릭시
            sweetAlertDialog = new SweetAlertDialog(FindInfoActivity.this,SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("ERROR");
            sweetAlertDialog.setConfirmText("CONFIRM");

            switch (view.getId()){
                // 아이디 찾기
                case R.id.btn_id:
                    if(id_phone.getText().toString().length()==0){
                        sweetAlertDialog.setContentText("휴대폰 번호를 입력해주세요.");
                        sweetAlertDialog.show();
                        return;
                    }

                    if(id_check.getText().toString().length()==0){
                        sweetAlertDialog.setContentText("인증번호(이름)를 입력해주세요.");
                        sweetAlertDialog.show();
                        return;
                    }

                    // 서버에 값 전달한 뒤, 일치 여부에따라서 접속 혹은 실패 결과 반환
                    phone = id_phone.getText().toString();
                    name = id_check.getText().toString();

                    result = "phone="+phone+"&name="+name;

                    // 정상적인 값이 있으니 서버 연결하여서, 아이디 찾기
                    new Find_idTask().execute(result, "type_findid");

                    break;

                // 비번 찾기
                case R.id.btn_pwd:
                    if(pwd_id.getText().toString().length()==0){
                        sweetAlertDialog.setContentText("아이디를 입력해주세요.");
                        sweetAlertDialog.show();
                        return;
                    }

                    if(pwd_phone.getText().toString().length()==0){
                        sweetAlertDialog.setContentText("핸드폰 번호를 입력해주세요.");
                        sweetAlertDialog.show();
                        return;
                    }

                    if(pwd_check.getText().toString().length()==0){
                        sweetAlertDialog.setContentText("이름(인증번호)를 입력해주세요.");
                        sweetAlertDialog.show();
                        return;
                    }

                    // 서버에 값 전달한 뒤, 일치 여부에따라서 접속 혹은 실패 결과 반환
                    id = pwd_id.getText().toString();
                    phone = pwd_phone.getText().toString();
                    name = pwd_check.getText().toString();

                    result = "phone="+phone+"&name="+name+"&id="+id;

                    // 정상적인 값이 있으니 서버 연결하여서, 비번 찾기
                    new Find_pwdTask().execute(result, "type_findpwd");

                    break;
            }
        }
    };

    // 아이디 찾기 서버 접속
    class Find_idTask extends AsyncTask<String, Void, String> {

        // IP 체크
        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/login/find_id.jsp";

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

                    // 서버에서는 [{'result':'%s'},{'info':'%s'}] 로 반환하게 할 것이다.
                    if(result.equalsIgnoreCase("yes")){
                        // 찾았으니 해당 id값을 전송
                        jsonObject  = jarray.getJSONObject(1);
                        vo.setId(jsonObject.optString("id"));
                        result = "yes";

                    }else{
                        result = "no";
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return result;
        }

        // 아이디 처리하고 받은 값
        @Override
        protected void onPostExecute(String s) {
            if(s.equalsIgnoreCase("yes")){
                sweetAlertDialog = new SweetAlertDialog(FindInfoActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("검색 완료");
                sweetAlertDialog.setConfirmText("CONFIRM");
                sweetAlertDialog.setContentText("회원님의 아이디는 "+vo.getId()+"입니다.\n로그인해주세요.");

                // 확인시 로그인 화면으로 이동
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                        sweetAlertDialog.dismiss();
                    }
                });

                sweetAlertDialog.show();
            }else{
                sweetAlertDialog = new SweetAlertDialog(FindInfoActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("검색 실패");
                sweetAlertDialog.setConfirmText("CONFIRM");
                sweetAlertDialog.setContentText("존재하지 않는 회원입니다.\n정보를 다시 확인해주세요.");
                sweetAlertDialog.show();
            }
        }
    }

    // 비번 찾기 TASK
    class Find_pwdTask extends AsyncTask<String, Void, String>{

        // IP 체크
        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/login/find_pwd.jsp";

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

                    // 서버에서는 [{'result':'%s'},{'info':'%s'}] 로 반환하게 할 것이다.
                    if(result.equalsIgnoreCase("yes")){
                        // 찾았으니 해당 id값을 전송
                        jsonObject  = jarray.getJSONObject(1);
                        vo.setPwd(jsonObject.optString("pwd"));
                        result = "yes";

                    }else{
                        result = "no";
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return result;
        }

        // 비번 ONPOST
        @Override
        protected void onPostExecute(String s) {
            if(s.equalsIgnoreCase("yes")){
                sweetAlertDialog = new SweetAlertDialog(FindInfoActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("검색 완료");
                sweetAlertDialog.setConfirmText("CONFIRM");
                sweetAlertDialog.setContentText("회원님의 비밀번호는 "+vo.getPwd()+"입니다.\n로그인해주세요.");

                // 확인시 로그인 화면으로 이동
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                        sweetAlertDialog.dismiss();
                    }
                });

                sweetAlertDialog.show();
            }else{
                sweetAlertDialog = new SweetAlertDialog(FindInfoActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("검색 실패");
                sweetAlertDialog.setConfirmText("CONFIRM");
                sweetAlertDialog.setContentText("존재하지 않는 회원입니다.\n정보를 다시 확인해주세요.");
                sweetAlertDialog.show();
            }
        }
    }

}