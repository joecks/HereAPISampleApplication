package x0r.hereapisampleapplication.ui.map.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;

import x0r.hereapisampleapplication.model.Suggestion;
import x0r.hereapisampleapplication.ui.base.presenter.Presenter;
import x0r.hereapisampleapplication.ui.map.view.MapView;

/**
 *
 * Describes map presenter
 *
 * @see MapView
 * @see Presenter
 *
 */
public interface MapPresenter extends Presenter {
    /**
     * Called whenever an associated {@link Map} finishes its initialization
     * @param error {@link com.here.android.mpa.common.OnEngineInitListener.Error}
     */
    void onMapInitFinished(@Nullable OnEngineInitListener.Error error);

    /**
     * Called whenever the start point gets selected
     * @param suggestion Selected {@link Suggestion}
     */
    void onStartPointSelected(@NonNull Suggestion suggestion);

    /**
     * Called whenever the end point gets selected
     * @param suggestion Selected {@link Suggestion}
     */
    void onEndPointSelected(@NonNull Suggestion suggestion);

    /**
     * Called whenever the start point clear button gets clicked
     */
    void onStartPointClearClick();

    /**
     * Called whenever the start point clear button gets clicked
     */
    void onEndPointClearClick();
}
