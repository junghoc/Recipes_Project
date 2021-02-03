package com.webproject.food.manager.board;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class ManagerBoardInfoFragment extends Fragment {

    ManagerActivity activity;
    View view;

    BoardVO vo = null;
    String idx;

    TextView idx_board, title_board, content_board, date_board, like_board, writer_board, status_board;
    Button pre_btn,  del_btn;

    public ManagerBoardInfoFragment(String idx){ this.idx = idx; }

    public static ManagerBoardInfoFragment newInstance(String idx){ return new ManagerBoardInfoFragment(idx); }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.manager_board_info, container, false);

        activity = ManagerActivity.getInstance();

        idx_board = view.findViewById(R.id.idx_board);
        title_board = view.findViewById(R.id.title_board);
        content_board = view.findViewById(R.id.content_board);
        date_board = view.findViewById(R.id.date_board);
        like_board = view.findViewById(R.id.like_board);
        writer_board = view.findViewById(R.id.writer_board);
        status_board = view.findViewById(R.id.status_board);

        pre_btn = view.findViewById(R.id.pre_btn);
        del_btn = view.findViewById(R.id.del_btn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new GetBoardVO().execute(idx);

        pre_btn.setOnClickListener(bottom_click);
        del_btn.setOnClickListener(bottom_click);
    }

    View.OnClickListener bottom_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.pre_btn :
                    activity.replacefragment(ManagerBoardFragment.newInstance(), activity.BACKPRESS_FINISH);
                    break;
                case R.id.del_btn :
                    // 개시글 삭제 버튼 누를시 경고창 생성
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("개시글 삭제");
                    builder.setMessage("정말로 삭제하시겠습니까?");
                    builder.setNegativeButton("아니오", null);
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new SetBoardStatus().execute(idx,"1");
                        }
                    });
                    builder.show();
                    break;
            }

        }
    };

    // 게시글 정보 조회
    class GetBoardVO extends AsyncTask<String, Void, JSONArray> {

        String ip = activity.getIp();
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/manager_board/manager_board_getcontext.jsp";

        @Override
        protected JSONArray doInBackground(String... strings) {
            JSONArray jarray = null;
            String param = "?idx=" + strings[0];
            serverip = serverip + param;

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
                vo.setContent(jsonObject.optString("content"));
                vo.setDate(jsonObject.optString("date"));
                vo.setLike(jsonObject.optString("like"));
                vo.setM_name(jsonObject.optString("writer"));
                vo.setStatus(jsonObject.optString("status"));

                // vo를 통해 내용 갱신
                idx_board.setText(vo.getIdx());
                title_board.setText(vo.getTitle());
                content_board.setText(vo.getContent());
                date_board.setText(vo.getDate());
                like_board.setText(vo.getLike());
                writer_board.setText(vo.getM_name());
                status_board.setText(vo.getStatus());
            }
        }
    }

    // 게시글 삭제 - > 서버전송
    class SetBoardStatus extends AsyncTask<String, Void, JSONArray> {

        String ip = activity.getIp();
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/manager_board/manager_board_setstatus.jsp";

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

                sendMsg = "idx=" + strings[0] + "&status=" + strings[1];
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
            if(jarray == null){
                Toast.makeText(activity, "개시글을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = null;

            try {
                jsonObject = jarray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = jsonObject.optString("result");

            if(result.equalsIgnoreCase("yes")) {
                Toast.makeText(activity, "성공적으로 갱신되었습니다", Toast.LENGTH_SHORT).show();
                activity.replacefragment(ManagerBoardFragment.newInstance(), activity.BACKPRESS_FINISH);
            }else{
                Toast.makeText(activity, "개시글 삭제에 실패했습니다", Toast.LENGTH_SHORT).show();
            }

        } // onPostExecute()

    } // SetBoardStatus
}
