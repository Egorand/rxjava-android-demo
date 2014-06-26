package egorand.rxjavaandroiddemo.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.devspark.progressfragment.ProgressListFragment;

import java.util.List;

import javax.inject.Inject;

import egorand.rxjavaandroiddemo.MainActivity;
import egorand.rxjavaandroiddemo.db.BeersDao;
import egorand.rxjavaandroiddemo.model.Beer;
import egorand.rxjavaandroiddemo.model.BeerContainer;
import egorand.rxjavaandroiddemo.rest.BeersRestClient;
import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BeersFragment extends ProgressListFragment {

    @Inject BeersRestClient restClient;
    @Inject BeersDao beersDao;

    private Subscription subscription;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getObjectGraph().inject(this);

        setEmptyText("No beer today, sorry");
        loadData();
    }

    private void loadData() {
        subscription = AndroidObservable.bindFragment(this, getBeer())
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Beer>>() {
                    @Override public void call(List<Beer> beers) {
                        displayData(beers);
                    }
                });
    }

    private Observable<List<Beer>> getBeer() {
        return restClient.getBeers("5")
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
                .map(new Func1<List<Beer>, List<Beer>>() {
                    @Override public List<Beer> call(List<Beer> beers) {
                        beersDao.cacheBeer(beers);
                        return beers;
                    }
                })
                .onErrorResumeNext(Observable.just(beersDao.getCachedBeer()));
    }

    private void displayData(List<Beer> beers) {
        BeersAdapter adapter = ((MainActivity) getActivity()).getObjectGraph().get(BeersAdapter.class);
        adapter.setBeers(beers);
        setListAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
