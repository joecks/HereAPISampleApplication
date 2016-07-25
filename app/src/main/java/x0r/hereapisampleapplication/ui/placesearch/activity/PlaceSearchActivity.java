package x0r.hereapisampleapplication.ui.placesearch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import x0r.hereapisampleapplication.R;
import x0r.hereapisampleapplication.di.HereApplication;
import x0r.hereapisampleapplication.di.module.placesearch.PlaceSearchActivityModule;
import x0r.hereapisampleapplication.model.Suggestion;
import x0r.hereapisampleapplication.ui.base.activity.BaseActivity;
import x0r.hereapisampleapplication.ui.placesearch.adapter.PlacesSearchAdapter;
import x0r.hereapisampleapplication.ui.placesearch.presenter.PlaceSearchPresenter;
import x0r.hereapisampleapplication.ui.placesearch.view.PlaceSearchView;
import x0r.hereapisampleapplication.util.DividerItemDecoration;

/**
 *
 * Represents place search screen
 *
 * @see BaseActivity
 * @see PlaceSearchView
 * @see PlaceSearchPresenter
 * @see PlacesSearchAdapter
 *
 */
public class PlaceSearchActivity extends BaseActivity<PlaceSearchView, PlaceSearchPresenter> implements PlaceSearchView, PlacesSearchAdapter.ClickListener {

    public static final int REQUEST_CODE_SELECT_PLACE = 1000;
    public static final String EXTRA_SUGGESTION = "suggestion";

    @Inject
    PlaceSearchPresenter mPlaceSearchPresenter;

    @BindView(R.id.msg)
    TextView mMsgView;

    @BindView(R.id.search)
    EditText mSearchInput;

    @BindView(R.id.recycler_view)
    RecyclerView mContentView;

    private PlacesSearchAdapter mPlacesSearchAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
        ButterKnife.bind(this);

        mPlacesSearchAdapter = new PlacesSearchAdapter(this);

        mContentView.setLayoutManager(new LinearLayoutManager(this));
        mContentView.addItemDecoration(new DividerItemDecoration(this));
        mContentView.setAdapter(mPlacesSearchAdapter);
    }

    @Override
    protected PlaceSearchPresenter getPresenter() {
        return mPlaceSearchPresenter;
    }

    @Override
    protected void setPresenter(PlaceSearchPresenter presenter) {
        this.mPlaceSearchPresenter = presenter;
    }

    @Override
    public void setupComponents() {
        HereApplication.get(this)
                .getAppComponent()
                .getPlaceSearchActivityComponent(new PlaceSearchActivityModule(this))
                .inject(this);
    }

    @Override
    @NonNull
    public EditText getSearchInput() {
        return mSearchInput;
    }

    /**
     * @see {@link PlaceSearchView#showMsg(int)}
     * @param msgRes A string resource with a corresponding message
     */
    @Override
    public void showMsg(@StringRes int msgRes) {
        mMsgView.setText(msgRes);
        mMsgView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }

    /**
     * @see {@link PlaceSearchView#hideMsg()}
     */
    @Override
    public void hideMsg() {
        mMsgView.setVisibility(View.GONE);
    }

    /**
     * @see {@link PlaceSearchView#sendResultAndFinish(Suggestion)}
     * @param suggestion Suggestion
     */
    @Override
    public void sendResultAndFinish(@NonNull Suggestion suggestion) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_SUGGESTION, suggestion);

        setResult(RESULT_OK, intent);
        supportFinishAfterTransition();
    }

    /**
     * @see {@link PlaceSearchView#setContent(List)}
     * @param content
     */
    @Override
    public void setContent(@NonNull List<Suggestion> content) {
        mPlacesSearchAdapter.updateData(content);
        mMsgView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(Suggestion suggestion) {
        getPresenter().onSuggestionClick(suggestion);
    }
}
