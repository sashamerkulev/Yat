package ru.merkulyevsasha.yat;

import android.app.Application;

import ru.merkulyevsasha.yat.di.AppModule;
import ru.merkulyevsasha.yat.di.DaggerDbComponent;
import ru.merkulyevsasha.yat.di.DbComponent;
import ru.merkulyevsasha.yat.di.DbModule;
import ru.merkulyevsasha.yat.di.InteractorsModule;
import ru.merkulyevsasha.yat.di.PresentersModule;
import ru.merkulyevsasha.yat.di.RepositoriesModule;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public class YatApp extends Application {

    private static DbComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerDbComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule())
                .repositoriesModule(new RepositoriesModule())
                .interactorsModule(new InteractorsModule())
                .presentersModule(new PresentersModule())
                .build();
    }

    public static DbComponent getComponent() {
        return component;
    }

}
