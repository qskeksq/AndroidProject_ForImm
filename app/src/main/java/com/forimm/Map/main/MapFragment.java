package com.forimm.Map.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.forimm.Map.card.MapPagerAdapter;
import com.forimm.Map.list.MapListFragment;
import com.forimm.Map.search.MapSearchFragment;
import com.forimm.R;
import com.forimm.Util.CheckPermission;
import com.forimm.domain.Center.Center;
import com.forimm.domain.Center.CenterDao;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class MapFragment extends Fragment
        implements OnMapReadyCallback, CheckPermission.CallBack, View.OnClickListener
        , GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    View view;
    FragmentManager fragmentManager;
    SupportMapFragment mapFragment;
    RecyclerView recyclerView;
    ViewPager mapPager;
    MapAdapter adapter;
    MapPagerAdapter pagerAdapter;
    CircleIndicator indicator;
    GoogleMap googleMap;
    LinearLayoutManager manager;
    ImageView mapPagerHam, pagerDown, searchViewIcon;
    TextView searchViewText;
    ConstraintLayout pagerLayout, searchViewLayout;
    FloatingActionButton fab;
    String curRegion;
    List<List<Center>> data;
    LocationManager locationManager;
    ProgressBar progressBar;
    ConstraintLayout mapListContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        initView();
        setListener();
        setData();
        setRegionRecycler();
        initMap();
        return view;
    }

    public void initView() {
        fragmentManager = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.mapFragment);
        recyclerView = (RecyclerView) view.findViewById(R.id.mapRecycler);
        mapPager = (ViewPager) view.findViewById(R.id.mapPager);
        indicator = (CircleIndicator) view.findViewById(R.id.circleIndicator);
        pagerLayout = (ConstraintLayout) view.findViewById(R.id.pagerLayout);
        mapPagerHam = (ImageView) view.findViewById(R.id.mapPagerHam);
        pagerDown = (ImageView) view.findViewById(R.id.pagerDown);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        searchViewLayout = (ConstraintLayout) view.findViewById(R.id.searchViewLayout);
        searchViewIcon = (ImageView) view.findViewById(R.id.searchViewIcon);
        searchViewText = (TextView) view.findViewById(R.id.searchViewText);
        mapListContainer = (ConstraintLayout) view.findViewById(R.id.mapListContainer);
    }

    public void setListener() {
        mapPagerHam.setOnClickListener(this);
        searchViewLayout.setOnClickListener(this);
        pagerDown.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapPagerHam:
                callMapListFragment();
                break;
            case R.id.searchViewLayout:
                callSearchMapFragment();
                break;
            case R.id.pagerDown:
                reducePager();
                break;
            case R.id.fab:
                currentLocation();
                break;
        }
    }

    /**
     * 지역 선택 리사이클러 관리
     */
    public void setData() {
        data = new ArrayList<>();
        List<Center> busan = CenterDao.getInstance(getActivity()).getRegions("부산", R.drawable.busan, R.drawable.busan_selected);
        data.add(busan);
        List<Center> gyeongnam = CenterDao.getInstance(getActivity()).getRegions("경남", R.drawable.gyeongnam, R.drawable.gyeongnam_selected);
        data.add(gyeongnam);
        List<Center> ulsan = CenterDao.getInstance(getActivity()).getRegions("울산", R.drawable.ulsan, R.drawable.ulsan_selected);
        data.add(ulsan);
        List<Center> daegu = CenterDao.getInstance(getActivity()).getRegions("대구", R.drawable.daegu, R.drawable.daegu_selected);
        data.add(daegu);
        List<Center> gyeongbuk = CenterDao.getInstance(getActivity()).getRegions("경북", R.drawable.gyeongbuk, R.drawable.gyeongbuk_selected);
        data.add(gyeongbuk);
        List<Center> daejeon = CenterDao.getInstance(getActivity()).getRegions("대전", R.drawable.daejeon, R.drawable.daegu_selected);
        data.add(daejeon);
        List<Center> choongchung = CenterDao.getInstance(getActivity()).getRegions("충청", R.drawable.chungnam, R.drawable.chungnam_selected);
        data.add(choongchung);
        List<Center> gwangju = CenterDao.getInstance(getActivity()).getRegions("광주", R.drawable.gwangju, R.drawable.gwangju_selected);
        data.add(gwangju);
        List<Center> jeonnam = CenterDao.getInstance(getActivity()).getRegions("전남", R.drawable.jeonnam, R.drawable.jeonnam_selected);
        data.add(jeonnam);
        List<Center> incheon = CenterDao.getInstance(getActivity()).getRegions("인천", R.drawable.incheon, R.drawable.incheon_selected);
        data.add(incheon);
        List<Center> gyeonggi = CenterDao.getInstance(getActivity()).getRegions("경기", R.drawable.gyeonggi, R.drawable.gyeonggi_selected);
        data.add(gyeonggi);
        List<Center> seoul = CenterDao.getInstance(getActivity()).getRegions("서울", R.drawable.seoul, R.drawable.seoul_selected);
        data.add(seoul);
        List<Center> jeju = CenterDao.getInstance(getActivity()).getRegions("제주", R.drawable.jeju, R.drawable.jeju_selected);
        data.add(jeju);
    }

    private void callMapListFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down)
                .add(R.id.containerLayout, MapListFragment.newInstance(curRegion))
                .commit();
    }

    private void callSearchMapFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.containerLayout, new MapSearchFragment())
                .commit();
    }

    private void reducePager() {
        if (pagerLayout.getVisibility() == View.VISIBLE) {
            pagerLayout.setVisibility(View.GONE);
        }
    }

    private void currentLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        CheckPermission.checkVersion(MapFragment.this, perms);
    }

    // 지역 선택 리사이클러 세팅
    public void setRegionRecycler() {
        adapter = new MapAdapter(this, data, getContext());
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void callInit() {
        setCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPermission.onResult(requestCode, grantResults, this);
    }

    /**
     * 지도 관리
     */
    private void initMap() {
        mapFragment.getMapAsync(this);
    }

    // 지도 초기화 -- 서울로 해준다
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng seoul = new LatLng(37.5665350, 126.9779690);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 11));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (pagerLayout.getVisibility() == View.VISIBLE) {
            pagerLayout.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("현재 위치")) {
            marker.showInfoWindow();
        } else {
            List<Center> data = CenterDao.getInstance(getActivity()).getRegions(curRegion);
            int curpos = 0;
            LatLng latLng = null;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getName().equals(marker.getTitle())) {
                    curpos = i;
                    latLng = new LatLng(Double.parseDouble(data.get(i).getLat()), Double.parseDouble(data.get(i).getLng()));
                }
            }
            moveLocal(latLng, 10);

            pagerLayout.setVisibility(View.VISIBLE);
            mapPager.setCurrentItem(curpos);
            marker.showInfoWindow();
        }
        return true;
    }

    // 각 지역으로 움직이기
    public void moveLocal(int position) {
        // 1. 어떤 지역인지 꺼내온다
        List<Center> local = data.get(position);
        // 2. 첫번째 데이터의 지역으로 이동한다.
        double lat = Double.parseDouble(local.get(0).getLat());
        double lng = Double.parseDouble(local.get(0).getLng());
        LatLng latLng = new LatLng(lat, lng);
        // 애니메이션으로 이동하기
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(10)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
    }

    public void moveLocal(LatLng latLng, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
    }

    // 마커 찍어주기
    public void setMarkers(int position) {
        googleMap.clear();
        List<Center> centers = data.get(position);
        for (int i = 0; i < centers.size(); i++) {
            MarkerOptions options = new MarkerOptions();
            options.title(centers.get(i).getName());
            options.position(new LatLng(Double.parseDouble(centers.get(i).getLat()), Double.parseDouble(centers.get(i).getLng())));
            options.draggable(false);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected));
            googleMap.addMarker(options);
        }
    }

    public void addMarker(Center center) {
        MarkerOptions options = new MarkerOptions();
        options.title(center.getName());
        options.position(new LatLng(Double.parseDouble(center.getLat()), Double.parseDouble(center.getLng())));
        options.draggable(false);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected));
        googleMap.addMarker(options);
    }

    // 현재 선택된 위치 알려줌
    public void setCurRegion(String region) {
        this.curRegion = region;
    }


    // 현재 위치로
    public void setCurrentLocation() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGranted = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (isNetworkEnabled) {
            if (isGranted) {
                setProgressDialog();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, curLocationListener);
            }
        } else {
            Toast.makeText(getContext(), "네트워크, 위치권한을 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    LocationListener curLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            removeProgressDialog();
            double curLat = location.getLatitude();
            double curLng = location.getLongitude();
            moveLocal(new LatLng(curLat, curLng), 14);
            locationManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * 현재 위치 기반 가까운 센터 찾기
     */
    public void findCenterByCurPosition() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGranted = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (isGPSEnabled && isNetworkEnabled) {
            if (isGranted) {
                setProgressDialog();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, searchLocationListener);
            }
        } else {
            Toast.makeText(getContext(), "네트워크, 위치권한을 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    LocationListener searchLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            removeProgressDialog();
            searchCenterByCurLocation(location);
            locationManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void searchCenterByCurLocation(Location location) {
        List<Center> centers = CenterDao.getInstance(getContext()).getDatas();
        List<Double> distances = new ArrayList<>();
        Map<Double, Center> mapDistance = new HashMap<>();
        for (Center center : centers) {
            Location centerLocation = new Location("centerLocation");
            centerLocation.setLatitude(Double.parseDouble(center.getLat()));
            centerLocation.setLongitude(Double.parseDouble(center.getLng()));
            double distance = location.distanceTo(centerLocation);
            distances.add(distance);
            mapDistance.put(distance, center);
        }
        Collections.sort(distances);
        Center center = mapDistance.get(distances.get(0));
        moveLocal(new LatLng(Double.parseDouble(center.getLat()), Double.parseDouble(center.getLng())), 14);
        curRegion = center.getRegion();
        setLayoutVisibility();
        addMarker(center);
        setCenterPager(curRegion);
        movePagerPage(center.getName());
    }

    private void setProgressDialog() {
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);

        ConstraintSet set = new ConstraintSet();
        set.clone(mapListContainer);
        mapListContainer.addView(progressBar);

        set.connect(progressBar.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.connect(progressBar.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(progressBar.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.connect(progressBar.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        set.constrainHeight(progressBar.getId(), 200);
        set.constrainWidth(progressBar.getId(), 200);
        set.applyTo(mapListContainer);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void removeProgressDialog() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mapListContainer.removeView(progressBar);
    }


    /**
     * 지역별 센터 페이저뷰 세팅
     */
    public void setCenterPager(int position) {
        pagerAdapter = new MapPagerAdapter(data.get(position), this);
        mapPager.setAdapter(pagerAdapter);
        mapPager.setClipToPadding(false);
        mapPager.setPadding(120, 0, 120, 0);
        indicator.setViewPager(mapPager);
    }

    public void setCenterPager(String curRegion) {
        pagerAdapter = new MapPagerAdapter(CenterDao.getInstance(getContext()).getRegions(curRegion), this);
        mapPager.setAdapter(pagerAdapter);
        indicator.setViewPager(mapPager);
    }

    // 페이저 페이지 선택시 마커 들어가기
    public void setPagerPage() {
        List<Center> centers = CenterDao.getInstance(getActivity()).getRegions(curRegion);
        int curPos = mapPager.getCurrentItem();
        double lat = Double.parseDouble(centers.get(curPos).getLat());
        double lng = Double.parseDouble(centers.get(curPos).getLng());
        LatLng latLng = new LatLng(lat, lng);
        moveLocal(latLng, 14);
    }

    // 페이저 마커 클릭한 곳으로 움직여주기
    public void movePagerPage(String title) {
        List<Center> centers = CenterDao.getInstance(getActivity()).getRegions(curRegion);
        int curpso = 0;
        for (int i = 0; i < centers.size(); i++) {
            if (centers.get(i).getName().equals(title)) {
                curpso = i;
            }
        }
        mapPager.setCurrentItem(curpso);
    }

    // 지역 아이콘을 누를 때 뷰페이저 목록 출력
    public void setLayoutVisibility() {
        if (pagerLayout.getVisibility() == View.GONE) {
            pagerLayout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(curLocationListener);
            locationManager.removeUpdates(searchLocationListener);
        }
    }


}
