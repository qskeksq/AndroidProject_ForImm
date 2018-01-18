package com.forimm.Main;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.forimm.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * 처음 보이는 화면. MainFragment 화면에서 언어선택과 페이지 선택이 이루어진다.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private ContainerFragment containerFragment;
    private LanguageAdapter adapter;
    private View view;
    private ImageView settings;
    private Button goMap, goLaw, goFaq, goEmail;
    private RecyclerView languageRecycler;
    private TextView mainTitle1, mainTitle2;
    private ImageView line4;
    private ImageView line3;
    private ImageView line2;
    private ImageView line1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        setListener();
        setLanguageRecycler();
        initContainerFragment();
        return view;
    }

    private void initView() {
        goMap = (Button) view.findViewById(R.id.goMap);
        goLaw = (Button) view.findViewById(R.id.goLaw);
        goFaq = (Button) view.findViewById(R.id.goFaq);
        goEmail = (Button) view.findViewById(R.id.goEmail);
        settings = (ImageView) view.findViewById(R.id.settings);
        mainTitle1 = (TextView) view.findViewById(R.id.mainTitle1);
        mainTitle2 = (TextView) view.findViewById(R.id.mainTitle2);
        languageRecycler = (RecyclerView) view.findViewById(R.id.languageRecycler);
        setTypeFace();
        line4 = (ImageView) view.findViewById(R.id.line4);
        line3 = (ImageView) view.findViewById(R.id.line3);
        line2 = (ImageView) view.findViewById(R.id.line2);
        line1 = (ImageView) view.findViewById(R.id.line1);
    }

    private void setTypeFace() {
        Typeface notosans = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NotoSans-Regular.ttf");
        goMap.setTypeface(notosans);
        goLaw.setTypeface(notosans);
        goFaq.setTypeface(notosans);
        goEmail.setTypeface(notosans);
        Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        mainTitle1.setTypeface(roboto);
        mainTitle2.setTypeface(roboto);
    }

    private void setListener() {
        goMap.setOnClickListener(this);
        goLaw.setOnClickListener(this);
        goFaq.setOnClickListener(this);
        goEmail.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    public void setLanguageRecycler() {
        adapter = new LanguageAdapter(getContext(), this);
        languageRecycler.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        languageRecycler.setLayoutManager(manager);
    }

    public void initContainerFragment() {
        containerFragment = new ContainerFragment();
    }

    public void onClick(View view) {
        callFragment(containerFragment);
        switch (view.getId()) {
            case R.id.goMap:
                containerFragment.setFragmentPosition(0);
                break;
            case R.id.goLaw:
                containerFragment.setFragmentPosition(1);
                break;
            case R.id.goFaq:
                containerFragment.setFragmentPosition(2);
                break;
            case R.id.goEmail:
                containerFragment.setFragmentPosition(3);
                break;
            case R.id.settings:
                containerFragment.setFragmentPosition(4);
                break;
        }
    }

    public void callFragment(final Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void refreshFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    private void setUnSelected() {
        line1.setImageResource(R.drawable.line_unselected);
        line2.setImageResource(R.drawable.line_unselected);
        line3.setImageResource(R.drawable.line_unselected);
        line4.setImageResource(R.drawable.line_unselected);
    }

    private void mapSelected() {
        line1.setImageResource(R.drawable.line_selected);
        line2.setImageResource(R.drawable.line_selected);
    }

    private void lawSelected() {
        line2.setImageResource(R.drawable.line_selected);
        line3.setImageResource(R.drawable.line_selected);
    }

    private void faqSelected() {
        line3.setImageResource(R.drawable.line_selected);
        line4.setImageResource(R.drawable.line_selected);
    }

    private void emailSelected() {
        line4.setImageResource(R.drawable.line_selected);
    }

    @Override
    public void onPause() {
        super.onPause();
        setUnSelected();
    }

    /**
     * 프래그먼트 통신 중간 과정
     */
    public void callContainer(LatLng latLng, int zoom) {
        containerFragment.callMap(latLng, zoom);
    }

    public void movePager(String title) {
        containerFragment.movePager(title);
    }

    public void callEmail() {
        containerFragment.callMail();
    }

    public void setLawContent(String law) {
        containerFragment.setLawContent(law);
    }

    public void setCenterByCurPos() {
        containerFragment.setCenterByCurPos();
    }

}
