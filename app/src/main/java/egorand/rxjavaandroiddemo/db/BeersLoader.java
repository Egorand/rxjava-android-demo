package egorand.rxjavaandroiddemo.db;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import egorand.rxjavaandroiddemo.model.Beer;

public class BeersLoader extends AsyncTaskLoader<List<Beer>> {

    private Lazy<RuntimeExceptionDao<Beer, String>> beersDao;

    private List<Beer> cachedResult;

    @Inject
    public BeersLoader(Context context, @BeersDao Lazy<RuntimeExceptionDao<Beer, String>> beersDao) {
        super(context);
        this.beersDao = beersDao;
    }

    @Override
    public List<Beer> loadInBackground() {
        return beersDao.get().queryForAll();
    }

    @Override
    public void deliverResult(List<Beer> result) {
        if (isReset()) {
            return;
        }
        if (isStarted()) {
            super.deliverResult(result);
        }
    }

    @Override
    protected void onStartLoading() {
        if (cachedResult != null) {
            deliverResult(cachedResult);
        }
        if (takeContentChanged() || cachedResult == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (cachedResult != null) {
            cachedResult.clear();
            cachedResult = null;
        }
    }
}
