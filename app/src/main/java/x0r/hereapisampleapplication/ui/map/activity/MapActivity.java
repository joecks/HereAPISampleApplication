package x0r.hereapisampleapplication.ui.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.TextView;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import x0r.hereapisampleapplication.R;
import x0r.hereapisampleapplication.di.HereApplication;
import x0r.hereapisampleapplication.di.module.map.MapActivityModule;
import x0r.hereapisampleapplication.model.Suggestion;
import x0r.hereapisampleapplication.ui.base.activity.BaseActivity;
import x0r.hereapisampleapplication.ui.map.presenter.MapPresenter;
import x0r.hereapisampleapplication.ui.map.view.MapView;
import x0r.hereapisampleapplication.ui.placesearch.activity.PlaceSearchActivity;
import x0r.hereapisampleapplication.util.MapGestureEventOnSubscribe;
import x0r.hereapisampleapplication.util.MapUtils;

/**
 * Represents app's main screen
 *
 * @see MapView
 * @see MapPresenter
 *
 * <br/><br/>
 * <b>Things to consider:</b>
 * </br>&nbsp;1) currently the activity is stick to the portrait mode and survives orientation changes due to <b>configChanges</b>.
 * Consider implementing activity's and presenter's state saving/restoring mechanism
 *
 */
public class MapActivity extends BaseActivity<MapView, MapPresenter> implements MapView {

    private static final String EXTRA_FIELD_ID = "field_id";

    @Inject
    MapPresenter mMapPresenter;

    @BindView(R.id.root_view)
    View mRootView;

    @BindView(R.id.start_point_clear)
    View mStartPointClearButton;

    @BindView(R.id.end_point_clear)
    View mEndPointClearButton;

    @BindView(R.id.start_point_text)
    TextView mStartPointTextView;

    @BindView(R.id.end_point_text)
    TextView mEndPointTextView;

    private Snackbar mMsgBar;
    private MapFragment mMapFragment;

    @OnClick({R.id.start_point, R.id.end_point})
    protected void onPointSelectClick(View v) {
        Intent intent = new Intent(this, PlaceSearchActivity.class);
        intent.putExtra(EXTRA_FIELD_ID, v.getId());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, getString(R.string.search_field_transition));
        startActivityForResult(intent, PlaceSearchActivity.REQUEST_CODE_SELECT_PLACE, options.toBundle());
    }

    @OnClick({R.id.start_point_clear, R.id.end_point_clear})
    protected void onPointClearClick(View v) {
        if (v.getId() == R.id.start_point_clear) {
            getPresenter().onStartPointClearClick();
        } else {
            getPresenter().onEndPointClearClick();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mMapFragment.init(getPresenter()::onMapInitFinished);
    }

    @Override
    protected MapPresenter getPresenter() {
        return this.mMapPresenter;
    }

    @Override
    protected void setPresenter(MapPresenter presenter) {
        this.mMapPresenter = presenter;
    }

    @Override
    public void setupComponents() {
        HereApplication.get(this)
                .getAppComponent()
                .getMapActivityComponent(new MapActivityModule(this))
                .inject(this);
    }

    @Override
    public boolean isMapInitCompleted() {
        return mMapFragment.getMap() != null;
    }

    @Nullable
    @Override
    public MapMarker getMapMarketFrom(double latitude, double longitude, @DrawableRes int imageRes) {
        return MapUtils.getMapMarketFrom(latitude, longitude, imageRes);
    }

    @NonNull
    @Override
    public Observable<Void> getMapGestureObservable() {
        return Observable.create(new MapGestureEventOnSubscribe(mMapFragment.getMapGesture()));
    }

    @Override
    public void setStartPointText(@NonNull String text) {
        mStartPointTextView.setText(text.length() > 0 ? String.format(getString(R.string.start_point_text_formatted), text) : text);
    }

    @Override
    public void setEndPointText(@NonNull String text) {
        mEndPointTextView.setText(text.length() > 0 ? String.format(getString(R.string.end_point_text_formatted), text) : text);
    }

    @Override
    public void setStartPointClearButtonVisible(boolean isVisible) {
        mStartPointClearButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEndPointClearButtonVisible(boolean isVisible) {
        mEndPointClearButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setControlsVisible(boolean isVisible) {
        ((View) mStartPointTextView.getParent()).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        ((View) mEndPointTextView.getParent()).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMsgBar(@StringRes int msgId, boolean isIndefinite) {
        hideMsgBar();

        mMsgBar = Snackbar.make(mRootView, msgId, isIndefinite ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG);
        mMsgBar.show();
    }

    @Override
    public void hideMsgBar() {
        if (mMsgBar != null && mMsgBar.isShownOrQueued()) {
            mMsgBar.dismiss();
        }
    }

    @Override
    public void showOnMap(@NonNull MapObject mapObject) {
        mMapFragment.getMap().addMapObject(mapObject);
    }

    @Override
    public void removeFromMap(@NonNull MapObject mapObject) {
        mMapFragment.getMap().removeMapObject(mapObject);
    }

    @Override
    public void centerMap(@NonNull GeoCoordinate coordinate) {
        mMapFragment.getMap().setCenter(coordinate, Map.Animation.LINEAR);
    }

    @Override
    public void zoomMap(@NonNull GeoBoundingBox boundingBox) {
        mMapFragment.getMap().zoomTo(boundingBox, Map.Animation.LINEAR, Map.MOVE_PRESERVE_ORIENTATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PlaceSearchActivity.REQUEST_CODE_SELECT_PLACE && resultCode == RESULT_OK) {
            Suggestion suggestion = data.getParcelableExtra(PlaceSearchActivity.EXTRA_SUGGESTION);

            if (data.getIntExtra(EXTRA_FIELD_ID, 0) == R.id.start_point) {
                getPresenter().onStartPointSelected(suggestion);
            } else {
                getPresenter().onEndPointSelected(suggestion);
            }
        }
    }
}
