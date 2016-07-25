package x0r.hereapisampleapplication.ui.base.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import x0r.hereapisampleapplication.ui.base.view.BaseView;


/**
 *
 * A base presenter that maintenance base view operations
 *
 * @see BaseView
 * @see Presenter
 *
 */
public abstract class BasePresenter<V extends BaseView> implements Presenter {

    /**
     * Holds a weak reference to a view to prevent memory leaks
     */
    private final WeakReference<V> mViewRef;

    /**
     * {@link CompositeSubscription} is used to automatically unsubscribe from all created subscriptions once presenter finished its work
     */
    private final CompositeSubscription mCompositeSubscription;

    public BasePresenter(V view) {
        mViewRef = new WeakReference<>(view);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void create() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
        if(mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription.clear();
        }
    }

    @Nullable
    public V getView() {
        return mViewRef.get();
    }

    protected Subscription unsubscribeOnFinish(@NonNull Subscription subscription) {
        mCompositeSubscription.add(subscription);
        return mCompositeSubscription;
    }
}
