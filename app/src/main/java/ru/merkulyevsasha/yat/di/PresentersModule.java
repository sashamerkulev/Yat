package ru.merkulyevsasha.yat.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.yat.domain.YatInteractor;
import ru.merkulyevsasha.yat.presentation.YatPresenterImpl;


@Module
public class PresentersModule {

    @Singleton
    @Provides
    YatPresenterImpl providesYatPresenter(YatInteractor inter) {
        return new YatPresenterImpl(inter);
    }


}
