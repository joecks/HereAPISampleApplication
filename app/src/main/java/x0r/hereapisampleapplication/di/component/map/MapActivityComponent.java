package x0r.hereapisampleapplication.di.component.map;

import dagger.Subcomponent;
import x0r.hereapisampleapplication.di.module.map.MapActivityModule;
import x0r.hereapisampleapplication.di.scope.ActivityScope;
import x0r.hereapisampleapplication.ui.map.activity.MapActivity;

@ActivityScope
@Subcomponent(modules = MapActivityModule.class)
public interface MapActivityComponent {
    void inject(MapActivity mapActivity);
}
