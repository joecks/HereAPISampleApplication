package x0r.hereapisampleapplication.ui.map.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RouteOptions.TransportMode;
import com.here.android.mpa.routing.RouteOptions.Type;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import x0r.hereapisampleapplication.BuildConfig;
import x0r.hereapisampleapplication.R;
import x0r.hereapisampleapplication.model.Suggestion;
import x0r.hereapisampleapplication.ui.base.presenter.BasePresenter;
import x0r.hereapisampleapplication.ui.map.view.MapView;

/**
 *
 * A default implementation of the {@link MapPresenter}
 *
 * @see MapView
 *
 */
public class MapPresenterImpl extends BasePresenter<MapView> implements MapPresenter {

    /**
     * A delay in ms between moment when user starts interacting with the {@link Map} and when UI's controls get hidden
     */
    private static final int HIDE_CONTROLS_DELAY_MS = 200;

    /**
     * A delay in ms between moment when user stops interacting with the {@link Map} and when UI's controls get shown back
     */
    private static final int SHOW_CONTROLS_DELAY_MS = 2000;

    private Suggestion mStartPointSuggestion;
    private Suggestion mEndPointSuggestion;

    private MapMarker mStartPointMapMarker;
    private MapMarker mEndPointMapMarker;

    /**
     * A latest built route
     */
    private MapRoute mLatestMapRoute;

    /**
     * A subscription to the observable created from {@link MapGesture}
     */
    private Subscription mMapGestureSubscription;

    /**
     * Holds route calculation state
     */
    private final AtomicBoolean mIsCalculatingRoute;

    private final RouteManager mRouteManager;
    private final RouteCalculationListener mRouteCalculationListener;

    @Inject
    public MapPresenterImpl(MapView view, RouteManager routeManager) {
        super(view);

        mIsCalculatingRoute = new AtomicBoolean(false);
        mRouteManager = routeManager;
        mRouteCalculationListener = new RouteCalculationListener();
    }

    @Override
    public void stop() {
        super.stop();

        /**
         * Cancel route's calculation when presenter stops its work (whenever {@link Activity} gets hidden)
         */
        if(mRouteManager.isBusy()) {
            mRouteManager.cancel();
        }

        /**
         * Restore route calculation and UI states
         */
        if(mIsCalculatingRoute.compareAndSet(true, false)) {
            if(getView() != null) {
                getView().setControlsVisible(true);
                getView().hideMsgBar();
            }
        }
    }

    @Override
    public void resume() {
        if(getView() != null && getView().isMapInitCompleted()) {
            subscribeMapGestures();
        }
    }

    private void showControls(Void event) {
        if(getView() != null) {
            getView().setControlsVisible(true);
        }
    }

    private void hideControls(Void event) {
        if(getView() != null) {
            getView().setControlsVisible(false);
        }
    }

    /**
     * Subscribes for {@link MapGesture} events
     *
     * Automatically hides UI's controls whenever interaction is detected after the {@link MapPresenterImpl#HIDE_CONTROLS_DELAY_MS} delay
     * Automatically shows UI's controls whenever interaction stopped after the {@link MapPresenterImpl#SHOW_CONTROLS_DELAY_MS} delay
     *
     * Doesn't perform any action if route is being calculated
     *
     * @see MapPresenterImpl#showControls(Void)
     * @see MapPresenterImpl#hideControls(Void)
     * @see MapPresenterImpl#handleError(Throwable)
     */
    private void subscribeMapGestures() {
        if(getView() != null && mMapGestureSubscription == null) {
            mMapGestureSubscription = unsubscribeOnFinish(
                    getView().getMapGestureObservable()
                        .debounce(HIDE_CONTROLS_DELAY_MS, TimeUnit.MILLISECONDS)
                        .filter(event -> !mIsCalculatingRoute.get())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(this::hideControls)
                        .debounce(SHOW_CONTROLS_DELAY_MS, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::showControls, this::handleError));
        }
    }

    /**
     * @see {@link MapPresenter#onMapInitFinished(OnEngineInitListener.Error)}
     * @param error {@link com.here.android.mpa.common.OnEngineInitListener.Error}
     */
    @Override
    public void onMapInitFinished(@Nullable OnEngineInitListener.Error error) {
        if(getView() != null) {

            if(error != null && error.getStackTrace() != null) {
                getView().showMsgBar(R.string.map_init_error, false);
            }
            else {
                subscribeMapGestures();
            }
        }
    }

