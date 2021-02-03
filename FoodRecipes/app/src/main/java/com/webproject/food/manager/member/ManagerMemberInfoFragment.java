package com.webproject.food.manager.member;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManagerMemberInfoFragment extends Fragment {

    String idx; // member의 파라마터 idx

    View view; // ManagerMemberInfoFragment의 Fragment -> View

    public ManagerMemberInfoFragment(String idx){
        this.idx = idx;
    }

    TextView idx_member, id_member, pwd_member, name_member, gender_member, birth_member,
            phone_member, email_member, regdate_member, status_member, del_info_member;
    Button  member_edit_btn, member_fire_btn, member_del_btn, pre_btn;

    // member : TextView
    // edit : EditText
    // btn : Button
    // group : RadioGroup

    MemberVO vo = null; // onActivityCreated()에서 작성

    ManagerActivity activity; // Activity

    public static ManagerMemberInfoFragment newInstance(String idx){return new ManagerMemberInfoFragment(idx);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.manager_member_info, container, false);

        activity = ManagerActivity.getInstance();

        // member : findViewById
        idx_member = view.findViewById(R.id.idx_board);
        id_member = view.findViewById(R.id.title_board);
        pwd_member = view.findViewById(R.id.content_board);
        name_member = view.findViewById(R.id.date_board);
        gender_member = view.findViewById(R.id.like_board);
        birth_member = view.findViewById(R.id.writer_board);
        phone_member = view.findViewById(R.id.status_board);
        email_member = view.findViewById(R.id.email_member);
        regdate_member = view.findViewById(R.id.regdate_member);
        status_member = view.findViewById(R.id.status_member);
        del_info_member = view.findViewById(R.id.del_info_member);


        // btn : findViewById
        member_edit_btn = view.findViewById(R.id.move_btn);
        member_fire_btn = view.findViewById(R.id.member_fire_btn);
        member_del_btn = view.findViewById(R.id.del_btn);
        pre_btn = view.findViewById(R.id.pre_btn);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO : 넘겨받은 idx를 통해서 db의 데이터를 vo에 담는다

        new GetMemberVO().execute(idx);

        // 담아온 vo의 내용을 TextView로 보낸다
//        idx_member.setText(vo.getSeq_member_idx());
//        id_member.setText(vo.getId());
//        pwd_member.setText(vo.getPwd());
//        name_member.setText(vo.getName());
//        gender_member.setText(vo.getGender());
//        birth_member.setText(vo.getBirth());
//        phone_member.setText(vo.getPhone());
//        email_member.setText(vo.getEmail());
//        regdate_member.setText(vo.getReg_date());
//        status_member.setText(vo.getDel_info());

        // 버튼 기능 만들기
        pre_btn.setOnClickListener(bottom_click);
        member_edit_btn.setOnClickListener(bottom_click);
        member_fire_btn.setOnClickListener(bottom_click);
        member_del_btn.setOnClickListener(bottom_click);
    }

    View.OnClickListener bottom_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            switch (view.getId()){
                case R.id.pre_btn : // '이전' 버튼
                    // ManagerMemberFragment 페이지로 이동
                    activity.replacefragment(ManagerMemberFragment.newInstance(),activity.BACKPRESS_FINISH);
                    break;
                case R.id.move_btn: // '회원수정' 버튼
                    // 회원수정 버튼 클릭시 회원수정 페이지로 이동
                    activity.replacefragment(ManagerMemberEditFragment.newInstatnce(idx),activity.BACKPRESS_BLOCK);

                    break;
                case R.id.member_fire_btn : // '회원강퇴' 버튼
                    builder.setTitle("경고");
                    builder.setMessage("정말로 '회원강퇴'하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 회원강퇴 -> 네 클릭시
                            // TODO : 회원의 STATUS를 변경

                            activity.replacefragment(ManagerMemberFragment.newInstance(),activity.BACKPRESS_FINISH); // 회원수정 페이지로 이동
                        }
                    });
                    builder.setNegativeButton("아니오", null);
                    builder.show();
                    break;
                case R.id.del_btn: // '회원삭제' 버튼
                    builder.setTitle("경고");
                    builder.setMessage("정말로 '회원탈퇴'하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 회원탈퇴 -> 네 클릭시
                            // TODO : 회원의 STATUS를 변경

                            activity.replacefragment(ManagerMemberFragment.newInstance(),activity.BACKPRESS_FINISH); // 회원수정 페이지로 이동
                        }
                    });
                    builder.setNegativeButton("아니오", null);
                    builder.show();
                    break;
            }

        }
    };

    class GetMemberVO extends AsyncTask<String, Void, JSONArray> {

        String ip = activity.getIp();
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/Recipe_Project/manager_member/manager_member_selectOne.jsp";

        @Override
        protected JSONArray doInBackground(String... strings) {
            JSONArray jarray = null;

            String param = "?seq_member_idx=" + strings[0];
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

                MemberVO vo = new MemberVO();


                vo.setIdx(jsonObject.optString("member_idx"));
                vo.setId(jsonObject.optString("id"));
                vo.setPwd(jsonObject.optString("pwd"));
                vo.setName(jsonObject.optString("name"));
                vo.setGender(jsonObject.optString("gender"));
                vo.setBirth(jsonObject.optString("birth"));
                vo.setPhone(jsonObject.optString("phone"));
                vo.setEmail(jsonObject.optString("email"));
                vo.setRegdate(jsonObject.optString("reg_date"));
                vo.setMrg(jsonObject.optString("mgr"));
                vo.setStatus(jsonObject.optString("del_info"));

                // vo를 통해 내용 갱신
                idx_member.setText(vo.getIdx());
                id_member.setText(vo.getId());
                pwd_member.setText(vo.getPwd());
                name_member.setText(vo.getName());
                gender_member.setText(vo.getName());
                birth_member.setText(vo.getBirth());
                phone_member.setText(vo.getPhone());
                email_member.setText(vo.getEmail());
                regdate_member.setText(vo.getRegdate());
                del_info_member.setText(vo.getStatus());


            }
        }
    }

    // 게시글 삭제 - > 서버전송
