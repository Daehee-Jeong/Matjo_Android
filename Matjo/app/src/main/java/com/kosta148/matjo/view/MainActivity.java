package com.kosta148.matjo.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosta148.matjo.R;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Toolbar toolbar;
    SearchView mSearchView;
    TextView toolbarTitle;
    Toast t;
    MainFragment mainFragment;
    FragmentManager fm;
    FloatingActionButton fab;
    Context context;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    ImageView ivLogo;

    TextView tvLogout;

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SHaredPreferences 키 상수
    private static final String SHAREDPREFERENCES_LOGIN_ID = "LoginId";
    private static final String SHAREDPREFERENCES_LOGIN_PW = "LoginPassword";
    private static final String SHAREDPREFERENCES_LOGIN_AUTO = "AutoLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_weather);
        fm = getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search);
        ivLogo = (ImageView) findViewById(R.id.toolbar_logo_iv);

        mSearchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        mSearchView.setTag("mSearchView"); // OnFocusChangeListener 에서 mSearchView 를 구별하기위해 태그 등록
        mSearchView.setOnQueryTextListener(mQueryTextListener);
        mSearchView.setOnQueryTextFocusChangeListener(mFocusChangeListener);

//        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        toolbarTitle.setText("지역을 추가해주세요");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_dialog_email);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog(1);
                return false;
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationLeftView = (NavigationView) findViewById(R.id.nav_left_view);
        navigationLeftView.setNavigationItemSelectedListener(onNavItemSelectedListener);
        NavigationView navigationRightView = (NavigationView) findViewById(R.id.nav_right_view);
        navigationRightView.setNavigationItemSelectedListener(onNavItemSelectedListener);

        // 액티비티에서 id 찾기 에러
        // navigationView 에서 id 찾기 에러
        // xml 상에서 headerLayout 속성을 통해 레이아웃을 불러오고 있는 점을 이용,
        // 네비뷰.getHeaderView(인덱스) ==> 이와 같은 형식으로 뷰를 가져와야 안에있는 id 를 찾을 수 있다.
        tvLogout = (TextView) navigationLeftView.getHeaderView(0).findViewById(R.id.tvLogout);
        Log.e("아이디", tvLogout + "");
        tvLogout.setOnClickListener(mClickListener);

    } // end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences != null) {
            showToast("로그인 ID: " + sharedPreferences.getString(SHAREDPREFERENCES_LOGIN_ID, "") +
                      "로그인 PW: " + sharedPreferences.getString(SHAREDPREFERENCES_LOGIN_PW, ""));
        }
    } // end of onResume

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (/*id == R.id.action_settings*/ true) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String text) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(),
                text,
                Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (t != null) t.cancel();
    }

    /* 뒤로가기 시 실제 핸들링 처리 부분 */
    Runnable backKeyRun = new Runnable() {
        int count = 0;
        @Override
        public void run() {
            if (count < 1) {
                showToast("뒤로가기를 한번 더 누르시면 종료됩니다");
                count++;
            } else {
                finish();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count = 0;
                }
            }, 2000);
        }
    };

    /* 백키 눌렀을 때 처리사항들 => UX 적으로 매우 중요 ! 우선순위 파악필요. */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            handler.post(backKeyRun);
//            if (isWeatherFragment) {
//                handler.post(backKeyRun);
//            } else {
//                animateFloatingActionButton();
//                toggleFragment();
//            }
        }
    } // end of onBackPressed

    /* SearchView 구현을 위한 OnQueryTextListener 객체생성 */
    SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            // 검색 동작 수행
            return false; // 정상적 처리 완료시 true 로 핸들링 완료를 표시한다.
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    /* SearchView 포커스 감지를 위한 리스너 객체 생성 */
    View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if ( "mSearchView".equals(String.valueOf(v.getTag())) ) {
                if (hasFocus) {
                    ivLogo.setVisibility(View.INVISIBLE);
                } else {
                    mSearchView.onActionViewCollapsed();
                    mSearchView.setIconified(true);
                    ivLogo.setVisibility(View.VISIBLE);
                }
            }
        } // end of onFocusChange
    };

    NavigationView.OnNavigationItemSelectedListener onNavItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // TODO : 홈 클릭 시 홈으로 이동 : default

            } else if (id == R.id.nav_mywalks) {
                // TODO : 나의 발자취 이동

            } else if (id == R.id.nav_promotion) {
                // TODO : 프로모션 이동

            } else if (id == R.id.nav_rank) {
                // TODO : 모임 랭킹 이동
            } else if (id == R.id.nav_inform) {
                // TODO : 공지 사항 창
            } else if (id == R.id.nav_setting) {
                // TODO : 환경 설정 창
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    /* UI 컴포넌트의 클릭이벤트 관리를 위한 클릭리스너 객체 생성 */
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.tvLogout:
                    // 로그아웃 시
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHAREDPREFERENCES_LOGIN_AUTO, false);
                    editor.putString(SHAREDPREFERENCES_LOGIN_ID, "");
                    editor.putString(SHAREDPREFERENCES_LOGIN_PW, "");
                    editor.commit(); // 변경사항을 저장하기

                    Intent intent = new Intent(getApplicationContext(), LoginFormActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            } // end of switch
        } // end of onClick()
    };
} // end of class
