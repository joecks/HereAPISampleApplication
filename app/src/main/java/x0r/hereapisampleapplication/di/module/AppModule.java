package x0r.hereapisampleapplication.di.module;

import android.app.Application;

import com.here.android.mpa.routing.RouteManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import x0r.hereapisampleapplication.BuildConfig;
import x0r.hereapisampleapplication.data.HereAPIFactory;
import x0r.hereapisampleapplication.domain.HereAPI;


@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public HereAPI provideHereAPI() {
        return HereAPIFactory.getHereAPI(BuildConfig.APP_ID, BuildConfig.APP_CODE);
    }

    @Singleton
    @Provides
    public RouteManager provideRouteManager() {
        return new RouteManager();
    }
}
