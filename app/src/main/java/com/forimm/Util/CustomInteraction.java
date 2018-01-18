package com.forimm.Util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.forimm.R;


/**
 * Created by Administrator on 2017-07-11.
 */

public class CustomInteraction extends LinearLayout {

    public CustomInteraction(Context context) {
        super(context);
        init(context);
    }

    public CustomInteraction(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_intertactionbar2, this, false);
        addView(view);
    }

}
