package com.webproject.food.manager.board;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webproject.food.ManagerActivity;
import com.webproject.food.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManagerBoardwriteFragment extends Fragment {

    ManagerActivity activity;
    View view;

    EditText title, content;
    Button pre_btn, add_btn;

    public ManagerBoardwriteFragment(){  }

    public static ManagerBoardwriteFragment newInstance(){ return new ManagerBoardwriteFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.manager_board_write, container, false);

        activity = ManagerActivity.getInstance();

        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);

        pre_btn = view.findViewById(R.id.pre_btn);
        add_btn = view.findViewById(R.id.add_btn);

        return view;
    } // onCreateView()

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pre_btn.setOnClickListener(bottom_click);
        add_btn.setOnClickListener(bottom_click);
    } // onActivityCreated()

    // BUTTON OnClickListener
    View.OnClickListener bottom_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            switch (view.getId()) {
                case R.id.pre_btn:
                    builder.setTitle("이전");
                    builder.setMessage("글 작성을 정말 취소하시겠습니까? (작성한 내용은 저장되지 않습니다)");
                    builder.setNegativeButton("아니오", null);
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.replacefragment(ManagerBoardFragment.newInstance(),activity.BACKPRESS_FINISH);
                        }
                    });
                    break;

                case R.id.add_btn:
                    builder.setTitle("등록");
                    builder.setMessage("새 글을 등록하시겠습니까?");
                    builder.setNegativeButton("아니오", null);
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new WriteBoard().execute(title.getText().toString(), content.getText().toString());
                        }
                    });
                    break;

            } // switch
            builder.show();
        }
    }; // bottom_click

    // 게시글 정보 조회
    class WriteBoard extends AsyncTask<String, Void, JSONArray> {

        String ip = activity.getIp();
        String receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/manager_board/manager_board_write.jsp";

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
                String param = String.format("title=%s&content=%s",strings[0],strings[1]);
                osw.write(param);

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
            if(jarray == null){
                Toast.makeText(activity, "게시글 등록 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = null;

            try {
                jsonObject = jarray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = jsonObject.optString("result");

            if(result.equalsIgnoreCase("no")){
                Toast.makeText(activity, "게시글 등록 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "게시글이 정상적으로 등록되었습니다", Toast.LENGTH_SHORT).show();
                activity.replacefragment(ManagerBoardFragment.newInstance(),activity.BACKPRESS_FINISH);
            }



        }
    }




}