    /**
     * @see {@link MapPresenter#onStartPointSelected(Suggestion)}
     * @param suggestion Selected {@link Suggestion}
     */
    @Override
    public void onStartPointSelected(@NonNull Suggestion suggestion) {
        mStartPointSuggestion = suggestion;

        if(getView() == null || !getView().isMapInitCompleted()) {
            return;
        }

        removeFromMap(mStartPointMapMarker);
        mStartPointMapMarker = getView().getMapMarketFrom(mStartPointSuggestion.getLatCoordinate(), mStartPointSuggestion.getLngCoordinate(), R.drawable.ic_map_point_start);

        showOnMap(mStartPointMapMarker);

        getView().setStartPointText(mStartPointSuggestion.getTitle());
        getView().setStartPointClearButtonVisible(true);

        calculateRoute();
    }

    /**
     * @see {@link MapPresenter#onEndPointSelected(Suggestion)}
     * @param suggestion Selected {@link Suggestion}
     */
    @Override
    public void onEndPointSelected(@NonNull Suggestion suggestion) {
        mEndPointSuggestion = suggestion;

        if(getView() == null || !getView().isMapInitCompleted()) {
            return;
        }

        removeFromMap(mEndPointMapMarker);
        mEndPointMapMarker = getView().getMapMarketFrom(mEndPointSuggestion.getLatCoordinate(), mEndPointSuggestion.getLngCoordinate(), R.drawable.ic_map_point_end);

        showOnMap(mEndPointMapMarker);

        getView().setEndPointText(mEndPointSuggestion.getTitle());
        getView().setEndPointClearButtonVisible(true);

        calculateRoute();
    }

    /**
     * @see {@link MapPresenter#onStartPointClearClick()}
     */
    @Override
    public void onStartPointClearClick() {
        if(getView() == null) {
            return;
        }

        removeFromMap(mStartPointMapMarker);
        removeFromMap(mLatestMapRoute);

        getView().setStartPointText("");
        getView().setStartPointClearButtonVisible(false);

        mStartPointSuggestion = null;
        mStartPointMapMarker = null;
        mLatestMapRoute = null;
    }

    /**
     * @see {@link MapPresenter#onEndPointClearClick()}
     */
    @Override
    public void onEndPointClearClick() {
        if(getView() == null) {
            return;
        }

        removeFromMap(mEndPointMapMarker);
        removeFromMap(mLatestMapRoute);

        getView().setEndPointText("");
        getView().setEndPointClearButtonVisible(false);

        mEndPointSuggestion = null;
        mEndPointMapMarker = null;
        mLatestMapRoute = null;
    }

    /**
     * @see {@link MapPresenterImpl#showOnMap(MapMarker, boolean)}
     * @param mapMarker
     */
    private void showOnMap(@Nullable MapMarker mapMarker) {
        showOnMap(mapMarker, true);
    }

    /**
     * Displays a {@link MapMarker} on a map
     *
     * @param mapMarker to be displayed
     * @param displayCentered <b>true</b> if map should be centered and zoomed in accordance with {@link GeoCoordinate} specified by mapMarker
     */
    private void showOnMap(@Nullable MapMarker mapMarker, boolean displayCentered) {
        if(getView() == null  || !getView().isMapInitCompleted() || mapMarker == null) {
            return;
        }

        getView().showOnMap(mapMarker);

        if(displayCentered) {
            getView().centerMap(mapMarker.getCoordinate());
            getView().zoomMap(new GeoBoundingBox(mapMarker.getCoordinate(), mapMarker.getCoordinate()));
        }
    }

    /**
     * Removes a {@link MapObject} from the map
     * @param mapObject to be removed
     */
    private void removeFromMap(@Nullable MapObject mapObject) {
        if(getView() == null  || !getView().isMapInitCompleted() || mapObject == null) {
            return;
        }

        getView().removeFromMap(mapObject);
    }

