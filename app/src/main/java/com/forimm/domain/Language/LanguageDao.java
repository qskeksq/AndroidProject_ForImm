package com.forimm.domain.Language;

import com.forimm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-10.
 */

public class LanguageDao {

    private static LanguageDao sLanguageDao;
    private List<Language> languageList;

    public static LanguageDao getInstance(){
        if(sLanguageDao == null){
            sLanguageDao = new LanguageDao();
        }
        return sLanguageDao;
    }

    public List<Language> getData(){
        languageList = new ArrayList<>();
        languageList.add(new Language("한국어", R.drawable.korean_default, R.drawable.korean_selected));
        languageList.add(new Language("English", R.drawable.english_default, R.drawable.english_selected));
        languageList.add(new Language("नेपाली", R.drawable.nepal_default, R.drawable.nepal_selected));
        languageList.add(new Language("", R.drawable.lang_empty, 0));
        languageList.add(new Language("", R.drawable.lang_empty, 0));
        return languageList;
    }

}
