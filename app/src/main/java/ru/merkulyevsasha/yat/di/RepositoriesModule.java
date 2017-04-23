package ru.merkulyevsasha.yat.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.yat.data.YatRepository;
import ru.merkulyevsasha.yat.data.YatRepositoryImpl;
import ru.merkulyevsasha.yat.data.db.DbDataSource;
import ru.merkulyevsasha.yat.data.http.HttpDataSource;
import ru.merkulyevsasha.yat.data.pref.YatSharedPreferences;


@Module
public class RepositoriesModule {

    @Singleton
    @Provides
    YatRepository providesYatRepository(DbDataSource db, HttpDataSource http, YatSharedPreferences pref) {
        return new YatRepositoryImpl(db, http, pref);
    }

}
