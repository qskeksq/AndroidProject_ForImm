package com.forimm.Map.list;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.forimm.R;
import com.forimm.domain.Center.Center;
import com.forimm.domain.Center.CenterDao;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class MapListFragment extends Fragment {

    RecyclerView recyclerView;
    List<Center> centerList;
    MapListAdapter adapter;
    TextView listRegion;
    ImageView backto;
    SearchView searchView;
    CallMapListBack callMapListBack;
    View view;

    public static MapListFragment newInstance(String region) {
        MapListFragment mapListFragment = new MapListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("region", region);
        mapListFragment.setArguments(bundle);
        return mapListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_list, container, false);
        initView();
        initData();
        setRecyclerView();
        setListener();
        return view;
    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.centerRecycler);
        listRegion = (TextView) view.findViewById(R.id.listRegion);
        backto = (ImageView) view.findViewById(R.id.backtopager);
        searchView = (SearchView) view.findViewById(R.id.searchView2);
    }

    private void initData() {
        String region = getArguments().getString("region");
        centerList = CenterDao.getInstance(getActivity()).getRegions(region);
        listRegion.setText(region);
    }

    private void setRecyclerView() {
        adapter = new MapListAdapter(centerList, callMapListBack, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setListener() {
        backto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    public interface CallMapListBack {
        void callMap(LatLng latLng, int zoom);

        void movePager(String title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callMapListBack = (CallMapListBack) context;
    }
}
