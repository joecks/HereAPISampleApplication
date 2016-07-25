package x0r.hereapisampleapplication.ui.base.presenter;

import android.app.Activity;
import android.os.Bundle;

/**
 * Base presenter interface
 */
public interface Presenter {
    /**
     * Gets called whenever the {@link Activity#onCreate(Bundle)} of the corresponding activity is called
     */
    void create();

    /**
     * Gets called whenever the {@link Activity#onResume()} of the corresponding activity is called
     */
    void resume();

    /**
     * Gets called whenever the {@link Activity#onStop()} of the corresponding activity is called
     */
    void stop();

    /**
     * Gets called whenever the {@link Activity#onDestroy()} of the corresponding activity is called
     */
    void destroy();
}