//    class SetBoardStatus extends AsyncTask<String, Void, JSONArray> {
//
//        String ip = activity.getIp();
//        String sendMsg, receiveMsg;
//        String serverip = "http://"+ip+":9090/AndJSPTest/manager_board/manager_board_setstatus.jsp";
//
//        @Override
//        protected JSONArray doInBackground(String... strings) {
//            JSONArray jarray = null;
//
//            // 연결할 주소로 접근
//            try {
//                String str = "";
//
//                // 전송할 서버 지정 및 준비
//                URL url = new URL(serverip);
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//
//
//                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//
//                sendMsg = "idx=" + strings[0] + "&status=" + strings[1];
//                osw.write(sendMsg);
//                osw.flush();
//
//                // 서버로 전송이 완료되면 서버측(jsp)에서 처리한 결과를 되받아야한다.
//                if(conn.getResponseCode()==conn.HTTP_OK){ // 200이면 정상, 404,500은 비정상
//
//                    // TMP에 해당 내용을 받는다.
//                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
//                    // READER가 서버로부터 결과 데이터를 가지고있으므로 이를 BufferedReader를 통해서 한줄 단위로 가져온다.
//                    BufferedReader reader = new BufferedReader(tmp);
//
//                    // {'result':'%s'}}
//                    StringBuffer buffer = new StringBuffer();
//                    while ( (str=reader.readLine())!=null ){
//                        buffer.append(str);
//                    }
//
//                    receiveMsg = buffer.toString();
//
//                    // 일반 JSON타입은 JSONOBJECT클래스가 담당
//                    // JSON 배열타입은 JSONARRAY클래스가 담당
//                    // 배열안에 있는 데이터를 가져올떄는 JSONOBJECT, 배열을 가져올때는 JSONARRAY
//                    jarray = new JSONObject(receiveMsg).getJSONArray("res");
//
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            return jarray;
//
//        }
//
//        @Override
//        protected void onPostExecute(JSONArray jarray) {
//            if(jarray == null){
//                Toast.makeText(activity, "개시글을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            JSONObject jsonObject = null;
//
//            try {
//                jsonObject = jarray.getJSONObject(0);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            String result = jsonObject.optString("result");
//
//            if(result.equalsIgnoreCase("yes")) {
//                Toast.makeText(activity, "성공적으로 갱신되었습니다", Toast.LENGTH_SHORT).show();
//                activity.replacefragment(ManagerBoardFragment.newInstance(), activity.BACKPRESS_FINISH);
//            }else{
//                Toast.makeText(activity, "개시글 삭제에 실패했습니다", Toast.LENGTH_SHORT).show();
//            }
//
//        } // onPostExecute()
//
//    } // SetBoardStatus

}
