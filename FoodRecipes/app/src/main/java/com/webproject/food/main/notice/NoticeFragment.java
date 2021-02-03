package com.webproject.food.main.notice;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import java.util.ArrayList;


public class NoticeFragment extends Fragment {

    Spinner qa_spinner;
    EditText qa_search;
    Button btn_qa_search;
    Button btn_qa_create;
    ArrayAdapter adapter;
    Qa_Adapters adapters_view;
    android.widget.ListView qa_list;
    Context ct;

    ArrayList<QaVO> arr;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notice, container, false);
        ct = container.getContext();

        qa_spinner = (Spinner)root.findViewById(R.id.qa_spinner);
        qa_search = root.findViewById(R.id.qa_search);
        btn_qa_search = root.findViewById(R.id.btn_qa_search);
        btn_qa_create = root.findViewById(R.id.btn_qa_create);
        qa_list = (android.widget.ListView)root.findViewById(R.id.qa_list);

        arr = new ArrayList<>();

        adapters_view = new Qa_Adapters(ct, R.layout.qa_list_item, arr, qa_list);
        qa_list.setAdapter(adapters_view);

        Task task = new Task();
        task.execute();

        //액티비티 전환
        btn_qa_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ct, Qa_Create.class);
                startActivity(i);
            }
        });

        //스피너
        String[] items = {"작성자", "글제목", "글내용"};
        adapter = new ArrayAdapter<>(ct,android.R.layout.simple_spinner_dropdown_item,items);
        qa_spinner.setAdapter(adapter);
        qa_spinner.setSelection(0);
        qa_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //검색
        btn_qa_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qa_search.getText().toString().length() == 0) {
                    Toast.makeText(ct, "검색어를 입력해주세요", Toast.LENGTH_LONG).show();
                } else {
                    String sel = (String) qa_spinner.getSelectedItem().toString();
                    String text = String.valueOf(qa_search.getText());
                    String result = "sel=" + sel + "&text=" + text;
                    Toast.makeText(ct, result, Toast.LENGTH_LONG).show();

                    arr = new ArrayList<>();

                    adapters_view = new Qa_Adapters(ct, R.layout.qa_list_item, arr, qa_list);
                    qa_list.setAdapter(adapters_view);

                    Task task = new Task(result);
                    task.execute();
                }
            }
        });

        return root;
    }

    public class Task extends AsyncTask<String, Void, JSONArray> {
        public String ip ="192.168.219.101"; //자신의 IP번호
        String sendMsg, receiveMsg;
        String serverip; // 연결할 jsp주소

        public Task(String sendmsg){
            serverip = "http://"+ip+":9090/Recipe_Project/qa/qa_select2.jsp";
            this.sendMsg = sendmsg;
        }

        public Task() {
            serverip = "http://"+ip+":9090/Recipe_Project/qa/qa_select.jsp";
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

                if (sendMsg != null) {
                    osw.write(sendMsg);
                    Log.i("json",sendMsg);
                }

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

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = jarray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                QaVO vo = new QaVO();
                vo.setQa_idx(Integer.parseInt(jsonObject.optString("idx")));
                vo.setQa_title(jsonObject.optString("title"));
                vo.setQa_id(jsonObject.optString("id"));
                vo.setQa_view(Integer.parseInt(jsonObject.optString("view")));
                vo.setQa_date(jsonObject.optString("date"));
                arr.add(vo);

                Log.i("json",vo.getQa_id());
            }
            adapters_view.notifyDataSetChanged();
        }

    }
}