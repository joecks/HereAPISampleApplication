package x0r.hereapisampleapplication.ui.placesearch.presenter;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import x0r.hereapisampleapplication.BuildConfig;
import x0r.hereapisampleapplication.R;
import x0r.hereapisampleapplication.domain.HereAPI;
import x0r.hereapisampleapplication.model.APIParameter;
import x0r.hereapisampleapplication.model.MediaType;
import x0r.hereapisampleapplication.model.Suggestion;
import x0r.hereapisampleapplication.model.SuggestionsResult;
import x0r.hereapisampleapplication.ui.base.presenter.BasePresenter;
import x0r.hereapisampleapplication.ui.placesearch.view.PlaceSearchView;

/**
 *
 * A default implementation of the {@link PlaceSearchPresenter}
 *
 * @see PlaceSearchView
 *
 */
public class PlaceSearchPresenterImpl extends BasePresenter<PlaceSearchView> implements PlaceSearchPresenter {

    /**
     * A minimal number of input's characters required for a request to the API to be sendt
     */
    private static final int MIN_INPUT_LENGTH = 2;

    /**
     * A delay in ms between moment when user ends typing in the search input and when input is handled
     */
    private static final int INPUT_READ_DELAY_MS = 500;

    private final HereAPI hereAPI;

    /**
     * A subscription to the observable created from {@link PlaceSearchView#getSearchInput()}
     */
    private Subscription mInputSubscription;

    @Inject
    public PlaceSearchPresenterImpl(PlaceSearchView view, HereAPI hereAPI) {
        super(view);
        this.hereAPI = hereAPI;
    }

    /**
     * @see {@link BasePresenter#resume()}
     *
     * TODO: Obtain a real user's location instead of using hardcoded value
     */
    @Override
    public void resume() {
        if(getView() != null && mInputSubscription == null) {
            /**
             * 1) Subscribe for text input changes
             * 2) React only after {@link PlaceSearchPresenterImpl#INPUT_READ_DELAY_MS} after latest interaction
             * 3) Show help msg depending on input by calling {@link PlaceSearchPresenterImpl#showHelp(CharSequence)}
             * 4) Filter inputs with {@link PlaceSearchPresenterImpl#isValidInput(CharSequence)}
             * 5) Call {@link HereAPI#suggest(String, String)} using input as a parameter
             * 6) Show no results message depending on result with {@link PlaceSearchPresenterImpl#showNoResultsMsg(SuggestionsResult)}
             * 7) Filter invalid results with {@link PlaceSearchPresenterImpl#isValidResult(SuggestionsResult)}
             * 8) Filter results with {@link PlaceSearchPresenterImpl#filterSuggestions(List)}
             * 9) Show content with {@link PlaceSearchPresenterImpl#showContent(List)}
             */
            mInputSubscription = unsubscribeOnFinish(
                    RxTextView.textChanges(getView().getSearchInput())
                            .debounce(INPUT_READ_DELAY_MS, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(this::showHelp)
                            .filter(this::isValidInput)
                            .flatMap(input -> hereAPI.suggest(APIParameter.HARDCODED_GEOLOCATION, input.toString()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(this::showNoResultsMsg)
                            .filter(this::isValidResult)
                            .map(SuggestionsResult::getResults)
                            .map(this::filterSuggestions)
                            .subscribe(this::showContent, this::handleException));
        }
    }

    /**
     * @see {@link PlaceSearchPresenter#onSuggestionClick(Suggestion)}
     * @param suggestion
     */
    @Override
    public void onSuggestionClick(@NonNull Suggestion suggestion) {
        if(getView() != null) {
            getView().sendResultAndFinish(suggestion);
        }
    }

    /**
     * Filters suggestions list by {@link MediaType}
     * @param suggestions
     * @return
     */
    @NonNull
    private List<Suggestion> filterSuggestions(@NonNull List<Suggestion> suggestions) {
        List<Suggestion> filteredList = new ArrayList<>();

        for(Suggestion suggestion : suggestions) {
            if(MediaType.PLACE.equalsIgnoreCase(suggestion.getType())) {
                filteredList.add(suggestion);
            }
        }

        return filteredList;
    }

    /**
     * Show a help message depending on input
     *
     * @param input
     * @see {@link PlaceSearchPresenterImpl#isValidInput(CharSequence)}
     */
    private void showHelp(CharSequence input) {
        if(getView() == null) {
            return;
        }

        if(isValidInput(input)) {
            getView().hideMsg();
        }
        else {
            getView().showMsg(R.string.search_help);
        }
    }

    /**
     * Shows no results message depending on {@link SuggestionsResult}
     *
     * @param result Result
     * @see {@link PlaceSearchPresenterImpl#isValidResult(SuggestionsResult)}
     */
    private void showNoResultsMsg(SuggestionsResult result) {
        if(getView() == null) {
            return;
        }

        if(!isValidResult(result)) {
            getView().showMsg(R.string.search_no_results);
        }
        else {
            getView().hideMsg();
        }
    }

    /**
     * Presents content
     * @param content Content
     */
    private void showContent(List<Suggestion> content) {
        if(getView() != null) {
            getView().setContent(content);
        }
    }

    /**
     * Handles errors
     * @param error Error
     */
    private void handleException(@NonNull Throwable error) {
        if(BuildConfig.DEBUG) {
            error.printStackTrace();
        }

        if(getView() != null) {
            getView().showMsg(R.string.search_common_error);
        }
    }

    /**
     * @param input
     * @return <b>true</b> if input is valid, <b>false</b> - otherwise
     */
    private boolean isValidInput(CharSequence input) {
        return input.length() > MIN_INPUT_LENGTH;
    }

    /**
     * @param result
     * @return <b>true</b> if result is valid, <b>false</b> - otherwise
     */
    private boolean isValidResult(SuggestionsResult result) {
        return result != null && result.getResults() != null && result.getResults().size() > 0;
    }
}
