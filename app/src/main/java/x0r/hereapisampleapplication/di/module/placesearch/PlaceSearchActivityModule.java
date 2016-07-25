package x0r.hereapisampleapplication.di.module.placesearch;

import dagger.Module;
import dagger.Provides;
import x0r.hereapisampleapplication.di.scope.ActivityScope;
import x0r.hereapisampleapplication.ui.placesearch.presenter.PlaceSearchPresenter;
import x0r.hereapisampleapplication.ui.placesearch.presenter.PlaceSearchPresenterImpl;
import x0r.hereapisampleapplication.ui.placesearch.view.PlaceSearchView;


@Module
public class PlaceSearchActivityModule {

    private final PlaceSearchView mPlaceSearchView;

    public PlaceSearchActivityModule(PlaceSearchView placeSearchView) {
        this.mPlaceSearchView = placeSearchView;
    }

    @ActivityScope
    @Provides
    PlaceSearchView providePlaceSearchView() {
        return this.mPlaceSearchView;
    }

    @ActivityScope
    @Provides
    PlaceSearchPresenter providePlaceSearchPresenter(PlaceSearchPresenterImpl presenter) {
        return presenter;
    }
}

