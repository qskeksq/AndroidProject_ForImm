package com.forimm.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.forimm.R;
import com.forimm.Util.Const;
import com.forimm.domain.Language.Language;
import com.forimm.domain.Language.LanguageDao;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017-08-10.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.Holder> {

    List<Language> data;
    Context context;
    MainFragment fragment;
    SharedPreferences sp;

    public LanguageAdapter(Context context, MainFragment fragment) {
        this.data = LanguageDao.getInstance().getData();
        this.context = context;
        this.fragment = fragment;
        sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.flag.setImageResource(data.get(position).getResId());
        holder.name.setText(data.get(position).getName());
        holder.position = position;
        if(sp.getInt(Const.LANG_POSITION, 0) == position){
            holder.flag.setImageResource(data.get(position).getSelectedImgId());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView flag;
        TextView name;
        int position;

        public Holder(View itemView) {
            super(itemView);
            flag = (ImageView) itemView.findViewById(R.id.languageFlag);
            name = (TextView) itemView.findViewById(R.id.languageName);
            name.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSans-Bold.ttf"));
            itemView.setOnClickListener(languageListener);
        }

        View.OnClickListener languageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        setLang();
                        break;
                    case 1:
                        setLang();
                        break;
                    case 2:
                        setLang();
                        break;
                }
                saveSP(position);
            }
        };

        private void setLang(){
            notifyDataSetChanged();
        }

        private void setLang(Locale locale){
            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            fragment.refreshFragment();
        }

        private void saveSP(int position){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(Const.LANG_POSITION, position);
            editor.commit();
        }
    }
}
