package x0r.hereapisampleapplication.ui.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import x0r.hereapisampleapplication.ui.base.presenter.Presenter;
import x0r.hereapisampleapplication.ui.base.view.BaseView;


/**
 * A base activity that maintenance base presenter operations
 *
 * @see BaseView
 * @see Presenter
 * @see AppCompatActivity
 */
public abstract class BaseActivity<V extends BaseView, P extends Presenter> extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponents();
        getPresenter().create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().resume();
    }

    @Override
    protected void onStop() {
        getPresenter().stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        getPresenter().destroy();
        super.onDestroy();
    }

    protected abstract P getPresenter();
    protected abstract void setPresenter(P presenter);
    protected abstract void setupComponents();
}
