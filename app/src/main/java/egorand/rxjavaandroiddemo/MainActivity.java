package egorand.rxjavaandroiddemo;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.StrictMode;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;

import egorand.rxjavaandroiddemo.db.BeerCache;
import egorand.rxjavaandroiddemo.db.BeersDatabaseHelper;
import egorand.rxjavaandroiddemo.db.BeersLoader;
import egorand.rxjavaandroiddemo.model.Beer;
import egorand.rxjavaandroiddemo.model.BeerContainer;
import egorand.rxjavaandroiddemo.rest.BeersRestClient;
import egorand.rxjavaandroiddemo.ui.BeersAdapter;
import retrofit.RestAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<List<Beer>>,
        Observer<List<Beer>> {

    private BeersRestClient restClient;
    private BeersDatabaseHelper databaseHelper;
    private BeerCache beerCache;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyDeath().build();
        StrictMode.setThreadPolicy(threadPolicy);

        setupRestClient();
        databaseHelper = OpenHelperManager.getHelper(this, BeersDatabaseHelper.class);
        beerCache = new BeerCache(databaseHelper);

        setProgressBarVisibility(true);
        subscription = AndroidObservable.bindActivity(this, restClient.getBeers("5"))
                .flatMap(new Func1<BeerContainer, Observable<Beer>>() {
                    @Override public Observable<Beer> call(BeerContainer beerContainer) {
                        return Observable.from(beerContainer.getBeer());
                    }
                })
                .filter(new Func1<Beer, Boolean>() {
                    @Override public Boolean call(Beer beer) {
                        return beer.getName().startsWith("B");
                    }
                })
                .toList()
                .doOnNext(new Action1<List<Beer>>() {
                    @Override public void call(List<Beer> beers) {
                        beerCache.cacheBeer(beers);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private void setupRestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.brewerydb.com/v2")
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
        subscription.unsubscribe();
        OpenHelperManager.releaseHelper();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable throwable) {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onNext(List<Beer> beers) {
        initAdapter(beers);
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
}
