package com.kosta148.matjo.view;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosta148.matjo.R;



import java.io.IOException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Toolbar toolbar;
    SearchView mSearchView;
    Toast t;
    MainFragment mainFragment;
    FragmentManager fm;
    FloatingActionButton fab;
    Context context;
    ActionBarDrawerToggle toggle;
    ImageView ivLogo;
    TextView tvLogout;
    TextView tvUserId;
    NotiFragment notiFragment = new NotiFragment();
    DrawerLayout drawer;
    int pageNo = 1; // 검색 요청 페이지 번호
    boolean isUseLoc = false;

    boolean autoSearched = false;

    // 모임 검색 필터
    String searchType = "";

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SHaredPreferences 키 상수
    private static final String SHAREDPREFERENCES_LOGIN_ID = "LoginId";
    private static final String SHAREDPREFERENCES_LOGIN_PW = "LoginPassword";
    private static final String SHAREDPREFERENCES_LOGIN_AUTO = "AutoLogin";
    private static final String SHAREDPREFERENCES_MEMBER_NAME = "memberName";
    private static final String SHAREDPREFERENCES_TOKEN = "memberToken";

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private boolean notiVisiblity = false;

    Animation fabAnimShrink;
    Animation fabAnimInflate;
    Animation fragmentShrink;
    Animation fragmentInflate;
    Animation animFade;

    NavigationView navigationLeftView;
    NavigationView navigationRightView;

    LocationManager lmPassive; // 수동적 위치관리자
    LocationManager lmNetwork; // GPS 위치관리자

    double LAT_PASSIVE = 0.0f;
    double LON_PASSIVE = 0.0f;
    double LAT_NET = 0.0f;
    double LON_NET = 0.0f;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();


        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);

        // LocationManager 초기화
        lmPassive = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lmNetwork = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(getApplicationContext());

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_weather);
        fm = getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        ivLogo = (ImageView) findViewById(R.id.toolbar_logo_iv);

        mSearchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        mSearchView.setTag("mSearchView"); // OnFocusChangeListener 에서 mSearchView 를 구별하기위해 태그 등록
        mSearchView.setOnQueryTextListener(mQueryTextListener);
        mSearchView.setOnQueryTextFocusChangeListener(mFocusChangeListener);
        mSearchView.setVisibility(View.INVISIBLE);

