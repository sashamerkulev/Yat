package ru.merkulyevsasha.yat.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;

import ru.merkulyevsasha.yat.data.pojo.Trans;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public class DbDataSourceImpl extends SQLiteOpenHelper implements DbDataSource{

    public static final String DATABASE_NAME = "yat.db";
    public static final int DATABASE_VERSION = 1;

    public static final String WORD_TABLE_NAME = "word";
    public static final String ID = "id";
    public static final String TEXT = "word";
    public static final String TRANSLATED_TEXT = "translated";
    public static final String TEXT_LOWER = "word_lower";
    public static final String LANG = "lang";
    public static final String FAVORITE = "favorite";
    public static final String JSON = "json";


    public DbDataSourceImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_WORD_TABLE = "CREATE TABLE " + WORD_TABLE_NAME +
                "( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TEXT + " TEXT, " +
                TRANSLATED_TEXT + " TEXT, " +
                TEXT_LOWER + " TEXT, " +
                LANG + " TEXT, " +
                JSON + " TEXT, " +
                FAVORITE + " INTEGER " +
                ");"
                +" create index "+WORD_TABLE_NAME+"_id_index on " + WORD_TABLE_NAME + "(" + ID + "); "
                +" create index "+WORD_TABLE_NAME+"_word_index on " + WORD_TABLE_NAME + "(" + TEXT+", "+LANG + "); "
                ;

        db.execSQL(CREATE_WORD_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public int saveHistory(Trans trans, String translatedText) {

        int id = 0;

        try (SQLiteDatabase db = getWritableDatabase()) {

            String select = "SELECT * FROM " + WORD_TABLE_NAME + " WHERE " + TEXT_LOWER + "=@word and " + LANG + "=@lang";
            try (Cursor cursor = db.rawQuery(select, new String[]{trans.getText().toLowerCase(), trans.getLang()})) {
                if (cursor.moveToFirst()) {
                    id = cursor.getInt(cursor.getColumnIndex(ID));
                }
            }

            if (id == 0) {

                ContentValues values = new ContentValues();
                values.put(TEXT, trans.getText());
                values.put(TRANSLATED_TEXT, translatedText);
                values.put(TEXT_LOWER, trans.getText().toLowerCase());
                values.put(LANG, trans.getLang());
                values.put(JSON, trans.getJson());
                values.put(FAVORITE, 0);

                id = (int) db.insert(WORD_TABLE_NAME, null, values);

            } else {

                ContentValues values = new ContentValues();
                values.put(JSON, trans.getJson());
                values.put(TRANSLATED_TEXT, translatedText);

                db.update(WORD_TABLE_NAME, values, "id=@id", new String[]{String.valueOf(id)});
            }

        }
        return id;
    }

    @Override
    public int getFavorite(int id) {
        int result = 0;
        try (SQLiteDatabase db = getReadableDatabase()) {
            String select = "SELECT * FROM " + WORD_TABLE_NAME + " WHERE " + ID + "=@id";
            try (Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(id)})) {
                if (cursor.moveToFirst()) {
                    result = cursor.getInt(cursor.getColumnIndex(FAVORITE));
                }
            }
        }
        return result;
    }

    @Override
    public void setFavorite(int id, boolean favorite) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(FAVORITE, favorite?1:0);
            db.update(WORD_TABLE_NAME, values, "id=@id", new String[]{ String.valueOf(id)});
        }
    }

    @NonNull
    private Word getWord(Cursor cursor) {
        Word item = new Word();
        item.setLanguage(cursor.getString(cursor.getColumnIndex(LANG)));
        item.setTranslatedText(cursor.getString(cursor.getColumnIndex(TRANSLATED_TEXT)));
        item.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
        item.setFavorite(cursor.getInt(cursor.getColumnIndex(FAVORITE))==1);
        item.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        return item;
    }

    private List<Word> getWord (String select){
        List<Word> result = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase()) {
            try (Cursor cursor = db.rawQuery(select, null)) {
                if (cursor.moveToFirst()) {
                    do {
                        Word item = getWord(cursor);
                        result.add(item);
                    } while (cursor.moveToNext());
                }
            }
        }
        return result;
    }

    @Override
    public List<Word> getHistory() {
        String select = "SELECT * FROM " + WORD_TABLE_NAME ;
        return getWord(select);
    }

    @Override
    public void deleteHistory() {
        try(SQLiteDatabase db = getWritableDatabase()){
            db.delete(WORD_TABLE_NAME, null, null);
        }
    }

    @Override
    public List<Word> getFavorites() {
        String select = "SELECT * FROM " + WORD_TABLE_NAME + " WHERE " + FAVORITE + "=1";
        return getWord(select);
    }

    @Override
    public void deleteFavorites() {
        try(SQLiteDatabase db = getWritableDatabase()){
            ContentValues values = new ContentValues();
            values.put(FAVORITE, 0);
            db.update(WORD_TABLE_NAME, values, null, null);
        }
    }
}
