package egorand.rxjavaandroiddemo;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;

import de.greenrobot.event.EventBus;
import egorand.rxjavaandroiddemo.db.BeerCache;
import egorand.rxjavaandroiddemo.db.BeersDatabaseHelper;
import egorand.rxjavaandroiddemo.db.BeersLoader;
import egorand.rxjavaandroiddemo.model.Beer;
import egorand.rxjavaandroiddemo.model.BeerContainer;
import egorand.rxjavaandroiddemo.rest.BeersRestClient;
import egorand.rxjavaandroiddemo.ui.BeersAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<List<Beer>> {

    private BeersRestClient restClient;
    private BeersDatabaseHelper databaseHelper;
    private BeerCache beerCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        setupRestClient();
        databaseHelper = OpenHelperManager.getHelper(this, BeersDatabaseHelper.class);
        beerCache = new BeerCache(databaseHelper);

        setProgressBarVisibility(true);
        restClient.getBeers("5", new Callback<BeerContainer>() {
            @Override public void success(BeerContainer beerContainer, Response response) {
                beerCache.cacheBeer(beerContainer.getBeer());
            }

            @Override public void failure(RetrofitError error) {
                getLoaderManager().initLoader(0, null, MainActivity.this);
            }
        });
    }

    private void setupRestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.brewerydb.com/v2")
                .setExecutors(AsyncTask.THREAD_POOL_EXECUTOR, new MainThreadExecutor())
                .build();
        restClient = restAdapter.create(BeersRestClient.class);
    }

    private void initAdapter(List<Beer> beers) {
        BeersAdapter adapter = new BeersAdapter(this, beers);
        setListAdapter(adapter);
        setProgressBarVisibility(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OpenHelperManager.releaseHelper();
    }

    @Override
    public Loader<List<Beer>> onCreateLoader(int id, Bundle args) {
        return new BeersLoader(this, databaseHelper);
    }

    @Override
    public void onLoadFinished(Loader<List<Beer>> loader, List<Beer> data) {
        initAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Beer>> loader) {
        setListAdapter(null);
    }

    public void onEventMainThread(List<Beer> cachedBeers) {
        initAdapter(cachedBeers);
    }
}
