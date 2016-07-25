package x0r.hereapisampleapplication.di;

import android.app.Application;
import android.content.Context;

import x0r.hereapisampleapplication.di.component.AppComponent;
import x0r.hereapisampleapplication.di.component.DaggerAppComponent;
import x0r.hereapisampleapplication.di.module.AppModule;


public class HereApplication extends Application {

    private AppComponent mAppComponent;

    public static HereApplication get(Context context) {
        return (HereApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = createAppComponent();
    }

    protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
