package ru.merkulyevsasha.yat.di;


import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.yat.data.YatRepository;
import ru.merkulyevsasha.yat.domain.YatInteractor;
import ru.merkulyevsasha.yat.domain.YatInteractorImpl;


@Module
public class InteractorsModule {

    @Singleton
    @Provides
    YatInteractor providesYatInteractor(ExecutorService serv, YatRepository repo) {
        return new YatInteractorImpl(serv, repo);
    }



}
