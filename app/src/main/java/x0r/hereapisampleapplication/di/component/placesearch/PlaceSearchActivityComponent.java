package x0r.hereapisampleapplication.di.component.placesearch;

import dagger.Subcomponent;
import x0r.hereapisampleapplication.di.module.placesearch.PlaceSearchActivityModule;
import x0r.hereapisampleapplication.di.scope.ActivityScope;
import x0r.hereapisampleapplication.ui.placesearch.activity.PlaceSearchActivity;

@ActivityScope
@Subcomponent(modules = PlaceSearchActivityModule.class)
public interface PlaceSearchActivityComponent {
    void inject(PlaceSearchActivity activity);
}
