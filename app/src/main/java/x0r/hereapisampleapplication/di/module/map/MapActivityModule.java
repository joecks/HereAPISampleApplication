package x0r.hereapisampleapplication.di.module.map;

import dagger.Module;
import dagger.Provides;
import x0r.hereapisampleapplication.di.scope.ActivityScope;
import x0r.hereapisampleapplication.ui.map.presenter.MapPresenter;
import x0r.hereapisampleapplication.ui.map.presenter.MapPresenterImpl;
import x0r.hereapisampleapplication.ui.map.view.MapView;

@Module
public class MapActivityModule {

    private final MapView mMapView;

    public MapActivityModule(MapView mapView) {
        this.mMapView = mapView;
    }

    @ActivityScope
    @Provides
    MapView provideMapView() {
        return this.mMapView;
    }

    @ActivityScope
    @Provides
    MapPresenter provideMapPresenter(MapPresenterImpl presenter) {
        return presenter;
    }
}
