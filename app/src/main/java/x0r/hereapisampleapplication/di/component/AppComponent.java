package x0r.hereapisampleapplication.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import x0r.hereapisampleapplication.di.component.map.MapActivityComponent;
import x0r.hereapisampleapplication.di.component.placesearch.PlaceSearchActivityComponent;
import x0r.hereapisampleapplication.di.module.AppModule;
import x0r.hereapisampleapplication.di.module.map.MapActivityModule;
import x0r.hereapisampleapplication.di.module.placesearch.PlaceSearchActivityModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Application getApplication();
    MapActivityComponent getMapActivityComponent(MapActivityModule mapActivityModule);
    PlaceSearchActivityComponent getPlaceSearchActivityComponent(PlaceSearchActivityModule placeSearchActivityModule);
}
