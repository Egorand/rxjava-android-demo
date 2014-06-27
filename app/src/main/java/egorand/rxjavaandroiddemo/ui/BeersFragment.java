package egorand.rxjavaandroiddemo.ui;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import egorand.rxjavaandroiddemo.MainActivity;
import egorand.rxjavaandroiddemo.R;
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

public class BeersFragment extends Fragment {

    @InjectView(android.R.id.list) RecyclerView list;
    @InjectView(android.R.id.empty) TextView empty;
    @InjectView(android.R.id.progress) ProgressBar progress;

    @Inject BeersRestClient restClient;
    @Inject BeersDao beersDao;

    private Subscription subscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getObjectGraph().inject(this);

        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        progress.setVisibility(View.VISIBLE);
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
        progress.setVisibility(View.GONE);
        BeersAdapter adapter = ((MainActivity) getActivity()).getObjectGraph().get(BeersAdapter.class);
        adapter.setBeers(beers);
        list.setAdapter(adapter);
        if (beers.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
