package com.forimm.Map.card;

import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forimm.Map.main.MapFragment;
import com.forimm.R;
import com.forimm.domain.Center.Center;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapPagerAdapter extends PagerAdapter implements View.OnClickListener {

    List<Center> data;

    LatLng latLng;
    Center center;
    MapFragment fragment;

    public MapPagerAdapter(List<Center> data, MapFragment fragment) {
        this.data = data;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_map_pager, container, false);
        TextView name = (TextView) view.findViewById(R.id.centerName);
        TextView address = (TextView) view.findViewById(R.id.centerAddress);
        TextView phone = (TextView) view.findViewById(R.id.centerTelephone);
        TextView email = (TextView) view.findViewById(R.id.centerMail);
        ConstraintLayout pagerItem = (ConstraintLayout) view.findViewById(R.id.pagerItem);
        center = data.get(position);
        name.setText(center.getName());
        address.setText(center.getAddress());
        phone.setText(center.getTel());
        email.setText(center.getEmail());
        pagerItem.setOnClickListener(this);
        Typeface typeface = Typeface.createFromAsset(fragment.getActivity().getAssets(), "fonts/NotoSans-Bold.ttf");
        name.setSelected(true);
        address.setSelected(true);
        email.setSelected(true);
        name.setTypeface(typeface);
        address.setTypeface(typeface);
        phone.setTypeface(typeface);
        email.setTypeface(typeface);
        latLng = new LatLng(Double.parseDouble(center.getLat()), Double.parseDouble(center.getLng()));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        fragment.setPagerPage();
    }
}
