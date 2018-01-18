package com.forimm.domain.Center;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.forimm.domain.DBHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.forimm.Util.Const.ADDRESS;
import static com.forimm.Util.Const.NAME;
import static com.forimm.Util.Const.REGION;
import static com.forimm.Util.Const.TABLE_NAME;


public class CenterDao {

    List<Center> data;
    DBHelper helper;
    SQLiteDatabase db;
    private static CenterDao sCenter;

    // 생성자
    public CenterDao(Context context) {
        data = new ArrayList<>();
        helper = new DBHelper(context);

        // 사용하는 곳에서 에셋의 데이터를 경로에 복사하고 데이터베이스를 열어줘야 한다.
        try {
            helper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        helper.openDatabase();
        db = helper.getDb();
    }

    // 싱글턴
    public static CenterDao getInstance(Context context){
        if(sCenter == null){
            sCenter = new CenterDao(context);
        }
        return sCenter;
    }

    // 전체 데이터 출력
    public List<Center> getDatas(){
        List<Center> data = new ArrayList<>();

        CenterCursorWrapper cursor = query(null,null);

        while(cursor.moveToNext()){
            data.add(cursor.getCenterFromCursor());
        }

        cursor.close();
        return data;
    }

    // 지역 출력 -- 그 지역에 해당하는 것만 쿼리해서 가져온다.
    public List<Center> getRegions(String region, int resId, int selectedResId){
        List<Center> data = new ArrayList();

        String whereClause = REGION + " = ?";
        String[] whereArgs = { region };

        CenterCursorWrapper cursor = query(whereClause, whereArgs);

        while(cursor.moveToNext()){
            Center center = cursor.getCenterFromCursor();
            center.setResId(resId);
            center.setSelectedResId(selectedResId);
            data.add(center);
        }
        cursor.close();
        return data;
    }

    public List<Center> getRegions(String region){
        List<Center> data = new ArrayList();

        String whereClause = REGION + " = ?";
        String[] whereArgs = { region };

        CenterCursorWrapper cursor = query(whereClause, whereArgs);

        while(cursor.moveToNext()){
            Center center = cursor.getCenterFromCursor();
            data.add(center);
        }
        cursor.close();
        return data;
    }

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


    // 쿼리 메소드
    public CenterCursorWrapper query(String whereClause, String[] whereArgs){
        Cursor cursor = db.query(TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        return new CenterCursorWrapper(cursor);
    }
}
