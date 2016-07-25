package x0r.hereapisampleapplication.ui.placesearch.presenter;

import android.support.annotation.NonNull;

import x0r.hereapisampleapplication.model.Suggestion;
import x0r.hereapisampleapplication.ui.base.presenter.Presenter;

/**
 *
 * Describes place search presenter
 *
 * @see Presenter
 *
 */
public interface PlaceSearchPresenter extends Presenter {
    /**
     * Called whenever user clicks a {@link Suggestion}
     * @param suggestion
     */
    void onSuggestionClick(@NonNull Suggestion suggestion);
}
