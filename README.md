# ForImm
- 이주민을 위한 정보제공 어플리케이션

#### 주요 기능
- 이주민 노동센터 찾기
- 한국 노동법 검색
- 자주 묻는 질문
- 이메일 컨설팅


![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184805_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184810_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184816_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184837_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184850_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184919_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184927_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184950_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_184955_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_185003_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_185032_180x320.jpg)
![](https://github.com/qskeksq/ForImm/blob/master/pic/20171116_185156_180x320.jpg)



## 1. 외부 SQLite 데이터베이스 입력

1. 경로에 해당하는 데이터베이스가 있는지 확인
2. 데이터 베이스가 없으면 복사
3. 복사한 데이터베이스를 열어주고 쿼리 할 수 있도록 리턴

### (1) 외부 sqlite 파일을 경로에 복사

```java
// 에셋에 넣어준 sqlite 파일을 위에서 만들어 준 데이터베이스에 복사해준다
public void copyDatabase() throws IOException {
    InputStream myInput = context.getAssets().open(DB_NAME);
    String outFileName = DB_PATH+ DB_NAME;
    OutputStream myOutput = new FileOutputStream(outFileName);
    byte[] buffer = new byte[1024];
    int length;
    while((length = myInput.read(buffer))>0){
        myOutput.write(buffer, 0, length);
    }
    myOutput.flush();
    myOutput.close();
    myInput.close();
}
```

### (2) 데이터베이스가 열려있는지 확인

```java
public boolean checkDatabase(){
    SQLiteDatabase checkDB = null;
    try{
        String myPath = DB_PATH + DB_NAME;
        checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    } catch (SQLiteException e){
        e.printStacekTrace();
    }
    return checkDB != null ? true : false;
}
```

### (3) 데이터 베이스가 없으면 열어준다.

```java
public void createDatabase() throws IOException {
    boolean dbexists = checkDatabase();
    // 데이터베이스가 존재할 경우
    if(dbexists) {
        if(BuildConfig.DEBUG){
            try{
                // 디버그 모드에서는 항상 데이터베이스를 복사해서 새로 생성한다
                copyDatabase();
            }catch (IOException e){
                e.printStacekTrace();
            }
        } else {
            return;
        }
    // 데이터베이스가 없으면 외부 파일을 복사해준다    
    } else {
        this.getReadableDatabase();
        try{
            copyDatabase();
        }catch (IOException e){
            e.printStacekTrace();
        }
    }
}
```

### (4) 복사한 데이터베이스를 열어주고 이 데이터베이스로 쿼리 할 수 있도록 리턴해준다.

```java
public void openDatabase() throws SQLiteException {
    String myPath = DB_PATH + DB_NAME;
    db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
}
public SQLiteDatabase getDb(){
    return  db;
}
```

### (5) 쿼리

- 전체 쿼리

    ```java
    public List<Center> getDatas(){
        List<Center> data = new ArrayList<>();

        CenterCursorWrapper cursor = query(null,null);

        while(cursor.moveToNext()){
            data.add(cursor.getCenterFromCursor());
        }

        cursor.close();
        return data;
    }
    ```

- 특정 검색어 쿼리

    ```java
    public List<Center> querySome(String query){
        List<Center> centers = new ArrayList<>();

        String whereClause =
                REGION + " like '%"+query+"%' or "+
                NAME + " like '%"+query+"%' or " +
                ADDRESS + " like '%"+query+"%' ";

        CenterCursorWrapper cursor = query(whereClause, null);

        while(cursor.moveToNext()){
            Center center = cursor.getCenterFromCursor();
            centers.add(center);
        }
        cursor.close();
        return centers;
    }
    ```

- 커서가 객체를 리턴해 줄 수 있도록 래핑해줌

    ```java
         public CenterCursorWrapper query(String whereClause, String[] whereArgs){
             Cursor cursor = db.query(TABLE_NAME, null, whereClause, whereArgs, null, null, null);
             return new CenterCursorWrapper(cursor);
         }
    ```


## 2. 이주민 노동센터 찾기

- 맵프래그먼트 사용
- 커스텀 마커 찍어주기
- 현재위치 찾기
- 뷰페이저 패딩값 설정
- 센터 검색(필터)
- [Custom ProgressDialog](https://github.com/qskeksq/ProgressDialog/blob/master/README.md)

### 맵프래그먼트 사용

- 매니페스트 추가

    ```HTML
    <meta-data
        android:name="com.google.android.maps.v2.API_KEY"
        android:value="API_KEY" />
    ```

- xml 추가

    ```HTML
    <fragment
        android:name="com.google.android.gms.maps.SupportMapFragment"
        ... />
    ```

- 카메라 움직이기

    ```java
    public void moveLocal(LatLng latLng, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .bearing(45)  // 평면에서 얼마나 지도를 기울일 것인가
                .tilt(90)  // 3d 로 얼마나 기울어져 있는가
                .zoom(zoom)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
    }
    ```

- Marker & MarkerOptions

    ```java
    MarkerOptions options = new MarkerOptions();
    options.title(centers.get(i).getName());
    options.position(new LatLng(Double.parseDouble(centers.get(i).getLat()), Double.parseDouble(centers.get(i).getLng())));
    options.draggable(false);
    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected));
    ```
    ```java
    Marker marker = googleMap.addMarker(options);
    marker.showInfoWindow();
    ```

### 현재위치 찾기

- GPS, 네트워크 프로바이더 지정되어 있는지 확인
    - 현재 위치 찾기를 실시간으로 하기 힘들기 때문에 현재위치가 반드시 필요하다면 어플리케이션 설치 할 때 미리 설정을 하거나 지도 권한을 받은 후 바로 찾으면 된다.
    - GPS는 실내에서만 가능하고 시간도 오래걸리며 배터리 소모도 크다. 따라서 위치설정을 할 때는 여러 요소를 고려해서 GPS와 네트워크로 현재위치 찾기를 적절히 선택해서 사용해야 한다
    - 네트워크 프로바이더는 시스템에서 설정하지 않으면 계속 false로 나온다. 따라서 startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)를 통해 설정을 요구해야 한다

```java
LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
boolean isGranted = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

if (isGPSEnabled && isNetworkEnabled) {
    if (isGranted) {
        setProgressDialog();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, curLocationListener);
    }
}
```

```java
LocationListener curLocationListener = new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {

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
```
### 뷰페이저 패딩값 설정

    초반에 안 됬던 이유는 카드뷰에 특정 값을 준 것이 아니라 마진 값을 줬기 때문이다.

```java
pagerAdapter = new MapPagerAdapter(data.get(position), this);
mapPager.setAdapter(pagerAdapter);
mapPager.setClipToPadding(false);
mapPager.setPadding(120, 0, 120, 0);
indicator.setViewPager(mapPager);
```

### 이주민센터 검색(필터)

- A. 데이터 원본과 검색된 데이터를 넣어둘 임시저장소 생성

    ```java
    List<Center> data = new ArrayList<>();
    List<Center> original = new ArrayList<>();
    ```

- B. 필터

    ```java
    // 1. 검색어 입력
    public void filter(String newText){
            // 2. 검색어가 없을 경우 전체 데이터 리턴
            data = original;
            if(newText.length() == 0){
                return;
            } else {
                // 3. 임시저장소에 넣어줄 임시데이터 저장소
                List<Center> newList = new ArrayList<>();
                // 4. 센터명과 넣어준 검색어를 비교, 일치사항이 있으면 추가
                for(Center center : data){
                    if(center.getName().contains(newText) || center.getAddress().contains(newText)
                            || center.getEmail().contains(newText) || center.getTel().contains(newText)){
                        newList.add(center);
                    }
                }
                // 5. 전체 데이터에서 필터링된 임시데이터로 바꿔줌
                data = newList;
            }
            // 6. 필터링된 임시데이터로 리사이클러뷰 데이터 갱신
            notifyDataSetChanged();
        }
    ```


## 노동법 검색

- ExpandableRecyclerView & 검색어 필터링
- 커스텀뷰 & 애니메이션

### 커스텀뷰 애니메이션

    참고로 현재 애니메이션은 뷰가 완전히 밖에서 들어오는 것이 아니라 Visibility를 Gone으로 해두고 애니메이션을 적용한 후 다시 Visibility 설정을 해주는 것이다.

- slide_in_left : 시작

    ```html
    <set xmlns:android="http://schemas.android.com/apk/res/android">
        <translate
            android:fromXDelta="100%"
            android:toXDelta="0"
            android:duration="500" />
    </set>
    ```

- slide_out_left : 취소

    ```html
    <set xmlns:android="http://schemas.android.com/apk/res/android">
        <translate
            android:fromXDelta="0"
            android:toXDelta="100%"
            android:duration="500" />

    </set>
    ```

- Animation
    - setAnimation(null)을 해주는 이유는 현재 (Expandable)리사이클러뷰에서 부모, 자식 아이템을 사용할 때 재활용 하지 않기 때문이다. 따라서 다음으로 넘어오는 convertView의
    interaction에는 이미 애니메이션이 설정되어 있고, 지난 애니메이션 적용까지 모두 남아있다. 이 상황에서 다시 애니메이션을 세팅해주면 마지막에 설정한 애니메이션이 실행되는 현상이 발생한다
    따라서 마지막 애니메이션을 null로 해준다
    - 참고로 setAnimationListener하면 애니메이션이 설정되는 때에도 onAnimationStart, onAnimationEnd 콜백함수가 호출된다

    ```java
        Animation slide_in = AnimationUtils.loadAnimation(parent.getContext(), R.anim.slide_in_left);
        Animation slide_out = AnimationUtils.loadAnimation(parent.getContext(), R.anim.slide_out_left);
        interaction.setAnimation(slide_out);
        interaction.setAnimation(slide_in);
        interaction.setAnimation(null);
    ```
    ```java
        slide_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            // 애니메이션이 끝난 후 값을 변경해 준다.
            @Override
            public void onAnimationEnd(Animation animation) {
                interaction.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    ```
    ```java
        interaction.startAnimation(slide_in);
        interaction.startAnimation(slide_out);
    ```

### ExpandableRecyclerView

    라이브러리 사용하지 않고 직접 구현하는 것이 빠르고 정확하다

- 시스템(안드로이드)에서 알아야 할 사전 데이터

    ```java
    // 전체 부모 뷰 개수
    @Override
    public int getGroupCount() {
        return data.size();
    }

    // 각 부모 뷰에 할당된 자식 뷰 개수
    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getData().size();
    }

    // 현재 부모 뷰
    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }


    // 현재 자식 뷰
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getData().get(childPosition);
    }

    // 현재 부모 아이디
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 현재 자식 아이디
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    ```

- 부모뷰 설정(getView 영역)

    ```java
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_law_parent, parent, false);
        }
        // 부모뷰 제목 설정
        TextView parentTitle = (TextView) convertView.findViewById(R.id.lawParentTitle);
        switch (data.get(groupPosition).getChapter()){
            case "1부":
                parentTitle.setText(data.get(groupPosition).getChapter()+".노동기본권");
                break;
            case "2부":
                parentTitle.setText(data.get(groupPosition).getChapter()+".고용허가제");
                break;
            case "3부":
                parentTitle.setText(data.get(groupPosition).getChapter()+".안전하게 일할 권리");
                break;
            case "4부":
                parentTitle.setText(data.get(groupPosition).getChapter()+".작업중지권");
                break;
        }
        ImageView seeDetailIcon = (ImageView) convertView.findViewById(R.id.lawParentDown);
        if(isExpanded){
            seeDetailIcon.setImageResource(R.drawable.list_up);
        } else {
            seeDetailIcon.setImageResource(R.drawable.list_down_black);
        }
        return convertView;
    }
    ```

- 자식뷰 설정

    ```java
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_law_child, parent, false);
        }

        // 자식뷰 제목 설정
        final TextView title = (TextView) convertView.findViewById(R.id.lawChildTitle);
        title.setText(data.get(groupPosition).getData().get(childPosition).getTitle());

        // 자식뷰 내용 설정
        final TextView content = (TextView) convertView.findViewById(R.id.lawContent);
        final ImageView seeChildDetail = (ImageView) convertView.findViewById(R.id.lawChildDown);
        content.setText(data.get(groupPosition).getData().get(childPosition).getContent());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.getVisibility()== View.GONE){
                    content.setVisibility(View.VISIBLE);
                    seeChildDetail.setImageResource(R.drawable.list_up);
                    title.setSingleLine(false);
                } else {
                    content.setVisibility(View.GONE);
                    seeChildDetail.setImageResource(R.drawable.list_down_black);
                    title.setSingleLine(true);
                }
            }
        });
        // + 버튼 설정
        ImageView start = (ImageView) convertView.findViewById(R.id.lawChildStartInter);

        // 애니메이션으로 등장할 커스텀 뷰 설정
        final CustomInteraction interaction = (CustomInteraction) convertView.findViewById(R.id.lawChildInteraction);
        ImageView cancelInteraction = (ImageView) interaction.findViewById(R.id.cancelInteraction);
        ImageView sendEmailInteraction = (ImageView) interaction.findViewById(R.id.sendEmailInteraction);

        // 애니메이션 적용
        // 위에서 작성한 Animation 참고

        return convertView;
    }
    ```

### ExpandableRecyclerView 검색어 필터링

    기존 필터링 방식과 같이 하되 자식의 개수가 있으면 부모를 생성해서 임시저장소에 같이 넣어준다.

```java
/**
* 데이터 필터링 한 후 검색된 내용만을 리스트에 갱신해서 출력
* @param newText : 입력된 검색어
*/
public void filter(String newText){
    // 1. 처음에는 반드시 원상태로 복구해 준다. 복구해 주지 않으면 밑에서 변형된 채로 검색하기 때문에 데이터가 온전히 검색되지 않는다.
    data = original;
    // 검색이 취소될 때 원상태로 리턴되게 한다.
    if(newText.length() == 0){
        return;
    } else {
        // 2. 검색된 자료를 담을 부모 객체
        List<LawParent> newParentList = new ArrayList<>();
        // 3. 부모 객체 안의 자식 개체 하나하나 조사
        for(LawParent parent : data){
            // 4. 부모 객체 안에 담을 자식 객체 임시저장소
            List<LawChild> newChildList = new ArrayList<>();
            for(LawChild child : parent.getData()){
                // 5. 자식 객체가 검색어를 포함하고 있다면
                if(child.getTitle().contains(newText) || child.getContent().contains(newText)){
                    // 새로운 자식 객체 배열에 담아둔다
                    newChildList.add(child);
                }
            }
            // 6. 자식 객체를 모두 조사한 후 검색된 유효값이 있다면 새로운 부모 객체에 담아준다.
            if(newChildList.size() > 0){
                // 새로운 parent 를 넣어줘야 한다
                LawParent newParent = new LawParent(parent.getChapter(), newChildList);
                newParentList.add(newParent);
            }
        }
        // 7. 마지막에 새로운 부모 객체(자식 개체를 담고 있는)를 담고 있는 배열로 기존 데이터를 갱신해 준다.
        data = newParentList;
    }
    // 8. 어댑터 갱신
    notifyDataSetChanged();
}
```

## 5. 이메일 문의하기

#### 인텐트로 다중 데이터 보내기

```java
public void send(String sender, String receiver, String title, String content) {
    String[] tos = {receiver};
    String[] me = {sender};
    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);   // 다중으로 보내고 싶을 때 사용
    intent.putExtra(Intent.EXTRA_EMAIL, tos);  // 받는 사람. 꼭 배열에 넣어줘야 한다. 아마 다수의 사람에게 보낼 수 있는 듯
    intent.putExtra(Intent.EXTRA_CC, me);       
    intent.putExtra(Intent.EXTRA_TEXT, content);
    intent.putExtra(Intent.EXTRA_SUBJECT, title);
    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUri);  // ArrayList 가 parcelable 을 상속받았다.
    intent.setType("message/rfc822");                                   // 이걸로 하면 선택할 앱이 몇 개 덜 뜸.
    startActivity(Intent.createChooser(intent, "선택하세요"));
}
```