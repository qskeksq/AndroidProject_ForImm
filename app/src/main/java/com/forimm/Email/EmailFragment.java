package com.forimm.Email;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.forimm.R;
import com.forimm.Util.CheckPermission;
import com.forimm.domain.Center.Center;
import com.forimm.domain.Center.CenterDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class EmailFragment extends Fragment implements CheckPermission.CallBack, View.OnClickListener {

    View view;
    Toolbar toolbar;
    PopupMenu popupMenu;
    RecyclerView imageRecycler;
    ExpandableListView listView;
    ConstraintLayout constraintLayout;
    ImageView emailAddress, myMail, toggle;
    EditText inputMyMail, email, title, content;
    EditText input;

    boolean status = false;
    List<String> chapters;
    Map<String, List<Center>> contents;
    ImageAdapter adapter;
    EmailExpandable expandable;
    ArrayList<Uri> imageUri = new ArrayList<>();
    String askLaw;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_email, container, false);
            initViews();
            setToolbar();
            setListener();
            initData();
            initSP();
            setEmailExpandable();
            setImageRecycler();
        }
        return view;
    }


    public void initViews() {
        toolbar = (Toolbar) view.findViewById(R.id.emailToolbar);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);
        emailAddress = (ImageView) view.findViewById(R.id.expandList);
        myMail = (ImageView) view.findViewById(R.id.myEmail);
        inputMyMail = (EditText) view.findViewById(R.id.inputMyEmail);
        email = (EditText) view.findViewById(R.id.inputEmail);
        title = (EditText) view.findViewById(R.id.inputTitle);
        content = (EditText) view.findViewById(R.id.inputContent);
        listView = (ExpandableListView) view.findViewById(R.id.recyclerView3);
        toggle = (ImageView) view.findViewById(R.id.toggleOn);
        imageRecycler = (RecyclerView) view.findViewById(R.id.imageRecycler);
        setContent();
    }

    private void setContent() {
        if (askLaw == null) {
            content.setText("\n" + "언제 : " + "\n\n" + "어디서 : " + "\n\n" + "누가" + "\n\n" + "무엇을 : " + "\n\n" + "어떻게 : " + "\n");
        } else {
            content.setText("\n" + askLaw + "\n");
        }
    }

    public void setToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.mainBackground));
        toolbar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) toolbar.setElevation(5);
    }

    public void setListener() {
        emailAddress.setOnClickListener(this);
        myMail.setOnClickListener(this);
        toggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expandList:
                expandList();
                break;
            case R.id.toggleOn:
                setContentToggle();
                break;
        }
    }

    private void expandList() {
        int layoutsStatus = constraintLayout.getVisibility();
        if (layoutsStatus == View.VISIBLE) {
            emailAddress.setImageResource(R.drawable.expand_parent);
            constraintLayout.setVisibility(View.GONE);
            view.findViewById(R.id.imageView23).setVisibility(View.VISIBLE);
        } else if (layoutsStatus == View.GONE) {
            emailAddress.setImageResource(R.drawable.collapse_parent);
            constraintLayout.setVisibility(View.VISIBLE);
            view.findViewById(R.id.imageView23).setVisibility(View.INVISIBLE);
        }
    }

    private void setContentToggle() {
        if (status) {
            toggle.setImageResource(R.drawable.toggle_on);
            content.setText("\n" + "언제 : " + "\n\n" + "어디서 : " + "\n\n" + "누가" + "\n\n" + "무엇을 : " + "\n\n" + "어떻게 : ");
            status = false;
        } else {
            toggle.setImageResource(R.drawable.toggle_off);
            content.setText("");
            status = true;
        }
    }

    public void initData() {
        chapters = new ArrayList<>();
        contents = new HashMap<>();

        // 이 이름으로 쿼리하기 때문에 이 이름을 바꾸면 안 된다.
        chapters.add("부산");
        chapters.add("경남");
        chapters.add("울산");
        chapters.add("대구");
        chapters.add("경북");
        chapters.add("대전");
        chapters.add("울산");
        chapters.add("충청");
        chapters.add("광주");
        chapters.add("전남");
        chapters.add("인천");
        chapters.add("경기");
        chapters.add("서울");
        chapters.add("제주");

        List<Center> busan = CenterDao.getInstance(getActivity()).getRegions(chapters.get(0));
        List<Center> daegu = CenterDao.getInstance(getActivity()).getRegions(chapters.get(1));
        List<Center> gyeongbuk = CenterDao.getInstance(getActivity()).getRegions(chapters.get(2));
        List<Center> daejeon = CenterDao.getInstance(getActivity()).getRegions(chapters.get(3));
        List<Center> ulsan = CenterDao.getInstance(getActivity()).getRegions(chapters.get(4));
        List<Center> choongnam = CenterDao.getInstance(getActivity()).getRegions(chapters.get(5));
        List<Center> gwangju = CenterDao.getInstance(getActivity()).getRegions(chapters.get(6));
        List<Center> jeonnam = CenterDao.getInstance(getActivity()).getRegions(chapters.get(7));
        List<Center> incheon = CenterDao.getInstance(getActivity()).getRegions(chapters.get(8));
        List<Center> gyeonggi = CenterDao.getInstance(getActivity()).getRegions(chapters.get(9));
        List<Center> seoul = CenterDao.getInstance(getActivity()).getRegions(chapters.get(10));
        List<Center> jeju = CenterDao.getInstance(getActivity()).getRegions(chapters.get(11));

        contents.put(chapters.get(0), busan);
        contents.put(chapters.get(1), daegu);
        contents.put(chapters.get(2), gyeongbuk);
        contents.put(chapters.get(3), daejeon);
        contents.put(chapters.get(4), ulsan);
        contents.put(chapters.get(5), choongnam);
        contents.put(chapters.get(6), gwangju);
        contents.put(chapters.get(7), jeonnam);
        contents.put(chapters.get(8), incheon);
        contents.put(chapters.get(9), gyeonggi);
        contents.put(chapters.get(10), seoul);
        contents.put(chapters.get(11), jeju);
    }

    private void initSP() {
        sp = getActivity().getSharedPreferences("mailsp", MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("count", 0);
        editor.commit();
    }

    private void setEmailExpandable() {
        expandable = new EmailExpandable(chapters, contents, this);
        listView.setAdapter(expandable);
        listView.setGroupIndicator(null);
    }

    private void setImageRecycler() {
        adapter = new ImageAdapter(this);
        imageRecycler.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecycler.setLayoutManager(manager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.email_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.attachemail:
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                CheckPermission.checkVersion(this, perms);
                break;
            case R.id.sendemail:
                send(inputMyMail.getText().toString(), email.getText().toString(), title.getText().toString(), content.getText().toString());
                break;
            case R.id.emailsettings:
                break;
        }
        return true;
    }

    public void send(String sender, String receiver, String title, String content) {
        String[] tos = {receiver};
        String[] me = {sender};
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);   // 다중으로 보내고 싶을 때 사용
        intent.putExtra(Intent.EXTRA_EMAIL, tos);   // 받는 사람. 꼭 배열에 넣어줘야 한다. 아마 다수의 사람에게 보낼 수 있는 듯
        intent.putExtra(Intent.EXTRA_CC, me);       // 참조. 뭐하는 것인지는 아직 확실하지 않음.
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUri);  // ArrayList 가 parcelable 을 상속받았군. 처음 알았다.
        intent.setType("message/rfc822");           // 이걸로 하면 선택할 앱이 몇 개 덜 뜸.
        startActivity(Intent.createChooser(intent, "선택하세요"));
    }

    public void setEmail(String input) {
        email.setText(input);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 999) {
                imageRecycler.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
                imageUri.add(uri);
                adapter.setData(imageUri);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setImageRecyclerVisibility() {
        imageRecycler.setVisibility(View.GONE);
    }

    @Override
    public void callInit() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 999);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPermission.onResult(requestCode, grantResults, this);
    }

    public void setLawContent(String law) {
        askLaw = law;
    }


}
