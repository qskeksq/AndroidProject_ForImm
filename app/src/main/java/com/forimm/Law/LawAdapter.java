package com.forimm.Law;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.forimm.Main.MainActivity;
import com.forimm.R;
import com.forimm.Util.CustomInteraction;
import com.forimm.domain.Law.item.LawChild;
import com.forimm.domain.Law.item.LawParent;

import java.util.ArrayList;
import java.util.List;


/**
 * 노동법 법률 어댑터
 */
public class LawAdapter extends BaseExpandableListAdapter {

    // 사용할 데이터와 기존 데이터를 분리해서 저장
    List<LawParent> data = new ArrayList<>();
    List<LawParent> original = new ArrayList<>();

    MainActivity activity;

    public LawAdapter(List<LawParent> data, MainActivity activity) {
        this.data = data;
        this.original = data;
        this.activity = activity;
    }

    /**
     * 전체 부모 뷰 개수
     */
    @Override
    public int getGroupCount() {
        return data.size();
    }

    /**
     * 각 부모 뷰에 할당된 자식 뷰 개수
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getData().size();
    }

    /**
     * 현재 부모 뷰
     */
    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    /**
     * 현재 자식 뷰
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getData().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 부모 뷰 설정
     */
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
            seeDetailIcon.setImageResource(R.drawable.collapse_parent);
        } else {
            seeDetailIcon.setImageResource(R.drawable.expand_parent);
        }
        return convertView;
    }

    TextView title;

    /**
     * 자 뷰 설정
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 애니메이션 처리할 떄 null 값 처리해주고 재활용하기 때문에 문제 생김. 대부분 뷰 관련 문제는 재사용성 때문 인 듯 하다
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
                    seeChildDetail.setImageResource(R.drawable.collapse_parent);
                    title.setSingleLine(false);
                } else {
                    content.setVisibility(View.GONE);
                    seeChildDetail.setImageResource(R.drawable.expand_parent);
                    title.setSingleLine(true);
                }
            }
        });
        // + 버튼 설정
        ImageView start = (ImageView) convertView.findViewById(R.id.lawChildStartInter);

        // 애니메이션으로 등장할 커스텀 뷰 설정
        // convertView 가 재활용 될 때 findViewById를 하지만 이미 값이 설정된 상황에서 그 주소만 찾아오는 셈이 된다. 따라서 이미
        // 애니메이션도 설정이 되어 있는 상황인데 추가로 세팅을 해주면 마지막으로 세팅해 준 애니메이션이 실행이 된다. 따라서 마지막 애니메이션은 nulㅣ로 설정해준다
        // 한가지 더 생각할 것은 interaction이 맨 오른쪽에서 시작하는 게 아니라 Visibility 만 Gone 한 상태에서 애니메이션을 줬기 때문에 위의 재사용 문제와
        // 겹쳐서 계속 애니메이션이 반복되는 상황이 나타났던 것
        final CustomInteraction interaction = (CustomInteraction) convertView.findViewById(R.id.lawChildInteraction);
        interaction.setVisibility(View.GONE);
        ImageView cancelInteraction = (ImageView) interaction.findViewById(R.id.cancelInteraction);
        ImageView sendEmailInteraction = (ImageView) interaction.findViewById(R.id.sendEmailInteraction);

        // 애니메이션 설정
        final Animation slide_in = AnimationUtils.loadAnimation(parent.getContext(), R.anim.slide_in_left);
        final Animation slide_out = AnimationUtils.loadAnimation(parent.getContext(), R.anim.slide_out_left);
        interaction.setAnimation(slide_out);
        interaction.setAnimation(slide_in);
        interaction.setAnimation(null);

        // 동기화 해주기 위해 리스너 설정
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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interaction.getVisibility() == View.GONE){
                    interaction.setVisibility(View.VISIBLE);
                    interaction.startAnimation(slide_in);
                }
            }
        });
        cancelInteraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interaction.getVisibility() == View.VISIBLE) {
                    interaction.startAnimation(slide_out);
                }
            }
        });
        sendEmailInteraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.callEmail();
                activity.setLawContent(data.get(groupPosition).getData().get(childPosition).getContent());
            }
        });
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 데이터 필터링 한 후 검색된 내용만을 리스트에 갱신해서 출력
     * @param newText : 입력된 검색어
     */
    public void filter(String newText){
        // 처음에는 반드시 원상태로 복구해 준다. 복구해 주지 않으면 밑에서 변형된 채로 검색하기 때문에 데이터가 온전히 검색되지 않을 수 있다.
        data = original;
        // 검색이 취소될 때 원상태로 리턴되게 한다.
        if(newText.length() == 0){
            return;
        } else {
            // 검색된 자료를 담을 부모 객체
            List<LawParent> newParentList = new ArrayList<>();
            // 부모 객체 안의 자식 개체 하나하나 조사
            for(LawParent parent : data){
                List<LawChild> newChildList = new ArrayList<>();
                for(LawChild child : parent.getData()){
                    // 자식 객체가 검색어를 포함하고 있다면
                    if(child.getTitle().contains(newText) || child.getContent().contains(newText)){
                        // 새로운 자식 객체 배열에 담아둔다
                        newChildList.add(child);
                    }
                }
                // 자식 객체를 모두 조사한 후 검색된 유효값이 있다면 새로운 부모 객체에 담아준다.
                if(newChildList.size() > 0){
                    // 새로운 parent 를 넣어줘야지 위에 있는 parent 전체를 넣어버리면 어떡하냐
                    LawParent newParent = new LawParent(parent.getChapter(), newChildList);
                    newParentList.add(newParent);
                }
            }
            // 마지막에 새로운 부모 객체(자식 개체를 담고 있는)를 담고 있는 배열로 기존 데이터를 갱신해 준다.
            data = newParentList;
        }
        // 어댑터 갱신
        notifyDataSetChanged();
    }

}
