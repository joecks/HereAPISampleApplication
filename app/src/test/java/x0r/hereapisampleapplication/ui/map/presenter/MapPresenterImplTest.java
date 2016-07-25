package x0r.hereapisampleapplication.ui.map.presenter;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.routing.RouteManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;
import x0r.hereapisampleapplication.model.Suggestion;
import x0r.hereapisampleapplication.ui.map.view.MapView;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

/**
 *
 * Created by kirillrozhenkov on 24.07.16.
 *
 */
@PrepareForTest({MapMarker.class, GeoCoordinate.class})
@RunWith(PowerMockRunner.class)
public class MapPresenterImplTest {

    @Mock
    MapView mockMapView;

    @Mock
    RouteManager mockRouteManager;

    @Mock
    Observable<Void> mockObservable;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private MapPresenter mapPresenter;
    private GeoCoordinate mockGeoCoordinate;

    @Before
    public void setUp() throws Exception {
        mapPresenter = new MapPresenterImpl(mockMapView, mockRouteManager);
        mockGeoCoordinate = PowerMockito.mock(GeoCoordinate.class);

        MapMarker mockMapMarker = PowerMockito.mock(MapMarker.class);

        Mockito.when(mockGeoCoordinate.getLatitude()).thenReturn(0.0);
        Mockito.when(mockGeoCoordinate.getLongitude()).thenReturn(0.0);

        Mockito.when(mockMapMarker.getCoordinate()).thenReturn(mockGeoCoordinate);

        Mockito.when(mockMapView.getMapGestureObservable()).thenReturn(mockObservable);
        Mockito.when(mockMapView.getMapMarketFrom(anyDouble(), anyDouble(), anyInt())).thenReturn(mockMapMarker);
    }

    @After
    public void tearDown() throws Exception {
        mapPresenter.stop();
        mapPresenter.destroy();
        mapPresenter = null;
    }

    @Test
    public void testOnStartPointSelected() throws Exception {
        Suggestion suggestion = Mockito.mock(Suggestion.class);
        Mockito.when(suggestion.getTitle()).thenReturn("Mock Title");

        mapPresenter.onStartPointSelected(suggestion);

        /**
         * Verify that showOnMap is not called if map isn't prepared
         */
        Mockito.when(mockMapView.isMapInitCompleted()).thenReturn(false);
        Mockito.verify(mockMapView, atLeastOnce()).isMapInitCompleted();
        Mockito.verify(mockMapView, never()).showOnMap(any(MapObject.class));

        /**
         * Now change map state to initialized and verify the appropriate map interaction methods are called
         */
        Mockito.when(mockMapView.isMapInitCompleted()).thenReturn(true);
        mapPresenter.onStartPointSelected(suggestion);

        Mockito.verify(mockMapView, atLeastOnce()).isMapInitCompleted();
        Mockito.verify(mockMapView).showOnMap(any(MapObject.class));
        Mockito.verify(mockMapView).centerMap(mockGeoCoordinate);
        Mockito.verify(mockMapView).zoomMap(any(GeoBoundingBox.class));
        Mockito.verify(mockMapView).setStartPointText(eq("Mock Title"));
        Mockito.verify(mockMapView).setStartPointClearButtonVisible(true);
    }

    @Test
    public void testOnEndPointSelected() throws Exception {
        Suggestion suggestion = Mockito.mock(Suggestion.class);
        Mockito.when(suggestion.getTitle()).thenReturn("Mock Title");

        mapPresenter.onEndPointSelected(suggestion);

        /**
         * Verify that showOnMap is not called if map isn't prepared
         */
        Mockito.when(mockMapView.isMapInitCompleted()).thenReturn(false);
        Mockito.verify(mockMapView, atLeastOnce()).isMapInitCompleted();
        Mockito.verify(mockMapView, never()).showOnMap(any(MapObject.class));

        /**
         * Now change map state to initialized and verify the appropriate map interaction methods are called
         */
        Mockito.when(mockMapView.isMapInitCompleted()).thenReturn(true);
        mapPresenter.onEndPointSelected(suggestion);

        Mockito.verify(mockMapView, atLeastOnce()).isMapInitCompleted();
        Mockito.verify(mockMapView).showOnMap(any(MapObject.class));
        Mockito.verify(mockMapView).centerMap(mockGeoCoordinate);
        Mockito.verify(mockMapView).zoomMap(any(GeoBoundingBox.class));
        Mockito.verify(mockMapView).setEndPointText(eq("Mock Title"));
        Mockito.verify(mockMapView).setEndPointClearButtonVisible(true);
    }

    @Test
    public void testOnStartPointClearClick() throws Exception {
        mapPresenter.onStartPointClearClick();

        Mockito.verify(mockMapView).setStartPointText("");
        Mockito.verify(mockMapView).setStartPointClearButtonVisible(false);
    }

    @Test
    public void testOnEndPointClearClick() throws Exception {
        mapPresenter.onEndPointClearClick();

        Mockito.verify(mockMapView).setEndPointText("");
        Mockito.verify(mockMapView).setEndPointClearButtonVisible(false);
    }
}