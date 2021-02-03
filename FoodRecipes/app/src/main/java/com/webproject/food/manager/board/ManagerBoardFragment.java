package com.webproject.food.manager.board;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webproject.food.ManagerActivity;
import com.webproject.food.R;
import com.webproject.food.vo.BoardVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ManagerBoardFragment extends Fragment {

    // layout의 요소
    EditText id_qna;
    Button id_btn, write_board;
    ListView listview;

    ManagerActivity activity = ManagerActivity.getInstance();

    ManagerBoardAdapter adapter = null;
    ArrayList<BoardVO> arr = null;

    public static ManagerBoardFragment newInstance() {
        return new ManagerBoardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_board_fragment, container, false);

        // findViewById
        id_qna = view.findViewById(R.id.id_qna);
        id_btn = view.findViewById(R.id.id_btn);
        listview = view.findViewById(R.id.board_listview);
        write_board = view.findViewById(R.id.write_board);

        return view;
    } // onCreateView()

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        new GetListView().execute(""); // 모든 게시글을 listview로 전송

        // 검색 기능
        id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = id_qna.getText().toString();
                new GetListView_Search().execute(keyword);
            }
        });

        write_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.replacefragment(ManagerBoardwriteFragment.newInstance(),activity.BACKPRESS_BLOCK);
            }
        });

    } // onActivityCreated()

    class GetListView extends AsyncTask<String, Void, JSONArray>{

        String ip = activity.getIp();
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/manager_board/manager_board_getlist.jsp";

        @Override
        protected JSONArray doInBackground(String... strings) {
            JSONArray jarray = null;

            // 연결할 주소로 접근
            try {
                String str = "";

                // 전송할 서버 지정 및 준비
                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

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
                    jarray = new JSONObject(receiveMsg).getJSONArray("res");
                    
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return jarray;

        }

        @Override
        protected void onPostExecute(JSONArray jarray) {

            arr = new ArrayList<>();
            adapter = new ManagerBoardAdapter(activity, R.layout.manager_board_form, arr, listview);

            listview.setAdapter(adapter); // listview 셋팅

            if(jarray == null){
                Toast.makeText(activity, "개시글을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jsonObject = null;

                try {
                    jsonObject = jarray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BoardVO vo = new BoardVO();
                vo.setIdx(jsonObject.optString("idx"));
                vo.setTitle(jsonObject.optString("title"));
                vo.setM_name(jsonObject.optString("id"));
                vo.setDate(jsonObject.optString("date"));
                vo.setStatus(jsonObject.optString("status"));
                arr.add(vo);
            }
            adapter.notifyDataSetChanged();
        }
    }

    class GetListView_Search extends AsyncTask<String, Void, JSONArray>{

        String ip = activity.getIp();
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/manager_board/manager_board_getlist.jsp";

        @Override
        protected JSONArray doInBackground(String... strings) {
            JSONArray jarray = null;

            // 연결할 주소로 접근
            try {
                String str = "";

                sendMsg = "search=" + strings[0];

                // 전송할 서버 지정 및 준비
                URL url = new URL(serverip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
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
                    jarray = new JSONObject(receiveMsg).getJSONArray("res");

                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return jarray;

        }

        @Override
        protected void onPostExecute(JSONArray jarray) {

            arr = new ArrayList<>();
            adapter = new ManagerBoardAdapter(activity, R.layout.manager_board_form, arr, listview);

            listview.setAdapter(adapter); // listview 셋팅

            // jarray를 가져왔는지 체크 : 서버에 연결이 안 되었을 경우
            if(jarray == null){
                Toast.makeText(activity, "개시글을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            // DB검색 내용을 arr에 담는다
            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jsonObject = null;

                try {
                    jsonObject = jarray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BoardVO vo = new BoardVO();
                vo.setIdx(jsonObject.optString("idx"));
                vo.setTitle(jsonObject.optString("title"));
                vo.setM_name(jsonObject.optString("id"));
                vo.setDate(jsonObject.optString("date"));
                vo.setStatus(jsonObject.optString("status"));
                arr.add(vo);
            }

            // 검색결과 없음
            if(jarray.length() == 0){
                Toast.makeText(activity, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
        }
    }

}