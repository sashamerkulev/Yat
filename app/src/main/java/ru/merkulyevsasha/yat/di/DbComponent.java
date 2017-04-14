package ru.merkulyevsasha.yat.di;



import javax.inject.Singleton;

import dagger.Component;
import ru.merkulyevsasha.yat.presentation.YatActivity;


@Singleton
@Component(modules={AppModule.class, DbModule.class, RepositoriesModule.class, InteractorsModule.class, PresentersModule.class})
public interface DbComponent {

    void inject(YatActivity context);

}
