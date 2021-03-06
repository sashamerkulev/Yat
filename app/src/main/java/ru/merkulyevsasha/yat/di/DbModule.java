package ru.merkulyevsasha.yat.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.yat.data.db.DbDataSource;
import ru.merkulyevsasha.yat.data.db.DbDataSourceImpl;
import ru.merkulyevsasha.yat.data.http.HttpDataSource;
import ru.merkulyevsasha.yat.data.http.HttpDataSourceImpl;
import ru.merkulyevsasha.yat.data.pref.YatSharedPreferences;
import ru.merkulyevsasha.yat.data.pref.YatSharedPreferencesImpl;


@Module
public class DbModule {

    @Singleton
    @Provides
    DbDataSource providesDbDataSource(Context context) {
        return new DbDataSourceImpl(context);
    }

    @Singleton
    @Provides
    HttpDataSource providesHttpDataSource() {
        return new HttpDataSourceImpl();
    }

    @Singleton
    @Provides
    YatSharedPreferences providesYatSharedPreferences(Context context) {
        return new YatSharedPreferencesImpl(context);
    }
}
