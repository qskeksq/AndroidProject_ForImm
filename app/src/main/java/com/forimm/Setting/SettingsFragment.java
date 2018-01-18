package com.forimm.Setting;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.forimm.R;


public class SettingsFragment extends Fragment {
    private ImageView imageView30;
    private ImageView imageView34;
    private ImageView imageView36;
    private ImageView imageView35;
    private ImageView imageView31;
    private TextView set1;
    private ImageView imageView16;
    private ImageView imageView33;
    private TextView set2;
    private TextView set4;
    private TextView set5;
    private TextView set6;
    private TextView set7;
    private ImageView imageView37;
    private TextView set3;
    private Toolbar toolbar;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView();
        setToolbar2();
        return view;
    }

    private void initView() {
        Typeface notosanbold = Typeface.createFromAsset(getContext().getAssets(), "fonts/NotoSans-Bold.ttf");
        set1 = (TextView) view.findViewById(R.id.set1);
        set2 = (TextView) view.findViewById(R.id.set2);
        set3 = (TextView) view.findViewById(R.id.set3);
        set4 = (TextView) view.findViewById(R.id.set4);
        set5 = (TextView) view.findViewById(R.id.set5);
        set6 = (TextView) view.findViewById(R.id.set6);
        set7 = (TextView) view.findViewById(R.id.set7);
        set1.setTypeface(notosanbold);
        set2.setTypeface(notosanbold);
        set3.setTypeface(notosanbold);
        set4.setTypeface(notosanbold);
        set5.setTypeface(notosanbold);
        set6.setTypeface(notosanbold);
        set7.setTypeface(notosanbold);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar2);
    }

    private void setToolbar2() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.mainBackground));
        toolbar.setTitle("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_settings:
                break;
        }
        return true;
    }
}
