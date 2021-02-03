package com.webproject.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.webproject.food.main.recipes.BoardRecipesAdapter;
import com.webproject.food.main.recipes.RecipesFragment;
import com.webproject.food.vo.BoardVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class BoardInfoActivity extends AppCompatActivity {

    ImageView board_like;
    String idx;
    boolean check = false;
    TextView tv_like, tv_m_name, tv_title, tv_content;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_info);

        board_like = findViewById(R.id.board_like);
        tv_like = findViewById(R.id.tv_like);
        tv_m_name = findViewById(R.id.tv_m_name);
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        img_back = findViewById(R.id.img_back);

        board_like.setOnClickListener(click);
        img_back.setOnClickListener(click);

        //파라미터값 받기
        Intent i = getIntent();
        idx = i.getStringExtra("idx");
        String result = "idx="+idx;

        //서버에 접속해서 데이터 가져오기
        new GetBoardView().execute(result, "type_board");

    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.board_like:
                    String result = "idx="+idx;

                    if(check == false){
                        check = true;
                        board_like.setImageResource(R.drawable.ic_baseline_star_24);
                        //서버에 접속해서 데이터 가져오기
                        new GetBoardLike().execute(result, "type_board_like");
                    }else if(check == true){
                        check = false;
                        board_like.setImageResource(R.drawable.ic_baseline_star_border_24);
                        //서버에 접속해서 데이터 가져오기
                        new GetBoardLike().execute(result, "type_board_nolike");
                    }
                    break;

                case R.id.img_back:
                    finish();
                    break;
            }

        }
    };

    //좋아요 데이터업데이트트 가져오기
    class GetBoardLike extends AsyncTask<String, Void, JSONArray> {

        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/Recipe_Project/board/boardinfo_like.jsp";//연결할 jsp의 주소

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

                    Log.i("TEST", "receiveMsg" + receiveMsg );

                    // 일반 JSON타입은 JSONOBJECT클래스가 담당
                    // JSON 배열타입은 JSONARRAY클래스가 담당
                    // 배열안에 있는 데이터를 가져올떄는 JSONOBJECT, 배열을 가져올때는 JSONARRAY
                    jarray = new JSONObject(receiveMsg).getJSONArray("res");
                    Log.i("TEST", "jarray" + jarray );

                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return jarray;

        }

        @Override
        protected void onPostExecute(JSONArray jarray) {

            JSONObject jsonObject = null;
            try {
                jsonObject = jarray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BoardVO vo = new BoardVO();
            vo.setLike(jsonObject.optString("like"));
            //정보 업데이트
            Log.i("TEST", "like : " + vo.getLike());
            tv_like.setText(vo.getLike());

        }
    }

    //선택 데이터리스트 가져오기
    class GetBoardView extends AsyncTask<String, Void, JSONArray> {

        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/Recipe_Project/board/boardinfo.jsp";//연결할 jsp의 주소

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

                    Log.i("TEST", "receiveMsg" + receiveMsg );

                    // 일반 JSON타입은 JSONOBJECT클래스가 담당
                    // JSON 배열타입은 JSONARRAY클래스가 담당
                    // 배열안에 있는 데이터를 가져올떄는 JSONOBJECT, 배열을 가져올때는 JSONARRAY
                    jarray = new JSONObject(receiveMsg).getJSONArray("res");
                    Log.i("TEST", "jarray" + jarray );

                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return jarray;

        }

        @Override
        protected void onPostExecute(JSONArray jarray) {

            JSONObject jsonObject = null;
            try {
                jsonObject = jarray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BoardVO vo = new BoardVO();
            vo.setIdx(jsonObject.optString("idx"));
            vo.setTitle(jsonObject.optString("title"));
            vo.setContent(jsonObject.optString("content"));
            vo.setM_name(jsonObject.optString("id"));
            vo.setDate(jsonObject.optString("date"));
            vo.setLike(jsonObject.optString("like"));

            //셋팅하기
            tv_like.setText(vo.getLike());
            tv_m_name.setText(vo.getM_name());
            tv_title.setText(vo.getTitle());
            tv_content.setText(vo.getContent());

        }
    }

}