package com.webproject.food.main.notice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.webproject.food.R;
import com.webproject.food.vo.QaVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Qa_View extends AppCompatActivity {

    TextView idx_qa;
    TextView id_qa;
    TextView title_qa;
    TextView content_qa;
    TextView date_qa;

    Button move_btn_qa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_view);
        idx_qa = findViewById(R.id.idx_qa);
        id_qa = findViewById(R.id.id_qa);
        title_qa = findViewById(R.id.title_qa);
        content_qa = findViewById(R.id.content_qa);
        date_qa = findViewById(R.id.date_qa);
        move_btn_qa = findViewById(R.id.move_btn_qa);

        Intent intents = getIntent();
        String num = intents.getStringExtra("sel_num");
        Task task = new Task(num);
        Log.i("test",num);

        move_btn_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Qa_ListView.class);
                startActivity(intent);
            }
        });

    }

    public class Task extends AsyncTask<String, Void, JSONArray> {
        public String ip ="192.168.219.101"; //자신의 IP번호
        String sendMsg, receiveMsg;
        String serverip; // 연결할 jsp주소

        public Task(String sendmsg){
            serverip = "http://"+ip+":9090/Recipe_Project/qa/qa_view.jsp";
            this.sendMsg = sendmsg;
        }

        @Override
        protected JSONArray doInBackground(String... strings) {
            JSONArray jarray = null;

            try {
                String str = "";
                URL url = new URL(serverip);

                //url경로에 문제가 없다면 서버와 접속
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                osw.write(sendMsg);
                Log.i("json",sendMsg);

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

                    jarray = new JSONObject(receiveMsg).getJSONArray("res");
                    Log.i("json",jarray.toString());
                }//if( conn.getResponseCode() == conn.HTTP_OK ){}

            }catch (Exception e){

            }//catch

            return jarray;

        }//doInBackground

        @Override
        protected void onPostExecute(JSONArray jarray) {
            Log.i("json","onPostExecute");
            JSONObject jsonObject = null;
            try {
                jsonObject = jarray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            QaVO vo = new QaVO();
            vo.setQa_idx(Integer.parseInt(jsonObject.optString("idx")));
            vo.setQa_title(jsonObject.optString("title"));
            vo.setQa_id(jsonObject.optString("id"));
            vo.setQa_content(jsonObject.optString("content"));
            vo.setQa_date(jsonObject.optString("date"));

            idx_qa.setText(vo.getQa_idx());
            id_qa.setText(vo.getQa_id());
            content_qa.setText(vo.getQa_content());
            date_qa.setText(vo.getQa_date());
            title_qa.setText(vo.getQa_title());
        }

    }
}