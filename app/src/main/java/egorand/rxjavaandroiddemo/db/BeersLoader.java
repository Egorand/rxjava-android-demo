package egorand.rxjavaandroiddemo.db;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import egorand.rxjavaandroiddemo.model.Beer;

public class BeersLoader extends AsyncTaskLoader<List<Beer>> {

    private BeersDatabaseHelper databaseHelper;

    private List<Beer> cachedResult;

    public BeersLoader(Context context, BeersDatabaseHelper databaseHelper) {
        super(context);
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<Beer> loadInBackground() {
        return databaseHelper.getBeersDao().queryForAll();
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
