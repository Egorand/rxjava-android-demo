package egorand.rxjavaandroiddemo.db;

import java.util.List;
import java.util.concurrent.Callable;

import egorand.rxjavaandroiddemo.model.Beer;
import rx.Observable;
import rx.Subscriber;

public class BeerCache {

    private BeersDatabaseHelper databaseHelper;

    public BeerCache(BeersDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public Observable<List<Beer>> cacheBeer(final List<Beer> beers) {
        return Observable.create(new Observable.OnSubscribe<List<Beer>>() {
            @Override public void call(Subscriber<? super List<Beer>> subscriber) {
                databaseHelper.getBeersDao().callBatchTasks(new BatchBeerCachingTask(beers));
                subscriber.onNext(beers);
                subscriber.onCompleted();
            }
        });
    }

    private class BatchBeerCachingTask implements Callable<Void> {

        private List<Beer> beers;

        private BatchBeerCachingTask(List<Beer> beers) {
            this.beers = beers;
        }

        @Override
        public Void call() throws Exception {
            for (Beer beer : beers) {
                databaseHelper.getBeersDao().createOrUpdate(beer);
            }
            return null;
        }
    }
}
