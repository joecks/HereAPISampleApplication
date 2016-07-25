package x0r.hereapisampleapplication.ui.map.view;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;

import rx.Observable;
import x0r.hereapisampleapplication.ui.base.view.BaseView;

/**
 *
 * Base map view
 *
 * @see  BaseView
 *
 */
public interface MapView extends BaseView {
    /**
     * @return <b>true</b>is map initialization has been finished, <b>false</b> - otherwise
     */
    boolean isMapInitCompleted();

    /**
     * @param latitude
     * @param longitude
     * @param imageRes
     * @return An {@link MapMarker} constructed from given parameters
     */
    @Nullable MapMarker getMapMarketFrom(double latitude, double longitude, @DrawableRes int imageRes);

    /**
     * @return An observable used to observe map's interactions
     *
     * @see {@link com.here.android.mpa.mapping.MapGesture}
     */
    @NonNull Observable<Void> getMapGestureObservable();

    /**
     * Applies start's point text
     * @param text
     */
    void setStartPointText(@NonNull String text);

    /**
     * Applies end's point text
     * @param text
     */
    void setEndPointText(@NonNull String text);

    /**
     * Changes start's point clear button visibility
     * @param isVisible
     */
    void setStartPointClearButtonVisible(boolean isVisible);

    /**
     * Changes end's point clear button visibility
     * @param isVisible
     */
    void setEndPointClearButtonVisible(boolean isVisible);

    /**
     * Changes all available UI's controls visibility state
     * @param isVisible
     */
    void setControlsVisible(boolean isVisible);

    /**
     * Shows a bar containing the specified message
     * @param msgId String resource with the message to be shown
     * @param isIndefinite If <b>false</b> the message bar will remain visible until it explicitly hidden by {@link MapView#hideMsgBar()}
     */
    void showMsgBar(@StringRes int msgId, boolean isIndefinite);

    /**
     * Hide a message bar that has been shown with {@link MapView#showMsgBar(int, boolean)}
     */
    void hideMsgBar();

    /**
     * Shows a specified {@link MapObject} on a map
     * @param mapObject
     */
    void showOnMap(@NonNull MapObject mapObject);

    /**
     * Removes a {@link MapObject} that has been previously added with {@link MapView#showOnMap(MapObject)} from the map
     * @param mapObject
     */
    void removeFromMap(@NonNull MapObject mapObject);

    /**
     * Moves the map such that specified {@link GeoCoordinate} appears in the center
     * @param coordinate
     */
    void centerMap(@NonNull GeoCoordinate coordinate);

    /**
     * Zooms the map in accordance to {@link GeoBoundingBox} bounds
     * @param boundingBox
     */
    void zoomMap(@NonNull GeoBoundingBox boundingBox);
}
