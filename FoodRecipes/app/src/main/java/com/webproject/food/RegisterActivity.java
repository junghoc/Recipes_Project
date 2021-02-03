package com.webproject.food;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    // 아이디 중복 체크 유무
    String idCheck = "false";

    ImageView img_back;
    EditText et_id, et_pwd, et_cpwd, et_name, et_birth, et_phone, et_email;
    Button btn_check, btn_cal, btn_phone, regi_member;
    RadioGroup gender_rg;
    SweetAlertDialog sweetAlertDialog;

    // 달력 다이얼로그를 표시하기위한 객체
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        img_back = findViewById(R.id.img_back);
        et_id = findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_pwd);
        et_cpwd = findViewById(R.id.et_cpwd);
        et_name = findViewById(R.id.et_name);
        et_birth = findViewById(R.id.et_birth);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        btn_check = findViewById(R.id.btn_check);
        btn_cal = findViewById(R.id.btn_cal);
        btn_phone = findViewById(R.id.btn_phone);
        regi_member = findViewById(R.id.regi_member);
        gender_rg = findViewById(R.id.gender_rg);


        RadioButton btn_gender = (RadioButton)findViewById(gender_rg.getCheckedRadioButtonId());


        // 뒤로가기 이미지 선택시 그냥 액티비티 삭제 > 로그인으로 이동
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 등록 버튼 클릭시
        regi_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // 다이얼로그 선언
                sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("ERROR");
                sweetAlertDialog.setConfirmText("CONFIRM");

                // 정보 유효성 검사
                if (et_id.getText().toString().length() == 0) {
                    sweetAlertDialog.setContentText("아이디를 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                if(idCheck.equalsIgnoreCase("false")){
                    sweetAlertDialog.setContentText("아이디를 중복체크하세요.");
                    sweetAlertDialog.show();
                    return;
                }

                if (et_pwd.getText().toString().length() == 0) {
                    sweetAlertDialog.setContentText("비밀번호를 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                if (et_cpwd.getText().toString().length() == 0) {
                    sweetAlertDialog.setContentText("비밀번호 확인의 내용을 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                if (!et_pwd.getText().toString().equalsIgnoreCase(et_cpwd.getText().toString())) {
                    sweetAlertDialog.setContentText("비밀번호가 맞지 않습니다.");
                    sweetAlertDialog.show();
                    return;
                }

                if(et_name.getText().toString().length()==0){
                    sweetAlertDialog.setContentText("이름을 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                if(et_birth.getText().toString().length()==0){
                    sweetAlertDialog.setContentText("생년월일을 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                if(et_phone.getText().toString().length()==0){
                    sweetAlertDialog.setContentText("핸드폰 번호를 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                if(et_email.getText().toString().length()==0){
                    sweetAlertDialog.setContentText("이메일을 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                // 해당 정보를 서버에 전송
                String id = et_id.getText().toString();
                String pwd = et_pwd.getText().toString();
                String name = et_name.getText().toString();
                String gender = "";

                if(gender_rg.getCheckedRadioButtonId()== R.id.radio_man){
                        gender = "남자";
                }else{
                    gender = "여자";
                }

                String birth = et_birth.getText().toString();
                String phone = et_phone.getText().toString();
                String email = et_email.getText().toString();

                String result = "id=" + id + "&pwd=" + pwd + "&name=" + name + "&gender=" + gender + "&birth=" + birth + "&phone=" + phone + "&email=" + email;

                new Reg_Task().execute(result, "type_register");

            }
        });

        // 아이디 중복 검사
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("ERROR");
                sweetAlertDialog.setConfirmText("CONFIRM");

                if(et_id.getText().toString().length()==0){
                    sweetAlertDialog.setContentText("아이디를 입력해주세요.");
                    sweetAlertDialog.show();
                    return;
                }

                // 중복 버튼이 눌리면 아이디 검색 후에, 같은 아이디가 있는지 확인한다.
                // 없으면 사용하시겠습니까?
                // 있으면 이미 사용중인 아이디입니다.
                String id = et_id.getText().toString();
                String result = "id=" + id;

                new idCheck_Task().execute(result, "type_idcheck");

            }
        });

        // 달력 버튼
        btn_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼을 누르면 달력이 뜨고 날짜를 선택 가능
                Calendar now = Calendar.getInstance();
                int y = now.get(Calendar.YEAR); // YEAR
                int m = now.get(Calendar.MONTH); // MONTH
                int d = now.get(Calendar.DAY_OF_MONTH); // DAY

                // DATEPICKERDIALOG (캘린더를 띄울 화면, 캘린더 날짜 감지자, 처음에 세팅되어야하는 YMD)
                dialog = new DatePickerDialog(RegisterActivity.this,
                        dateListener,
                        y,m,d);
                dialog.show();
            }
        });


        // 핸드폰 인증


    } // onCreate

    // idCheck_Task
    class idCheck_Task extends AsyncTask<String, Void, String>{

        // 아이디 체크
        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/login/idcheck.jsp";

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


                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            // 결과 값에 따른 출력
            if(s.equalsIgnoreCase("yes")){
                idCheck = "true";
                sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                sweetAlertDialog.setTitleText("검색 완료");
                sweetAlertDialog.setConfirmText("CONFIRM");
                sweetAlertDialog.setCancelText("CANCEL");
                sweetAlertDialog.setContentText("사용가능한 아이디입니다. \n해당 아이디를 사용하시겠습니까?");

                // 확인 이벤트 감지자
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Toast.makeText(getApplicationContext(),"해당 아이디를 사용합니다.",Toast.LENGTH_SHORT).show();
                        et_id.setClickable(false);
                        et_id.setFocusable(false);
                        et_pwd.requestFocus();
                        btn_check.setEnabled(false);
                        sweetAlertDialog.dismiss();
                    }
                });

                // 취소 이벤트 감지자
                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });

                sweetAlertDialog.show();

            }else{
                sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("ERROR");
                sweetAlertDialog.setConfirmText("CONFIRM");
                sweetAlertDialog.setContentText("이미 사용중인 아이디입니다.");
                sweetAlertDialog.show();
                return;
            }

        }
    }

    // 캘린더 날짜 감지자 등록
    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {

            String strDate = String.format("%d-%02d-%02d",y,m+1,d);
            et_birth.setText(strDate);

        }
    };

    // Reg_Task
    class Reg_Task extends AsyncTask<String, Void, String>{

        // 회원 등록
        String ip = "192.168.35.75";
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/AndJSPTest/login/reg_member.jsp";

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

                }

            }catch (Exception e){
                Log.e("MY",e.toString());
            }

            return result;
        }

        // ONPOST
        @Override
        protected void onPostExecute(String s) {
            if(s.equalsIgnoreCase("yes")){
                sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("등록 완료");
                sweetAlertDialog.setConfirmText("CONFIRM");
                sweetAlertDialog.setContentText("가입이 완료되었습니다.\n로그인해주세요.");

                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                        // 메인으로 이동
                        finish();
                    }
                });
                sweetAlertDialog.show();

            }else{
                sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("등록 실패");
                sweetAlertDialog.setConfirmText("CONFIRM");
                sweetAlertDialog.setContentText("오류가 발생했습니다.\n관리자에게 문의해주세요.");
                sweetAlertDialog.show();
            }
        }
    }
}