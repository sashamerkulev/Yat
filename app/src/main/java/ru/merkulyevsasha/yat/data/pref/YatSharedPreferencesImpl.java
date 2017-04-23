package ru.merkulyevsasha.yat.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sasha_merkulev on 23.04.2017.
 */

public class YatSharedPreferencesImpl implements YatSharedPreferences{

    private static final String PREF_NAME = "YAT_PREFS";
    private static final String KEY_LANG_INDEX = "LANG_INDEX";

    final SharedPreferences pref;

    public YatSharedPreferencesImpl(Context context){
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void setLanguageIndex(int index){
        pref.edit().putInt(KEY_LANG_INDEX, index).apply();
    }

    @Override
    public int getLanguageIndex(){
        return pref.getInt(KEY_LANG_INDEX, 0);
    }

}
