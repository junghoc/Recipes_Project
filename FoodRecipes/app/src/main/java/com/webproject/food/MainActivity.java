package com.webproject.food;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView toolbarbottom;
    int num = 3;
    //스테틱 액티비티 선언(다른 엑티비티에서 메인 액티비티를 끄기위해)
    public static Activity main_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //엑티비티 선언
        main_activity = MainActivity.this;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_mypage, R.id.nav_notice, R.id.nav_logout, R.id.nav_recipes, R.id.nav_admin_page)
                .setDrawerLayout(drawer)
                .build();
        //해당 fragment의 layout을 컨트롤러에 저장
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //메뉴아이콘 클릭시 drawer을 열어준다
        ImageView drawer_menu = findViewById(R.id.drawer_menu);
        drawer_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(navigationView);
            }
        });
        //해당 fragment로 layout을 구현
        NavigationUI.setupWithNavController(navigationView, navController);

        //xml 파일에서 넣어놨던 header 선언
        View header = navigationView.getHeaderView(0);
        //header에 있는 리소스 가져오기
        TextView userName = header.findViewById(R.id.userName);
        TextView userEmail = header.findViewById(R.id.userEmail);
        Button header_btn_login = header.findViewById(R.id.header_btn_login);

        //로그인 화면 전환
        header_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent 또는 번들로 페이지 전환
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        LinearLayout header_linear_login = header.findViewById(R.id.header_linear_login);
        LinearLayout header_linear_nologin = header.findViewById(R.id.header_linear_nologin);

        //하단바
        toolbarbottom = findViewById(R.id.toolbarbottom);

        //해당 fragment로 layout을 구현
        NavigationUI.setupWithNavController(toolbarbottom, navController);

        //로그인 되있지는 sharedpreference로 확인
        SharedPreferences login = getSharedPreferences("vo", MODE_PRIVATE);

        //메뉴 item검색
        Menu menu = navigationView.getMenu();
        MenuItem drawer_admin = menu.findItem(R.id.drawer_admin);
        MenuItem drawer_logout = menu.findItem(R.id.drawer_logout);
        //어드민이 아닐경우 관리자 페이지가는 경로 숨기기
        int mgr = login.getInt("mgr",0);
        if(mgr == 1){
            //관리자로그인
            drawer_admin.setVisible(true);
        }else {
            //비 로그인 또는 일반회원 로그인
            drawer_admin.setVisible(false);
        }

        //쉐어드안에있는 id를 검색해서, value에 값이 없는경우 뒤의""값으로 대체한다
        String id = login.getString("id","");
        String name = login.getString("name", "");
        String email = login.getString("email", "");
        if( id.equals("") ){
            //로그인이 되지않은경우
            header_linear_login.setVisibility(View.GONE);
            header_linear_nologin.setVisibility(View.VISIBLE);
            drawer_logout.setVisible(false);
        }else {
            //로그인인경우
            header_linear_login.setVisibility(View.VISIBLE);
            header_linear_nologin.setVisibility(View.GONE);
            drawer_logout.setVisible(true);
            userEmail.setText(email);
            userName.setText(name);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        if( num != 3 ){
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();

            //핸들러 호출
            handler.sendEmptyMessage(0);

        }

    }//onBackPressed()

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            //1초간격으로 반복하며 3초를 카운팅한다
            //3초가 지나면 핸들러는 정지하고 num을 다시 3으로 초기화
            handler.sendEmptyMessageDelayed(0, 1000);
            if( num > 0 ){
                --num;
            }else {
                num = 3;
                handler.removeMessages(0);//핸들러 정지
            }

        }
    };

}