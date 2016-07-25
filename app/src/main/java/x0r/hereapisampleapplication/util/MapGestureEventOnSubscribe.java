package x0r.hereapisampleapplication.util;

import android.graphics.PointF;

import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.MapGesture;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

public class MapGestureEventOnSubscribe implements Observable.OnSubscribe<Void> {

    private final MapGesture mMapGesture;

    public MapGestureEventOnSubscribe(MapGesture mapGesture) {
        this.mMapGesture = mapGesture;
    }

    @Override
    public void call(Subscriber<? super Void> subscriber) {
        checkUiThread();

        final MapGesture.OnGestureListener gestureListener = new MapGesture.OnGestureListener() {

            @Override
            public void onPanStart() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }

            @Override
            public void onPanEnd() {
            }

            @Override
            public void onMultiFingerManipulationStart() {
            }

            @Override
            public void onMultiFingerManipulationEnd() {
            }

            @Override
            public boolean onMapObjectsSelected(List<ViewObject> list) {
                return false;
            }

            @Override
            public boolean onTapEvent(PointF pointF) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(PointF pointF) {
                return false;
            }

            @Override
            public void onPinchLocked() {
            }

            @Override
            public boolean onPinchZoomEvent(float v, PointF pointF) {
                return false;
            }

            @Override
            public void onRotateLocked() {
            }

            @Override
            public boolean onRotateEvent(float v) {
                return false;
            }

            @Override
            public boolean onTiltEvent(float v) {
                return false;
            }

            @Override
            public boolean onLongPressEvent(PointF pointF) {
                return false;
            }

            @Override
            public void onLongPressRelease() {
            }

            @Override
            public boolean onTwoFingerTapEvent(PointF pointF) {
                return false;
            }
        };

        mMapGesture.addOnGestureListener(gestureListener);

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                mMapGesture.removeOnGestureListener(gestureListener);
            }
        });
    }
}
