package com.forimm.Map.search;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forimm.R;
import com.forimm.domain.Center.Center;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-10.
 */

public class MapSearchAdapter extends RecyclerView.Adapter<MapSearchAdapter.Holder> {

    List<Center> centers = new ArrayList<>();

    public void setData(List<Center> centers){
        this.centers = centers;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_center_recycler, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Center center = centers.get(position);
        holder.name.setText(center.getName());
        holder.address.setText(center.getAddress());
        holder.phone.setText(center.getTel());
        holder.email.setText(center.getEmail());
        holder.title = center.getName();
    }

    @Override
    public int getItemCount() {
        return centers.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView name, address, phone, email;
        ConstraintLayout layout;
        String title;

        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.mapListCenterName);
            address = (TextView) itemView.findViewById(R.id.mapListCenterAddres);
            phone = (TextView) itemView.findViewById(R.id.mapListCenterPhone);
            email = (TextView) itemView.findViewById(R.id.mapListCenterEmail);
            layout = (ConstraintLayout) itemView.findViewById(R.id.mapListCenterItem);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

}
