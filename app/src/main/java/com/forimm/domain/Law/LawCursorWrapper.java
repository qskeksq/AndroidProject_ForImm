package com.forimm.domain.Law;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.forimm.domain.Law.item.LawChild;

import static com.forimm.Util.Const.CONTENT;
import static com.forimm.Util.Const.TITLE;

/**
 * Created by Administrator on 2017-07-13.
 */

public class LawCursorWrapper extends CursorWrapper {

    public LawCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LawChild getLawFromCursor(){
        String title = getString(getColumnIndex(TITLE));
        String content = getString(getColumnIndex(CONTENT));

        LawChild law = new LawChild(title, content);
        return law;
    }

}
