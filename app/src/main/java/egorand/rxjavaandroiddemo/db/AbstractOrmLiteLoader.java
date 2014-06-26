package egorand.rxjavaandroiddemo.db;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

/**
 * An abstract superclass for the ORMLite Loader classes, which closely resembles to the Android's
 * <code>CursorLoader</code>. Implements basic loading and synchronization logic.
 *
 * @author EgorAnd
 */
public abstract class AbstractOrmLiteLoader<T, ID> extends AsyncTaskLoader<T> {

    /**
     * A {@link com.j256.ormlite.dao.Dao} which will be queried for the data.
     */
    private RuntimeExceptionDao<T, ID> dao;

    private T cachedResult;

    public AbstractOrmLiteLoader(Context context) {
        super(context);
    }

    public AbstractOrmLiteLoader(Context context, RuntimeExceptionDao<T, ID> dao) {
        super(context);
        this.dao = dao;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(T result) {
        if (isStarted()) {
            super.deliverResult(result);
        }
    }

    /**
     * Starts an asynchronous load of the data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p/>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (cachedResult != null) {
            deliverResult(cachedResult);
        }
        if (takeContentChanged() || cachedResult == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();
    }

    public void setDao(RuntimeExceptionDao<T, ID> dao) {
        this.dao = dao;
    }

    public RuntimeExceptionDao<T, ID> getDao() {
        return dao;
    }
}