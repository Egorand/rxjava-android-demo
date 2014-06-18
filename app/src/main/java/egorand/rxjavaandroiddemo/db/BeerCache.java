package egorand.rxjavaandroiddemo.db;

import java.util.List;
import java.util.concurrent.Callable;

import de.greenrobot.event.EventBus;
import egorand.rxjavaandroiddemo.model.Beer;

public class BeerCache {

    private BeersDatabaseHelper databaseHelper;

    public BeerCache(BeersDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void cacheBeer(List<Beer> beers) {
        databaseHelper.getBeersDao().callBatchTasks(new BatchBeerCachingTask(beers));
        EventBus.getDefault().post(beers);
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
