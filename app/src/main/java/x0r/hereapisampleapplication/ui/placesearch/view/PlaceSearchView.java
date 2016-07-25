package x0r.hereapisampleapplication.ui.placesearch.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.EditText;

import java.util.List;

import x0r.hereapisampleapplication.model.Suggestion;
import x0r.hereapisampleapplication.ui.base.view.BaseView;

/**
 *
 * Describes Place Search View
 *
 * @see BaseView
 *
 */
public interface PlaceSearchView extends BaseView {
    /**
     * @return {@link EditText} belong to the user's search input
     */
    @NonNull EditText getSearchInput();

    /**
     * Applies content to the view
     * @param suggestions A collection of {@link Suggestion}
     */
    void setContent(@NonNull List<Suggestion> suggestions);

    /**
     * Show a message to the user
     * @param msgRes A string resource with a corresponding message
     */
    void showMsg(@StringRes int msgRes);

    /**
     * Hides a message that has been previously shown to the user
     */
    void hideMsg();

    /**
     * Sends the specified {@link Suggestion} as a result to the caller and finishes its work
     * @param suggestion Suggestion
     */
    void sendResultAndFinish(@NonNull Suggestion suggestion);
}