    /**
     * Check if start and end point are selected and performs a new route calculation by calling {@link MapPresenterImpl#performRouteCalculation()}
     */
    private void calculateRoute() {
        if(mStartPointSuggestion == null || mEndPointSuggestion == null || getView() == null) {
            return;
        }

        RouteManager.Error error = performRouteCalculation();

        /**
         * TODO: Provide a better explanation msg depending on the error's type
         */
        if(error != RouteManager.Error.NONE) {
            getView().showMsgBar(R.string.map_route_error_common, false);
        }
    }

    /**
     * Performs a route calculation
     * </br>
     * <b>Caution:</b> Route calculating is performed using {@link TransportMode#PEDESTRIAN} and {@link Type#SHORTEST}
     * @return Error
     */
    @Nullable
    private RouteManager.Error performRouteCalculation() {

        if(getView() == null) {
            return null;
        }

        /**
         * Another route calculating is in progress...
         */
        if(!mIsCalculatingRoute.compareAndSet(false, true)) {
            return null;
        }

        getView().setControlsVisible(false);
        getView().showMsgBar(R.string.map_route_calculation_progress, true);

        RoutePlan routePlan = new RoutePlan();
        RouteOptions routeOptions = new RouteOptions();

        routeOptions.setTransportMode(TransportMode.PEDESTRIAN);
        routeOptions.setRouteType(Type.SHORTEST);

        routePlan.setRouteOptions(routeOptions);
        routePlan.addWaypoint(new GeoCoordinate(mStartPointSuggestion.getLatCoordinate(), mStartPointSuggestion.getLngCoordinate()));
        routePlan.addWaypoint(new GeoCoordinate(mEndPointSuggestion.getLatCoordinate(), mEndPointSuggestion.getLngCoordinate()));

        RouteManager.Error error = mRouteManager.calculateRoute(routePlan, mRouteCalculationListener);

        if(error != RouteManager.Error.NONE) {
            mIsCalculatingRoute.set(false);
        }

        return error;
    }

    /**
     * Handles route calculation results. Shows a calculated route if one has been found by calling {@link MapPresenterImpl#displayNewRoute(MapRoute)}
     * @param results
     */
    private void onRouteCalculated(@Nullable List<RouteResult> results) {
        if(results == null || results.size() == 0) {

            mIsCalculatingRoute.set(false);

            if(getView() != null) {
                getView().showMsgBar(R.string.map_route_error_common, false);
                getView().setControlsVisible(true);
            }

            return;
        }

        Route route = null;

        for(RouteResult result : results) {
            if(result.getRoute() != null) {
                route = result.getRoute();
                break;
            }
        }

        if(route == null) {

            mIsCalculatingRoute.set(false);

            if(getView() != null) {
                getView().showMsgBar(R.string.map_route_error_common, false);
                getView().setControlsVisible(true);
            }

            return;
        }

        displayNewRoute(new MapRoute(route));
    }

    /**
     * Displays a new route on the map. Old route is removed if exists.
     * @param mapRoute
     */
    private void displayNewRoute(@NonNull MapRoute mapRoute) {

        mIsCalculatingRoute.set(false);

        if(getView() == null || !getView().isMapInitCompleted()) {
            return;
        }

        if(mLatestMapRoute != null) {
            getView().removeFromMap(mLatestMapRoute);
        }

        removeFromMap(mLatestMapRoute);

        mLatestMapRoute = mapRoute;

        getView().showOnMap(mLatestMapRoute);

        removeFromMap(mStartPointMapMarker);
        removeFromMap(mEndPointMapMarker);

        showOnMap(mStartPointMapMarker);
        showOnMap(mEndPointMapMarker);

        getView().zoomMap(mapRoute.getRoute().getBoundingBox());

        getView().setControlsVisible(true);
        getView().hideMsgBar();
    }

    /**
     * Handles errors from {@link MapGesture} observable
     * @param error
     */
    private void handleError(@NonNull Throwable error) {
        if(BuildConfig.DEBUG) {
            error.printStackTrace();
        }
    }

    private class RouteCalculationListener implements RouteManager.Listener {

        @Override
        public void onProgress(int i) {

        }

        @Override
        public void onCalculateRouteFinished(RouteManager.Error error, List<RouteResult> list) {
            onRouteCalculated(list);
        }
    }
}
