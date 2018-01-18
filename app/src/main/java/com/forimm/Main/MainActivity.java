package com.forimm.Main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.forimm.Map.list.MapListFragment;
import com.forimm.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * 현재 액티비티에서 모든 프래그먼트가 동작한다.
 */
public class MainActivity extends AppCompatActivity implements MapListFragment.CallMapListBack {

    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor();
        initMainFragment();
    }

    /**
     * init
     */
    private void setStatusBarColor() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    private void initMainFragment() {
        mainFragment = new MainFragment();
        callFragment(mainFragment);
    }

    public void callFragment(Fragment fragment) {
        // addToBackStack 하지 않았기 때문에 MainActivity 에 속한 하나의 뷰로 봐도 됨
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();
    }


    /**
     * 액티비티를 통한 프래그먼트 통신
     *
     * 1. 액티비티를 직접 가져다 사용하기
     * callEmail()
     * setLawContent()
     * setCenterByCurPos()
     *
     * 2. 인터페이스를 통한 통신
     * callMap()
     * movePager()
     */
    @Override
    public void callMap(LatLng latLng, int zoom) {
        mainFragment.callContainer(latLng, 14);
    }

    @Override
    public void movePager(String title) {
        mainFragment.movePager(title);
    }

    public void callEmail() {
        mainFragment.callEmail();
    }

    public void setLawContent(String law) {
        mainFragment.setLawContent(law);
    }

    public void setCenterByCurPos() {
        mainFragment.setCenterByCurPos();
    }

}
