package com.forimm.Main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.forimm.Email.EmailFragment;
import com.forimm.Faq.FaqFragment;
import com.forimm.Law.LawFragment;
import com.forimm.Map.main.MapFragment;
import com.forimm.R;
import com.forimm.Setting.SettingsFragment;
import com.forimm.Util.CustomTab;
import com.google.android.gms.maps.model.LatLng;


/**
 * 액티비티를 적절히 이용했다면 크게 생명주기 관리해 줄 필요 없는데 전부 프래그먼트이고 static 이 중간에 사용되기 때문에 생명주기를 반드시 관리해줘야 한다.
 */
public class ContainerFragment extends Fragment implements View.OnClickListener {

    int position;
    View view;
    MapFragment mapFragment;
    LawFragment lawFragment;
    FaqFragment faqFragment;
    EmailFragment emailFragment;
    SettingsFragment settingsFragment;
    CustomTab customTab;
    ImageView goMap, goLaw, goFaq, goEmail, settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_container, container, false);
            initFragment();
            setViews();
            setListener();
        }
        switchFragment(position);
        return view;
    }

    public void initFragment() {
        mapFragment = new MapFragment();
        lawFragment = new LawFragment();
        faqFragment = new FaqFragment();
        emailFragment = new EmailFragment();
        settingsFragment = new SettingsFragment();
    }

    public void setFragmentPosition(int position) {
        this.position = position;
    }

    public void setViews() {
        customTab = (CustomTab) view.findViewById(R.id.customTab);
        goMap = (ImageView) view.findViewById(R.id.customMapImg);
        goLaw = (ImageView) view.findViewById(R.id.customLawImg);
        goFaq = (ImageView) view.findViewById(R.id.customFaqImg);
        goEmail = (ImageView) view.findViewById(R.id.customMailImg);
        settings = (ImageView) view.findViewById(R.id.customSettingsImg);
    }

    public void setListener() {
        goMap.setOnClickListener(this);
        goLaw.setOnClickListener(this);
        goFaq.setOnClickListener(this);
        goEmail.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    /**
     * 컨테이너에 프래그먼트 장착
     */
    public void callFragment(final Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.subFragmentContainer, fragment)
                .commit();
    }

    /**
     * 메인에서 버튼을 누를 떄 세부 프래그먼트 띄우기
     * callFragment 와 callFragmentByAnimation 을 분리해 준 이유는 처음 containerFragment 가 애니메이션으로 뜰 때 안에서는 애니메이션으로 뜨면 안 되기 때문
     */
    public void switchFragment(int position) {
        switch (position) {
            case 0:
                callFragment(mapFragment);
                goMap.setImageResource(R.drawable.navi_center_selected);
                break;
            case 1:
                callFragment(lawFragment);
                goLaw.setImageResource(R.drawable.navi_law_selected);
                break;
            case 2:
                callFragment(faqFragment);
                goFaq.setImageResource(R.drawable.navi_faq_selected);
                break;
            case 3:
                callFragment(emailFragment);
                goEmail.setImageResource(R.drawable.navi_email_selected);
                break;
            case 4:
                callFragment(settingsFragment);
                settings.setImageResource(R.drawable.navi_settings_selected);
                break;
        }
    }

    /**
     * 탭을 눌렀을 때 프래그먼트 변경
     */
    @Override
    public void onClick(View v) {
        // 탭을 unselected 상태로 교체
        initImg();
        switch (v.getId()) {
            case R.id.customMapImg:
                // 안쪽 프래그먼트가 움직일 떄 따로 애니메이션으로 움직이도록 해 준다.
                callFragment(mapFragment);
                // 이미지를 선택된 이미지로 바꿔준다.
                goMap.setImageResource(R.drawable.navi_center_selected);
                break;
            case R.id.customLawImg:
                callFragment(lawFragment);
                goLaw.setImageResource(R.drawable.navi_law_selected);
                break;
            case R.id.customFaqImg:
                callFragment(faqFragment);
                goFaq.setImageResource(R.drawable.navi_faq_selected);
                break;
            case R.id.customMailImg:
                callFragment(emailFragment);
                goEmail.setImageResource(R.drawable.navi_email_selected);
                break;
            case R.id.customSettingsImg:
                callFragment(settingsFragment);
                settings.setImageResource(R.drawable.navi_settings_selected);
                break;
        }
    }

    /**
     * 탭 unselected 로 초기화 -> 커스텀뷰로 규칙을 따로 설정
     */
    public void initImg() {
        goMap.setImageResource(R.drawable.navi_center);
        goLaw.setImageResource(R.drawable.navi_law);
        goFaq.setImageResource(R.drawable.navi_faq);
        goEmail.setImageResource(R.drawable.navi_email);
        settings.setImageResource(R.drawable.navi_settings);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initImg();
    }

    /**
     * 프래그먼트 통신 중간과정
     */
    public void callMap(LatLng latLng, int zoom) {
        mapFragment.moveLocal(latLng, zoom);
    }

    public void movePager(String title) {
        mapFragment.movePagerPage(title);
    }

    public void callMail() {
        initImg();
        callFragment(emailFragment);
        goEmail.setImageResource(R.drawable.navi_email_selected);
    }

    public void setLawContent(String law) {
        emailFragment.setLawContent(law);
    }

    public void setCenterByCurPos() {
        mapFragment.findCenterByCurPosition();
    }
}
