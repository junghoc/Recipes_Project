package com.webproject.food.main.home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webproject.food.R;
import com.webproject.food.main.recipes.RecipesFragment;
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

public class HomeFragment extends Fragment {

    ViewFlipper v_flipper;
    Context ct;
    float startX;
    TextView home_rec_more;
    ArrayList<BoardVO> items;
    RecyclerView recycler_view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ct = container.getContext();
        items = new ArrayList<>();

        //이미지
        int images[] = {
                R.drawable.a_search,
                R.drawable.b_search,
                R.drawable.happy
        };
        v_flipper = root.findViewById(R.id.image_slide);
        for(int image : images){
            fllipperImages(image);
        }

        //스와이프.. (왼쪽으로만 보이게함 오른쪽으로 스와이프하면 이상하게 나옴..)
        v_flipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float endX = motionEvent.getX();
                        float endY = motionEvent.getY();

                        //swipe right
                        /*if (startX < endX) {
                            HomeFragment.this.v_flipper.showNext();
                        }*/

                        //swipe left
                        if (startX > endX) {
                            HomeFragment.this.v_flipper.showPrevious();
                        }

                        break;
                }

                return true;
            }
        });

        //인기 레시피(더보기)
        home_rec_more = root.findViewById(R.id.home_rec_more);
        //클릭이벤트
        home_rec_more.setOnClickListener(click);

        //서버에 접속해서 데이터 가져오기
        new GetListView().execute("");

        //리사이클
        recycler_view = root.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ct, LinearLayoutManager.HORIZONTAL, false);//좌우
        recycler_view.setLayoutManager(layoutManager);

        return root;
    }

    //데이터리스트 가져오기
    class GetListView extends AsyncTask<String, Void, JSONArray> {

        String ip = "192.168.219.101";
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/Recipe_Project/home/boardlist.jsp";//연결할 jsp의 주소

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
                vo.setLike(jsonObject.optString("like"));
                items.add(vo);
            }

            recycler_view.setAdapter(new HomeRecipesAdapter(items));

        }
    }

    //클릭이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.home_rec_more:
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new RecipesFragment()).commit();
                    break;
            }
        }
    };

    // 이미지 슬라이더 구현 메서드
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(ct);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);      // 이미지 추가
        v_flipper.setFlipInterval(4000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_flipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_flipper.setInAnimation(ct,R.anim.slide_in_right);
        v_flipper.setOutAnimation(ct,R.anim.slide_out_left);
    }

}