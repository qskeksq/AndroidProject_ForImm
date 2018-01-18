package com.forimm.Law;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.forimm.Main.MainActivity;
import com.forimm.R;
import com.forimm.domain.Law.LawDao;
import com.forimm.domain.Law.item.LawChild;
import com.forimm.domain.Law.item.LawParent;

import java.util.ArrayList;
import java.util.List;

/**
 * 노동법 법률을 보여주는 프래그먼트
 */
public class LawFragment extends Fragment {

    View view;
    SearchView searchView;
    LawAdapter lawAdapter;
    ExpandableListView listView;
    List<LawParent> data = new ArrayList<>();
    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 재사용성을 위해 null 일 경우에만 세팅해준다.
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_law, container, false);
            initViews();
            setListener();
            initData();
            initExpandableRecycler();
        }
        return view;
    }

    public void initViews() {
        listView = (ExpandableListView) view.findViewById(R.id.lawExpandable);
        searchView = (SearchView) view.findViewById(R.id.lawSearch);
    }

    public void setListener() {
        searchView.setOnQueryTextListener(queryListener);
        searchView.setOnCloseListener(closeListener);
    }

    /**
     * 서치뷰에 입력 되었을 때 리스너
     */
    SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            lawAdapter.filter(newText);
            expandAll();
            return false;
        }
    };

    /**
     * 서치뷰가 닫혔을 때 리스너너
     */
    SearchView.OnCloseListener closeListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            lawAdapter.filter("");
            closeAll();
            return false;
        }
    };

    public void initData() {
        List<LawChild> childList1 = LawDao.getInstance(getActivity()).getLaws("1부");
        LawParent parent1 = new LawParent("1부", childList1);
        data.add(parent1);

        List<LawChild> childList2 = LawDao.getInstance(getActivity()).getLaws("2부");
        LawParent parent2 = new LawParent("2부", childList2);
        data.add(parent2);

        List<LawChild> childList3 = LawDao.getInstance(getActivity()).getLaws("3부");
        LawParent parent3 = new LawParent("3부", childList3);
        data.add(parent3);

        List<LawChild> childList4 = LawDao.getInstance(getActivity()).getLaws("4부");
        LawParent parent4 = new LawParent("4부", childList4);
        data.add(parent4);
    }

    private void initExpandableRecycler() {
        lawAdapter = new LawAdapter(data, activity);
        listView.setAdapter(lawAdapter);
        listView.setGroupIndicator(null);
    }

    public void expandAll() {
        int count = lawAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            listView.expandGroup(i);
        }
    }

    public void closeAll() {
        int count = lawAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            listView.collapseGroup(i);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }
}