//        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        toolbarTitle.setText("지역을 추가해주세요");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.ic_notifications_white_36dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                animateFloatingActionButton();
                toggleFragment();
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

        navigationLeftView = (NavigationView) findViewById(R.id.nav_left_view);
        navigationLeftView.setNavigationItemSelectedListener(onNavItemSelectedListener);
        navigationRightView = (NavigationView) findViewById(R.id.nav_right_view);
        navigationRightView.setNavigationItemSelectedListener(onNavItemSelectedListener);

        // 액티비티에서 id 찾기 에러
        // navigationView 에서 id 찾기 에러
        // xml 상에서 headerLayout 속성을 통해 레이아웃을 불러오고 있는 점을 이용,
        // 네비뷰.getHeaderView(인덱스) ==> 이와 같은 형식으로 뷰를 가져와야 안에있는 id 를 찾을 수 있다.
        tvLogout = (TextView) navigationLeftView.getHeaderView(0).findViewById(R.id.tvLogout);
        tvUserId = (TextView) navigationLeftView.getHeaderView(0).findViewById(R.id.tvUserId);
        tvLogout.setOnClickListener(mClickListener);
        tvUserId.setText(sharedPreferences.getString(SHAREDPREFERENCES_MEMBER_NAME, "")+"님");

        fabAnimShrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink);
        fabAnimInflate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.inflate);
        fragmentShrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_fragment);
        fragmentInflate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.inflate_fragment);
        animFade = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);

        if (sharedPreferences != null) {
//            showToast("로그인 ID: " + sharedPreferences.getString(SHAREDPREFERENCES_LOGIN_ID, "") +
//                    "\n로그인 PW: " + sharedPreferences.getString(SHAREDPREFERENCES_LOGIN_PW, "") +
//                    "\n토큰: " + sharedPreferences.getString(SHAREDPREFERENCES_TOKEN, ""));
        }

        sendToken();
    } // end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        // 권한 체크
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            // 이 권한을 필요한 이유를 설명해야하는가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
            }
        } else {
            // 위치제공자 리스너등록
            lmPassive.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                    0,
                    0,
                    locationListenerPassive);
            lmNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0,
                    0,
                    locationListenerNetwork);
        }

    } // end of onResume

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // 권한 허가
//                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
//                } else {
//                    // 권한 거부
//                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
//                    showToast("위치 기반 검색기능을 이용하실 수 없습니다");
//                }
//                return;
//        } // end of switch
//    } // end of onRequestPermissionsResult()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search, menu); // onCreate 에서 이미 inflate 하였다.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        if (lmPassive != null) lmPassive.removeUpdates(locationListenerPassive);
        if (lmNetwork != null) lmNetwork.removeUpdates(locationListenerNetwork);
    }

    LocationListener locationListenerPassive = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            // 위치제공자에 의해 위치정보가 변경되었을 때 호출되는 콜백메서드
            LAT_PASSIVE = location.getLatitude();
            LON_PASSIVE = location.getLongitude();
            setGeoLocation(LAT_PASSIVE, LON_PASSIVE);
            Log.e("TEST", "PASSIVE - " + LAT_PASSIVE + ", " + LON_PASSIVE);
            if (!autoSearched) {
                if (LAT_NET != 0.0f) {
                    RestaListFragment rlf = (RestaListFragment) mainFragment.mainFragmentPagerAdapter.getItem(1);
                    rlf.searchResta("맛집", 1, LAT_NET + "," + LON_NET);
                    autoSearched = true;
                } else {
                    RestaListFragment rlf = (RestaListFragment) mainFragment.mainFragmentPagerAdapter.getItem(1);
                    rlf.searchResta("맛집", 1, LAT_PASSIVE + "," + LON_PASSIVE);
                    autoSearched = true;
                }
            } // 최초로 위치값 받아왔을때 그값에 기반해 '맛집' 키워드를 검색한다.

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 위치제공자에 의해 상태가 변경되었을 때 호출되는 콜백메서드
        }
        @Override
        public void onProviderEnabled(String provider) {
            // 위치제공자가 활성화 되었을 때 호출되는 콜백메서드
        }
        @Override
        public void onProviderDisabled(String provider) {
            // 위치제공자가 비활성화 되었을 때 호출되는 메서드
        }
    };

    LocationListener locationListenerNetwork = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            // 위치제공자에 의해 위치정보가 변경되었을 때 호출되는 콜백메서드
            LAT_NET = location.getLatitude();
            LON_NET = location.getLongitude();
            setGeoLocation(LAT_NET, LON_NET);
            Log.e("TEST", "NETWORK - " + LAT_NET + ", " + LON_NET);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 위치제공자에 의해 상태가 변경되었을 때 호출되는 콜백메서드
        }
        @Override
        public void onProviderEnabled(String provider) {
            // 위치제공자가 활성화 되었을 때 호출되는 콜백메서드
        }
        @Override
        public void onProviderDisabled(String provider) {
            // 위치제공자가 비활성화 되었을 때 호출되는 메서드
        }
    };

    Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.menu_filter) {
                drawer.openDrawer(GravityCompat.END);
            }
            return true;
        }
    };

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
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            handler.post(backKeyRun);
            if (!notiVisiblity) {
                handler.post(backKeyRun);
            } else {
                animateFloatingActionButton();
                toggleFragment();
            }
        }
    } // end of onBackPressed


    /**
     * Fab 클릭시 애니메이션을 실행하는 메서드
     */
    public void animateFloatingActionButton() {
        fabAnimShrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (notiVisiblity) { // 뉴스피드 화면일때의 아이콘 설정
                    fab.setImageResource(R.mipmap.ic_launcher);
                } else { // 뉴스피드 화면이 아닐때의 아이콘 설정
                    fab.setImageResource(R.mipmap.ic_notifications_white_36dp);
                }
                fab.startAnimation(fabAnimInflate);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        fab.startAnimation(fabAnimShrink);
    } // end ofanimateFloatingActionButton()


    /**
     * Fab 클릭시 애니메이션 동작 이후, 실제 프래그먼트 전환을 하기위한 메서드
     */
    public void toggleFragment() {
        FragmentTransaction tran = fm.beginTransaction();
        tran.setCustomAnimations(R.anim.inflate_fragment_300, R.anim.shrink_fragment, R.anim.inflate_fragment_300, R.anim.shrink_fragment);
        if (!notiVisiblity) { // 노티목록 화면이 아니라면
            tran.add(R.id.container, notiFragment); // 보여줘라
            tran.addToBackStack(null);
            notiVisiblity = true; // 상태변경
        } else { // 노티목록 화면이라면
            fm.popBackStack(); // 화면제거해라
            notiVisiblity = false;
        }
        tran.commit();
    }


    /**
     * SearchView 구현을 위한 OnQueryTextListener 객체생성
     */
    SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchText) {
            // 검색 동작 수행
            switch(mainFragment.currentPos) {
                case 1:
                    // 맛집 검색시 RestaListFragment로 query 값을 넘긴다
                    RestaListFragment rlf = (RestaListFragment) mainFragment.mainFragmentPagerAdapter.getItem(mainFragment.currentPos);
                    rlf.restaList.clear();
                    if (rlf.isUseLoc) rlf.searchResta(searchText, 1, LAT_NET+","+LON_NET);
                    else rlf.searchResta(searchText, 1);
                    break;
                case 2:
                    GroupListFragment glf = (GroupListFragment) mainFragment.mainFragmentPagerAdapter.getItem(mainFragment.currentPos);
                    // 모임 검색 시 필터링 추가
                    if (searchType == null || searchType.equals("")) {
                        searchType = "all";
                    }
                    glf.searchGroup(searchText, searchType, 1);
                    break;
            }
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

                    if (mainFragment.currentPos == 2) {
                        // 내모임 목록을 다시 조회
                        GroupListFragment glf = (GroupListFragment) mainFragment.mainFragmentPagerAdapter.getItem(mainFragment.currentPos);
                        String memberNo = sharedPreferences.getString("memberNo", "");
                        glf.callMyGroup(memberNo);
                    }
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
                        Intent intent = new Intent(context, PromotionWebViewActivity.class );
                        startActivity(intent);
                    } else if (id == R.id.nav_rank) {
                        // TODO : 모임 랭킹 이동
                        Intent intent = new Intent(context, GroupRankViewActivity.class );
                        startActivity(intent);
                    } else if (id == R.id.nav_notice) {
                        // TODO : 공지 사항 창
                        Intent intent = new Intent(context, NoticeWebViewActivity.class );
                        startActivity(intent);
                    } else if (id == R.id.nav_inquiry) {
                        // TODO : 고객의 소리 창
                        Intent intent = new Intent(context,InquiryWebViewActivity.class );
                        startActivity(intent);
                    } else if (id == R.id.nav_setting) {
                        // TODO : 환경 설정 창
                        Intent intent = new Intent(context, SetupActivity.class );
                        startActivity(intent);
                    }

                    // 모임 검색 필터링 처리
                    if (id == R.id.nav_all) {
                        searchType = "all";
                    } else if (id == R.id.nav_group_name) {
                        searchType = "group_name";
                    } else if (id == R.id.nav_group_leader) {
                        searchType = "group_leader";
                    } else if (id == R.id.nav_group_content) {
                        searchType = "group_content";
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

    public void setGeoLocation(double lat, double lon) {
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(lat, lon, 10);
            if (list != null && (list.size()>=0) ) {
                Address addr = list.get(0);
                    RestaListFragment rlf = (RestaListFragment) mainFragment.mainFragmentPagerAdapter
                            .getItem(1);
                    rlf.tvLocation.setText(addr.getAddressLine(0));
                    Log.e("TEST", addr.getAddressLine(0));
            } // end of if
        } catch (IOException e) {
            // 위치 받아오지 못했을 시 처리
        } // try~catch
    } // end of getGeoLocation()

    public void sendToken() {
        Log.e("TEST", "토큰 보내기 시작");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/push/updatePushToken.do"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "response : " + response);
//                showToast(response);
                // 응답을 처리한다.

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();
                String result = rootObj.get("result").getAsString();
                String resultMsg = rootObj.get("resultMsg").getAsString();

                Gson gson = new Gson();

                // TODO 파싱

                if ( (result != null) && ("fail".equals(result)) ) {
                    showToast("null 또는 fail: " + resultMsg);
                } // 결과 메시지 토스트
            } // end of onResponse()
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyLog", "error : " + error);
                final VolleyError err = error;
//                showToast("결과: " + err.getMessage());
                Log.e("TEST", "에러메시지 : " + err.getMessage());

            } // end of onErrorResponse()
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String LoginId = sharedPreferences.getString("LoginId", "");
                String memberToken = sharedPreferences.getString("memberToken", "");

                Map<String, String> params = new HashMap<>();
                params.put("memberId", LoginId);
                params.put("memberToken", memberToken);
                return params;
            }
        };
        Log.e("TEST", "토큰 전송 전");
        requestQueue.add(stringRequest);
        Log.e("TEST", "토큰 전송 후");
        Log.e("TEST", "토큰 보내기 종료");
    } // end of sendToken

} // end of class