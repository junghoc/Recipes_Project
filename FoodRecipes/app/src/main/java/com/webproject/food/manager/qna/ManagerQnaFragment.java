package com.webproject.food.manager.qna;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webproject.food.ManagerActivity;
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

public class ManagerQnaFragment extends Fragment {

    TextView total_cnt, wait_cnt, answered_cnt;
    RadioGroup radioGroup;
    ListView qna_listview;
    EditText id_qna, title_qna;
    Button search_btn;

    ArrayList<QaVO> arr;
    ManagerQnaAdapter adapter;

    ManagerActivity activity = ManagerActivity.getInstance();

    public static ManagerQnaFragment newInstance() {
        return new ManagerQnaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_qna_fragment, container, false);

        // findViewById
        total_cnt = view.findViewById(R.id.total_cnt);
        wait_cnt = view.findViewById(R.id.wait_cnt);
        answered_cnt = view.findViewById(R.id.answered_cnt);
        radioGroup = view.findViewById(R.id.radiogroup);
        qna_listview = view.findViewById(R.id.qna_listview);
        id_qna = view.findViewById(R.id.id_qna);
        title_qna = view.findViewById(R.id.title_qna);
        search_btn = view.findViewById(R.id.search_btn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO : 총 문의 수
        //total_cnt.setText(arr.size());

        // TODO : 대기중인 문의 수
        wait_cnt.setText("2");

        // TODO : 답변 완료 문의 수
        answered_cnt.setText("3");

        arr = new ArrayList<>(); // arr 초기화

        adapter = new ManagerQnaAdapter(activity,R.layout.manager_qna_form,arr,qna_listview);

        qna_listview.setAdapter(adapter);
        GetListView task = new GetListView();
        task.execute();

       /* radioGroup.setOnCheckedChangeListener(listener); // radioGroup 기능 추가*/

        //listener.onCheckedChanged(radioGroup,R.id.wait_qna); // 페이지 초기값


        // TODO : search_btn 검색 구현
    } // onActivityCreated

    // radioGroup의 OnCheckedChangeListener
    /*RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            arr = null;
            arr = new ArrayList<>(); // arr 초기화
            switch (i){
                // TODO : 스위치 별로 정보전송 받기
                case R.id.wait_qna:
                    // TEST
                    //QaVO vo = new QaVO();
                    adapter = new ManagerQnaAdapter(activity, R.layout.manager_qna_form, arr, qna_listview);

                    qna_listview.setAdapter(adapter);

                    new ManagerQnaFragment.GetListView().execute("");
                    break;
                case R.id.answered_qna:
                    // TEST
                    QaVO vo2 = new QaVO();
                   *//* vo2.setIdx("idx");
                    vo2.setId("answered_id");
                    vo2.setTitle("답변입니다");
                    vo2.setStatus("정상");
                    vo2.setDate("00/00");*//*
                    arr.add(vo2);
                    // --
                    break;
                case R.id.all_qna:
                    // TEST
                    adapter = new ManagerQnaAdapter(activity, R.layout.manager_qna_form, arr, qna_listview);

                    qna_listview.setAdapter(adapter);

                    new ManagerQnaFragment.GetListView().execute("");
                    // --
                    break;
            } // switch*/
    //}; // OnCheckedChangeListener

    class GetListView extends AsyncTask<String, Void, JSONArray> {

        String ip = activity.getIp();
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/Recipe_Project/manager_qa/manager_qa_getlist.jsp";

        @Override
        protected JSONArray doInBackground(String... strings) {
            Log.i("json","onPostExecute");
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
                vo.setQa_date(jsonObject.optString("date"));
                arr.add(vo);
                Log.i("vo", vo.getQa_id());
            }
            adapter.notifyDataSetChanged();
        }

    }
}
