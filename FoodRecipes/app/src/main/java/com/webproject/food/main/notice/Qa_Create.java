package com.webproject.food.main.notice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.webproject.food.MainActivity;
import com.webproject.food.R;
import com.webproject.food.main.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Qa_Create extends AppCompatActivity {

    EditText titleView;
    EditText contentView;
    Button btn_create;
    Button btn_back;
    // 메인 객체를 담는다.
    MainActivity main_activity = (MainActivity) MainActivity.main_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_create);

        titleView = findViewById(R.id.title);
        contentView = findViewById(R.id.content);
        btn_create = findViewById(R.id.btn_create);
        btn_back = findViewById(R.id.btn_back);

        //목록으로 돌아가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //글 등록하기
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleView.getText().toString();
                String content = contentView.getText().toString();
                String result = "title="+title+"&content="+content;//서버로 전달할 파라미터

                Task task = new Task(result);
                task.execute();
            }
        });

    }

    public class Task extends AsyncTask<String, Void, String> {
        public String ip ="192.168.219.101"; //자신의 IP번호
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/qa/qa_insert_user.jsp"; // 연결할 jsp주소
        String result = "";

        public Task(String sendmsg){
            this.sendMsg = sendmsg;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String str = "";
                URL url = new URL(serverip);

                //url경로에 문제가 없다면 서버와 접속
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                Log.i("sendmsg", sendMsg);
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

                    JSONArray jarray = new JSONObject(receiveMsg).getJSONArray("res");
                    JSONObject jsonObject = jarray.getJSONObject(0);
                    result = jsonObject.optString("result");
                }//if( conn.getResponseCode() == conn.HTTP_OK ){}

            }catch (Exception e){

            }//catch
            return result;

        }//doInBackground
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            main_activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NoticeFragment()).commit();
            finish();
        }
    }
}