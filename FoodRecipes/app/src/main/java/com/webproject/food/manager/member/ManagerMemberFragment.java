package com.webproject.food.manager.member;

import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ManagerMemberFragment extends Fragment {


    ManagerActivity activity = ManagerActivity.getInstance();

    public static ManagerMemberFragment newInstance() {
        return new ManagerMemberFragment();
    }

    TextView total_member, month_member;
    RadioGroup radiogroup;
    EditText id_edittext;
    Button search_btn;
    ListView member_listview;

    ManagerMemberAdapter adapter = null;
    ArrayList<MemberVO> arr = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_member_fragment, container, false);

        // findViewById
        total_member = view.findViewById(R.id.total_member);
        month_member = view.findViewById(R.id.month_member);
        radiogroup = view.findViewById(R.id.radiogroup);
        id_edittext = view.findViewById(R.id.id_edittext);
        search_btn = view.findViewById(R.id.search_btn);
        member_listview = view.findViewById(R.id.member_listview);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO : total_member 갱신
//        total_member.setText("");

        // TODO : month_member 갱신
//        month_member.setText("3");

        // TODO : 아이디 검색 기능
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        radiogroup.setOnCheckedChangeListener(listener);

        listener.onCheckedChanged(radiogroup, R.id.all_select);
    }

    // TODO : 그룹버튼 클릭시
    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            arr = null;
            switch (i){
                case R.id.all_select :
                    // TODO : 모든 회원의 정보를 가져온다

                    arr = new ArrayList<>();
                    break;
                case R.id.normal_select :
                    // TODO : 일반 회원의 정보를 가져온다

                    arr = new ArrayList<>();
                    break;
                case R.id.banned_select :
                    // TODO : 강퇴된 회원의 정보를 가져온다

                    arr = new ArrayList<>();
                    break;
                case R.id.withdrawal_select :
                    // TODO : 탈퇴한 회원의 정보를 가져온다

                    arr = new ArrayList<>();
                    break;
            }

            // 샘플데이터 : 추후 삭제
//            MemberVO vo = new MemberVO();
//            vo.setSeq_member_idx("0");
//            vo.setId("test_id");
//            vo.setDel_info("NORMAL");
//            vo.setName("테스트");
//            vo.setReg_date("01/01");
//            arr.add(vo);
            // ---

            adapter = new ManagerMemberAdapter(activity, R.layout.manager_member_form, arr, member_listview);

            member_listview.setAdapter(adapter);

            new GetListView().execute("");

        }
    };

    class GetListView extends AsyncTask<String, Void, JSONArray> {

        String ip = activity.getIp();
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/manager_member/manager_member_getlist.jsp";

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
            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jsonObject = null;

                try {
                    jsonObject = jarray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MemberVO vo = new MemberVO();
                vo.setIdx(jsonObject.optString("member_idx"));
                vo.setId(jsonObject.optString("id"));
                vo.setName(jsonObject.optString("name"));
                vo.setRegdate(jsonObject.optString("reg_date"));
//                vo.setDel_info(jsonObject.optString("del_info"));
                arr.add(vo);
            }
            adapter.notifyDataSetChanged();
        }
    }



}